package com.skunkworks.core

import com.auth0.client.auth.AuthAPI
import com.auth0.client.mgmt.ManagementAPI
import com.auth0.json.auth.TokenHolder
import com.auth0.net.Request
import com.skunkworks.core.domain.Configuration
import zio.{Ref, Semaphore, Task, TaskLayer, UIO, ZIO, ZLayer}

import java.time.Instant

final case class Client(configuration: Configuration, semaphore: Semaphore, ref: Ref[TokenHolder], auth: AuthAPI, management: ManagementAPI) {

  /**
   * Ensures that the currently used Access Token hasn't expired.
   *
   * @return a Boolean
   */
  private def isTokenExpired: UIO[Boolean] = {
    ref.get.map { token =>
      token.getExpiresAt.toInstant.isAfter(Instant.now())
    }
  }

  /**
   * Renews the Access Token before setting it to be used by the [[ManagementAPI]].
   *
   * @return
   */
  private def renewAccessToken(): Task[Unit] = {
    val request = auth.requestToken(s"${configuration.domain}/api/v2/")

    for {
      _     <- ZIO.logDebug("Access Token Expired, refreshing...")
      token <- ZIO.fromCompletableFuture(request.executeAsync())
      _      = management.setApiToken(token.getAccessToken)
      _     <- ref.set(token)
      _     <- ZIO.logDebug("Access Token Refreshed, performing request...")
    } yield ()
  }


  /**
   * Executes the provided [[Request]], the request is passed as a function rather than it's final type. This is due to the nature of the
   * underlying Java API.
   *
   * When a [[Request]] is created using the [[ManagementAPI]] the token that is currently being used is copied into the [[Request]] object.
   * If the token expires and we refresh it, we can't simply retry the [[Request]] as it's own token won't have been updated and therefore
   * subsequent calls will still fail.
   *
   * Passing a function allows us to rebuild the [[Request]] should the token need to be refreshed.
   *
   * // TODO: Pass the request parameter as a [[Task]] instead of a Function
   *
   * @param request a function to build the [[Request]] object
   * @tparam A the response type the [[Request]] yields
   * @return the response type of [[A]]
   */
  private[skunkworks] def execute[A](request: () => Request[A]): Task[A] = {
    semaphore.withPermit {
      for {
        isTokenExpired <- isTokenExpired
        _              <- renewAccessToken().when(isTokenExpired)
        result         <- ZIO.fromCompletableFuture(request().executeAsync())
      } yield result
    }
  }

  object Client {

    /**
     * Creates a new instance of the [[AuthAPI]], this is used to retrieve Access Tokens to be used in follow up requests to the
     * Auth0 Management API using the [[ManagementAPI]] class.
     *
     * @param configuration the Auth0 [[Configuration]] containing the Client ID, Client Secret, and Auth0 Tenant Domain.
     * @return a new instance of the [[AuthAPI]]
     */
    private def createAuthApi(configuration: Configuration): AuthAPI = {
      new AuthAPI(
        configuration.domain,
        configuration.clientId,
        configuration.clientSecret
      )
    }

    /**
     * This function retrieves the initial Access Token so we can create an instance of the [[ManagementAPI]].
     *
     * @param configuration the Auth0 [[Configuration]]
     * @param authAPI       the [[AuthAPI]] instance
     * @return the [[TokenHolder]] containing the new Access Token.
     */
    private def getAccessToken(configuration: Configuration, authAPI: AuthAPI): Task[TokenHolder] = {
      ZIO.fromCompletableFuture(
        authAPI.requestToken(s"${configuration.domain}/api/v2/").executeAsync()
      )
    }

    def create(configuration: Configuration): TaskLayer[Client] = {
      ZLayer {
        for {
          _              <- ZIO.logDebug("Constructing Auth0 AuthAPI to retrieve Access Tokens")
          authApi         = createAuthApi(configuration)
          _              <- ZIO.logDebug("Attempting to Authenticate to the Auth0 Management API")
          tokenHolder    <- getAccessToken(configuration, authApi)
          tokenHolderRef <- Ref.make(tokenHolder)
          _              <- ZIO.logDebug("Authentication Successful...Creating Management API")
          managementApi   = new ManagementAPI(configuration.domain, tokenHolder.getAccessToken)
          semaphore      <- Semaphore.make(1)
        } yield new Client(configuration, semaphore, tokenHolderRef, authApi, managementApi)
      }
    }
  }
}