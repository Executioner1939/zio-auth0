package com.skunkworks.modules.roles

import com.skunkworks.core.Client
import com.skunkworks.modules.domain.PageFilter
import com.skunkworks.modules.implicits.JavaConversions._
import com.skunkworks.modules.roles.domain.Role._
import com.skunkworks.modules.roles.domain.{Role, RolesFilter}
import com.skunkworks.modules.users.domain.Permission
import com.skunkworks.modules.users.domain.Permission._
import zio.{Task, ZIO}

import scala.jdk.CollectionConverters._

case class RoleService(client: Client) {

  /**
   * Create a Role.
   * A token with scope create:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/post_roles
   *
   * @param role the role data to set
   * @return a Request to execute.
   */
  def create(role: Role.Update): Task[Role] = {
    client
      .execute(() => client.management.roles().create(role.toJava))
      .map(_.convert)
  }

  /**
   * Request all Roles created by this tenant that can be assigned to a given user or user group.
   * A token with read:roles is needed
   * See https://auth0.com/docs/api/management/v2#!/Roles/get_roles
   *
   * @param filter optional filtering and pagination criteria
   * @return a Request to execute
   */
  def list(filter: RolesFilter): Task[List[Role]] = {
    client
      .execute(() => client.management.roles().list(filter.toJava))
      .map(_.getItems.asScala.map(_.convert).toList)
  }

  /**
   * Get a single role created by this tenant that can be assigned to a given user or user group.
   * A token with scope read:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/get_roles_by_id
   *
   * @param roleId the id of the user to retrieve.
   * @return a Request to execute.
   */
  def get(roleId: String): Task[Role] = {
    client
      .execute(() => client.management.roles().get(roleId))
      .map(_.convert)
  }

  /**
   * Get the permissions associated to the role.
   * A token with read:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/get_permissions
   *
   * @param roleId     the role id
   * @param pageFilter an optional pagination filter
   * @return a Request to execute
   */
  def listPermissions(roleId: String, pageFilter: PageFilter): Task[List[Permission]] = {
    client
      .execute(() => client.management.roles().listPermissions(roleId, pageFilter.toJava))
      .map(_.getItems.asScala.map(_.convert).toList)
  }

  /**
   * Update an existing Role.
   * A token with scope update:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/patch_roles_by_id
   *
   * @param roleId the role id
   * @param role   the role data to set. It can't include id.
   * @return a Request to execute.
   */
  def update(roleId: String, role: Role.Update): Task[Role] = {
    client
      .execute(() => client.management.roles().update(roleId, role.toJava))
      .map(_.convert)
  }

  /**
   * Delete an existing Role.
   * A token with scope delete:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/delete_roles_by_id
   *
   * @param roleId The id of the role to delete.
   * @return a Request to execute.
   */
  def delete(roleId: String): Task[Unit] = {
    client
      .execute(() => client.management.roles().delete(roleId))
      .unit
  }

  /**
   * Associate permissions with a role. Only the `permission_name` and
   * `resource_server_identifier` Permission attributes should be specified.
   * A token with update:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/post_role_permission_assignment
   *
   * @param roleId      the role id
   * @param permissions a list of permission objects to associate to the role
   * @return a Request to execute
   */
  def addPermissions(roleId: String, permissions: List[Permission.Create]): Task[Unit] = {
    val perms = permissions.map(_.toJPermission).asJava

    client
      .execute(() => client.management.roles().addPermissions(roleId, perms))
      .unit
  }

  /**
   * Un-associate permissions from a role.
   * A token with update:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/delete_role_permission_assignment
   *
   * @param roleId      the role id
   * @param permissions a list of permission objects to un-associate from the role
   * @return a Request to execute
   */
  def removePermissions(roleId: String, permissions: List[Permission]): Task[Unit] = {
    val perms = permissions.map(_.toJPermission).asJava

    client
      .execute(() => client.management.roles().removePermissions(roleId, perms))
      .unit
  }

  /**
   * Assign users to a role.
   * A token with update:roles is needed.
   * See https://auth0.com/docs/api/management/v2#!/Roles/post_role_users
   *
   * @param roleId  the role id
   * @param userIds a list of user ids to assign to the role
   * @return a Request to execute.
   */
  def assign(roleId: String, userIds: List[String]): ZIO[Any, Throwable, Unit] = {
    client
      .execute(() => client.management.roles().assignUsers(roleId, userIds.asJava))
      .unit
  }
}