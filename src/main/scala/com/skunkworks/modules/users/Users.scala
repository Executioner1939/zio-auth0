package com.skunkworks.modules.users

import com.auth0.client.mgmt.filter.{FieldsFilter => JFieldsFilter, PageFilter => JPageFilter, UserFilter => JUserFilter}
import com.auth0.json.mgmt.users.{User => JUser}
import com.skunkworks.core.Client
import com.skunkworks.modules.domain.PageFilter._
import com.skunkworks.modules.domain.{FieldsFilter, PageFilter}
import com.skunkworks.modules.users.domain.Identity._
import com.skunkworks.modules.users.domain.Permission._
import com.skunkworks.modules.users.domain.User._
import com.skunkworks.modules.users.domain.UserFilter._
import com.skunkworks.modules.users.domain._
import com.skunkworks.modules.roles.domain.Role._
import zio.{Task, ZIO}

import scala.jdk.CollectionConverters._

final case class Users(client: Client) {

  private def toUserFilter(filter: UserFilter): JUserFilter = {
    new JUserFilter()
      .ifSome(filter.includeTotals)(_.withTotals(_))
      .ifSome(filter.searchEngineVersion)(_.withSearchEngine(_))
      .ifSome(filter.sort)(_.withSort(_))
      .ifSome(filter.query)(_.withQuery(_))
      .ifSome(filter.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
      .ifSome(filter.fields)((a, b) => a.withFields(b.fields, b.includeFields))
  }


  private def toPageFilter(filter: PageFilter): JPageFilter = {
    new JPageFilter()
      .ifSome(filter.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
      .ifSome(filter.from)(_.withFrom(_))
      .ifSome(filter.take)(_.withTake(_))
      .ifSome(filter.totals)(_.withTotals(_))
  }


  def create(user: User.Create): Task[JUser] = {
    val body = new JUser(user.connection)
    body.setGivenName(user.given_name)
    body.setFamilyName(user.family_name)
    body.setName(user.name.orNull)
    body.setNickname(user.nickname.orNull)
    body.setUsername(user.username.orNull)
    body.setEmail(user.email)
    body.setVerifyEmail(user.verify_email)
    body.setPhoneNumber(user.phone_number)
    body.setPhoneVerified(user.phone_verified)
    body.setConnection(user.connection)
    body.setBlocked(user.blocked)
    body.setAppMetadata(user.app_metadata.map(_.asJava).orNull)
    body.setUserMetadata(user.user_metadata.map(_.asJava).orNull)
    body.setPicture(user.picture.map(_.toString).orNull)
    body.setId(user.user_id.orNull)
    body.setPassword(user.password.orNull)

    client
      .execute(() => client.management.users().create(body))
  }

  def list(filters: Option[UserFilter]): Task[List[User]] = {
    val params = filters.fold(new JUserFilter())(toUserFilter)

    client
      .execute(() => client.management.users().list(params))
      .map(_.getItems.asScala.map(_.convert).toList)
  }

  def listByEmail(emailAddress: String, filter: FieldsFilter): Task[List[User]] = {
    val params = filter.fields.fold(new JFieldsFilter()) { fields =>
      new JFieldsFilter().withFields(fields.fields, fields.includeFields)
    }

    client
      .execute(() => client.management.users().listByEmail(emailAddress, params))
      .map(_.asScala.map(_.convert).toList)
  }

  def getById(userId: String, filters: Option[UserFilter]): Task[User] = {
    client
      .execute(() => client.management.users().get(userId, filters.fold(new JUserFilter())(toUserFilter)))
      .map(_.convert)
  }


  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  // Permissions
  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  def listPermissions(userId: String, pageFilter: PageFilter): Task[List[Permission]] = {
    client
      .execute(() => client.management.users().listPermissions(userId, toPageFilter(pageFilter)))
      .map(_.getItems.asScala.map(_.convert).toList)
  }

  def addPermissions(userId: String, permissions: List[Permission.Create]): Task[Unit] = {
    val perms = permissions.map(_.toJPermission).asJava

    client
      .execute(() => client.management.users().addPermissions(userId, perms))
      .unit
  }

  def removePermissions(userId: String, permissions: List[Permission]): Task[Unit] = {
    client
      .execute(() => client.management.users().removePermissions(userId, permissions.map(_.toJPermission).asJava))
      .unit
  }


  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  // Roles
  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  def listRoles(userId: String, pageFilter: PageFilter) = {
    client
      .execute(() => client.management.users().listRoles(userId, toPageFilter(pageFilter)))
      .map(_.getItems.asScala.map(_.convert).toList)
  }

  def addRoles(userId: String, roles: List[String]): Task[Unit] = {
    client
      .execute(() => client.management.users().addRoles(userId, roles.asJava))
      .unit
  }

  /**
   * Remove roles from a user.
   * A token with update:users is needed.
   * See https://auth0.com/docs/api/management/v2#!/Users/delete_user_roles
   *
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
   * See https://auth0.com/docs/api/management/v2#!/Users/delete_users_by_id
   *
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
   * See https://auth0.com/docs/api/management/v2#!/Users/delete_multifactor_by_provider
   *
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
  def getOrganisation(userId: String, pageFilter: PageFilter) = {
    client
      .execute(() => client.management.users().getOrganizations(userId, toPageFilter(pageFilter)))
  }



  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  // Identity and Enrollments
  // ************************************************************************************************************************************ //
  // ************************************************************************************************************************************ //
  def linkIdentity(primaryUserId: String, secondaryIdToken: String): Task[List[Identity]] = {
    client
      .execute(() => client.management.users().linkIdentity(primaryUserId, secondaryIdToken))
      .map(_.asScala.map(_.convert).toList)
  }

  def linkIdentity(primaryUserId: String, secondaryIdToken: String, provider: String, connectionId: String): Task[List[Identity]] = {
    client
      .execute(() => client.management.users().linkIdentity(primaryUserId, secondaryIdToken, provider, connectionId))
      .map(_.asScala.map(_.convert).toList)
  }

  def unlinkIdentity(primaryUserId: String, secondaryIdToken: String, provider: String): Task[List[Identity]] = {
    client
      .execute(() => client.management.users().unlinkIdentity(primaryUserId, secondaryIdToken, provider))
      .map(_.asScala.map(_.convert).toList)
  }

  def getEnrollments(userId: String): Task[List[Enrollment]] = {
    client
      .execute(() => client.management.users().getEnrollments(userId))
      .map(_.asScala.map(Enrollment.fromJava).toList)
  }
}
