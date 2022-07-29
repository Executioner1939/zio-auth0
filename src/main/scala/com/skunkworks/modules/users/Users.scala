package com.skunkworks.modules.users

import com.auth0.client.mgmt.filter.{UserFilter, PageFilter => JPageFilter, FieldsFilter => JFieldsFilter}
import com.auth0.json.mgmt.users.{UsersPage, User => JUser}
import com.auth0.json.mgmt.{Permission => JPermission}
import com.skunkworks.core.Client
import com.skunkworks.modules.domain.{FieldsFilter, PageFilter}
import com.skunkworks.modules.domain.PageFilter._
import com.skunkworks.modules.users.domain.UserFilters._
import com.skunkworks.modules.users.domain.{Permission, User, UserFilters}
import zio.Task

import scala.jdk.CollectionConverters._

final case class Users(client: Client) {
  def list(filters: Option[UserFilters]): Task[UsersPage] = {
    val params = filters.fold(new UserFilter()) { f =>
      new UserFilter()
        .ifSome(f.includeTotals)(_.withTotals(_))
        .ifSome(f.searchEngineVersion)(_.withSearchEngine(_))
        .ifSome(f.sort)(_.withSort(_))
        .ifSome(f.query)(_.withQuery(_))
        .ifSome(f.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
        .ifSome(f.fields)((a, b) => a.withFields(b.fields, b.includeFields))
    }

    client.execute(
      () => client.management.users().list(params)
    )
  }

  def listByEmail(emailAddress: String, filter: FieldsFilter) = {
    val params = filter.fields.fold(new JFieldsFilter()) { fields =>
      new JFieldsFilter().withFields(fields.fields, fields.includeFields)
    }

    client.execute(
      () => client.management.users().listByEmail(emailAddress, params)
    )
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

    client.execute(
      () => client.management.users().create(body)
    )
  }

  // TODO: Make Unit
  def delete(userId: String): Task[Void] = {
    client.execute(
      () => client.management.users().delete(userId)
    )
  }

  // TODO: Case Class for Enrollments
  def getEnrollments(userId: String) = {
    client.execute(
      () => client.management.users().getEnrollments(userId)
    ).map(_.asScala.toList)
  }

  def addPermissions(userId: String, permissions: List[Permission.Create]): Task[Void] = {
    val perms = permissions.map { p =>
      val item = new JPermission()
      item.setName(p.permission_name)
      item.setDescription(p.description)
      item.setResourceServerId(p.resource_server_identifier)
      item.setResourceServerName(p.resource_server_name)
      item
    }


    client.execute(
      () => client.management.users().addPermissions(userId, perms.asJava)
    )
  }

  def addRoles(userId: String, roles: List[String]) = {
    client.execute(
      () => client.management.users().addRoles(userId, roles.asJava)
    )
  }

  def deleteMultiFactorProvider(userId: String, provider: String) = {
    client.execute(
      () => client.management.users().deleteMultifactorProvider(userId, provider)
    )
  }



  def getById(userId: String) = {
    client.execute(
      () => client.management.users().get(userId, new UserFilter())
    )
  }

  def getById(userId: String, filters: UserFilters) = {
    val params = new UserFilter()
      .ifSome(filters.includeTotals)(_.withTotals(_))
      .ifSome(filters.searchEngineVersion)(_.withSearchEngine(_))
      .ifSome(filters.sort)(_.withSort(_))
      .ifSome(filters.query)(_.withQuery(_))
      .ifSome(filters.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
      .ifSome(filters.fields)((a, b) => a.withFields(b.fields, b.includeFields))

    client.execute(
      () => client.management.users().get(userId, params)
    )
  }

  def getOrganisation(userId: String, pageFilter: PageFilter) = {
    val params = new JPageFilter()
      .ifSome(pageFilter.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
      .ifSome(pageFilter.from)(_.withFrom(_))
      .ifSome(pageFilter.take)(_.withTake(_))
      .ifSome(pageFilter.totals)(_.withTotals(_))

    client.execute(
      () => client.management.users().getOrganizations(userId, params)
    )
  }

  def linkIdentity(primaryUserId: String, secondaryIdToken: String) = {
    client.execute(
      () => client.management.users().linkIdentity(primaryUserId, secondaryIdToken)
    )
  }

  def linkIdentity(primaryUserId: String, secondaryIdToken: String, provider: String, connectionId: String) = {
    client.execute(
      () => client.management.users().linkIdentity(primaryUserId, secondaryIdToken, provider, connectionId)
    )
  }





  client.management.users().listByEmail()
}
