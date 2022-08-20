package io.bitlevel.zio.auth0.modules.users

import com.auth0.client.mgmt.filter.{FieldsFilter => JFieldsFilter, UserFilter => JUserFilter}
import com.auth0.json.mgmt.organizations.OrganizationsPage
import com.auth0.json.mgmt.users.{User => JUser}
import io.bitlevel.zio.auth0.core.Client
import io.bitlevel.zio.auth0.core.domain.Configuration
import io.bitlevel.zio.auth0.modules.domain.LogEvent._
import io.bitlevel.zio.auth0.modules.domain.filters.{FieldsFilter, LogEventFilter, PageFilter}
import io.bitlevel.zio.auth0.modules.domain.LogEvent
import io.bitlevel.zio.auth0.modules.implicits.JavaConversions._
import io.bitlevel.zio.auth0.modules.roles.domain.Role
import io.bitlevel.zio.auth0.modules.roles.domain.Role._
import io.bitlevel.zio.auth0.modules.users.domain.Identity._
import io.bitlevel.zio.auth0.modules.users.domain.Permission._
import io.bitlevel.zio.auth0.modules.users.domain.User._
import io.bitlevel.zio.auth0.modules.users.domain._
import zio.{Task, UIO, URIO, URLayer, ZIO, ZLayer}

import scala.jdk.CollectionConverters._

/**
 * Class that provides an implementation of the Users methods of the Management API.
 *
 * @see https://auth0.com/docs/api/management/v2#!/Users
 * @see https://auth0.com/docs/api/management/v2#!/Users_By_Email
 * @param client the underlying HTTP Client
 */
final case class UsersService(client: Client) {

  /**
   * Create a User.
   * A token with scope create:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/post_users
   * @param user the user data to set
   * @return a Request to execute.
   */
  def create(user: User.Create): Task[User] = client
    .execute(() => client.management.users().create(user.toJava))
    .map(_.toScala)

  /**
   * Update an existing User.
   * A token with scope update:users is needed.
   * If you're updating app_metadata you'll also need update:users_app_metadata scope.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/patch_users_by_id
   * @param userId the user id
   * @param user   the user data to set
   * @return a Request to execute.
   */
  def update(userId: String, user: User.Update): Task[User] = client
    .execute(() => client.management.users().update(userId, user.toJava))
    .map(_.toScala)

  /**
   * Request all the Users.
   * A token with scope read:users is needed.
   * If you want the identities.access_token property to be included, you will also need the scope read:user_idp_tokens.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/get_users
   * @param filters the filter to use. Can be null.
   * @return a Request to execute.
   */
  def list(filters: Option[UserFilter]): Task[List[User]] = {
    val params = filters.fold(new JUserFilter())(_.toJava)

    client
      .execute(() => client.management.users().list(params))
      .map(_.getItems.asScala.map(_.toScala).toList)
  }

  /**
   * Request all the Events Log for a given User.
   * A token with scope read:logs is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/get_logs_by_user
   * @param userId  the id of the user to retrieve.
   * @param filters the filter to use.
   * @return a Request to execute.
   */
  def getLogEvents(userId: String, filters: LogEventFilter): Task[List[LogEvent]] = client
    .execute(() => client.management.users().getLogEvents(userId, filters.toJava))
    .map(_.getItems.asScala.map(_.toScala).toList)

  /**
   * Request all the Users that match a given email.
   * A token with scope read:users is needed.
   * If you want the identities.access_token property to be included, you will also need the scope read:user_idp_tokens.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users_By_Email/get_users_by_email
   * @param emailAddress the email of the users to look up.
   * @param filter       the filter to use. Can be null.
   * @return a Request to execute.
   */
  def listByEmail(emailAddress: String, filter: FieldsFilter): Task[List[User]] = {
    val params = filter.fields.fold(new JFieldsFilter()) { fields =>
      new JFieldsFilter().withFields(fields.fields, fields.includeFields)
    }

    client
      .execute(() => client.management.users().listByEmail(emailAddress, params))
      .map(_.asScala.map(_.toScala).toList)
  }

  /**
   * Request a User.
   * A token with scope read:users is needed.
   * If you want the identities.access_token property to be included, you will also need the scope read:user_idp_tokens.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/get_users_by_id
   * @param userId  the id of the user to retrieve.
   * @param filters the filter to use. Can be null.
   * @return a Request to execute.
   */
  def getById(userId: String, filters: Option[UserFilter]): Task[User] = {
    client
      .execute(() => client.management.users().get(userId, filters.fold(new JUserFilter())(_.toJava)))
      .map(_.toScala)
  }


  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  // Permissions
  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //

  /**
   * Get the permissions associated to the user.
   * A token with read:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/get_permissions
   * @param userId the role id
   * @param filter an optional pagination filter
   * @return a Request to execute
   */
  def listPermissions(userId: String, pageFilter: PageFilter): Task[List[Permission]] = client
    .execute(() => client.management.users().listPermissions(userId, pageFilter.toJava))
    .map(_.getItems.asScala.map(_.toScala).toList)

  /**
   * Assign permissions to a user.
   * A token with update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/post_permissions
   * @param userId      the user id
   * @param permissions a list of permission objects to assign to the user
   * @return a Request to execute
   */
  def addPermissions(userId: String, permissions: List[Permission.Create]): Task[Unit] = {
    val perms = permissions.map(_.toJava)

    client
      .execute(() => client.management.users().addPermissions(userId, perms.asJava))
      .unit
  }

  /**
   * Remove permissions from a user.
   * A token with update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/delete_permissions
   * @param userId      the user id
   * @param permissions a list of permission objects to remove from the user
   * @return a Request to execute
   */
  def removePermissions(userId: String, permissions: List[Permission]): Task[Unit] = {
    val perms = permissions.map(_.toJava)

    client
      .execute(() => client.management.users().removePermissions(userId, perms.asJava))
      .unit
  }


  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  // Roles
  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //

  /**
   * Get the roles associated with a user.
   * A token with read:users and read:roles is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/get_user_roles
   * @param userId     the role id
   * @param pageFilter an optional pagination filter
   * @return a Request to execute
   */
  def listRoles(userId: String, pageFilter: PageFilter): Task[List[Role]] = {
    client
      .execute(() => client.management.users().listRoles(userId, pageFilter.toJava))
      .map(_.getItems.asScala.map(_.convert).toList)
  }

  /**
   * Assign roles to a user.
   * A token with update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/post_user_roles
   * @param userId   the user id
   * @param rolesIds a list of role ids to assign to the user
   * @return a Request to execute
   */
  def addRoles(userId: String, rolesIds: List[String]): Task[Unit] = {
    client
      .execute(() => client.management.users().addRoles(userId, rolesIds.asJava))
      .unit
  }

  /**
   * Remove roles from a user.
   * A token with update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/delete_user_roles
   * @param userId  the user id
   * @param roleIds a list of role ids to remove from the user
   * @return a Request to execute
   */
  def removeRoles(userId: String, roleIds: List[String]): Task[Unit] = {
    client
      .execute(() => client.management.users().removeRoles(userId, roleIds.asJava))
      .unit
  }

  /**
   * Delete an existing User.
   * A token with scope delete:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/delete_users_by_id
   * @param userId the user id
   * @return a Request to execute.
   */
  def delete(userId: String): Task[Unit] = {
    client
      .execute(() => client.management.users().delete(userId))
      .unit
  }

  /**
   * Delete an existing User's Multifactor Provider.
   * A token with scope update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/delete_multifactor_by_provider
   * @param userId   the user id
   * @param provider the multifactor provider
   * @return a Request to execute.
   */
  def deleteMultiFactorProvider(userId: String, provider: String): Task[Unit] = {
    client
      .execute(() => client.management.users().deleteMultifactorProvider(userId, provider))
      .unit
  }

  /**
   * Rotates a User's Guardian recovery code.
   * A token with scope update:users is needed.
   *
   * @param userId the user id
   * @return a Request to execute.
   * @see <a href="https://auth0.com/docs/api/management/v2#!/Users/post_recovery_code_regeneration">Management API2 docs</a>
   */
  def rotateRecoveryCode(userId: String): Task[String] = {
    client
      .execute(() => client.management.users().rotateRecoveryCode(userId))
      .map(_.getCode)
  }

  /**
   * Get the organizations a user belongs to.
   * A token with {@code read:users} and {@code read:organizations} is required.
   *
   * @param userId the user ID
   * @param filter an optional pagination filter
   * @return a Request to execute
   * @see <a href="https://auth0.com/docs/api/management/v2#!/Users/get_organizations">https://auth0.com/docs/api/management/v2#!/Users/get_organizations</a>
   */
  def getOrganisation(userId: String, pageFilter: PageFilter): Task[OrganizationsPage] = {
    client
      .execute(() => client.management.users().getOrganizations(userId, pageFilter.toJava))
  }

  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  // Identity and Enrollments
  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //

  /**
   * A token with scope update:current_user_identities is needed.
   * It only works for the user the access token represents.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/post_identities
   * @param primaryUserId    the primary identity's user id associated with the access token this client was configured with.
   * @param secondaryIdToken the user ID token representing the identity to link with the current user
   * @return a Request to execute.
   */
  def linkIdentity(primaryUserId: String, secondaryIdToken: String): Task[List[Identity]] = {
    client
      .execute(() => client.management.users().linkIdentity(primaryUserId, secondaryIdToken))
      .map(_.asScala.map(_.convert).toList)
  }

  /**
   * Links two User's Identities.
   * A token with scope update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/post_identities
   * @param primaryUserId   the primary identity's user id
   * @param secondaryUserId the secondary identity's user id
   * @param provider        the provider name of the secondary identity.
   * @param connectionId    the connection id of the secondary account being linked, useful if the provider is 'auth0' and you have several connections. Can be null.
   * @return a Request to execute.
   */
  def linkIdentity(primaryUserId: String, secondaryUserId: String, provider: String, connectionId: String): Task[List[Identity]] = {
    client
      .execute(() => client.management.users().linkIdentity(primaryUserId, secondaryUserId, provider, connectionId))
      .map(_.asScala.map(_.convert).toList)
  }

  /**
   * Un-links two User's Identities.
   *
   * @note the `update:users` is required.
   * @see https://auth0.com/docs/api/management/v2#!/Users/delete_provider_by_user_id
   * @param primaryUserId   the primary identity's user id
   * @param secondaryUserId the secondary identity's user id
   * @param provider        the provider name of the secondary identity.
   * @return a Request to execute.
   */
  def unlinkIdentity(primaryUserId: String, secondaryUserId: String, provider: String): Task[List[Identity]] = {
    client
      .execute(() => client.management.users().unlinkIdentity(primaryUserId, secondaryUserId, provider))
      .map(_.asScala.map(_.convert).toList)
  }

  /**
   * Retrieve the first confirmed enrollment, or a pending enrollment if none are confirmed.
   * A token with scope read:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Users/get_enrollments
   * @param userId the id of the user to retrieve.
   * @return a Request to execute.
   */
  def getEnrollments(userId: String): Task[List[Enrollment]] = {
    client
      .execute(() => client.management.users().getEnrollments(userId))
      .map(_.asScala.map(Enrollment.fromJava).toList)
  }
}

object UsersService {
  val layer: URLayer[Client, UsersService] = {
    ZLayer(ZIO.service[Client].map(UsersService(_)))
  }
}