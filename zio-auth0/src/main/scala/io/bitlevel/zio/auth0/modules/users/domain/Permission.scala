package io.bitlevel.zio.auth0.modules.users.domain

import com.auth0.json.mgmt.{Permission => JPermission}
import io.bitlevel.zio.auth0.modules.users.domain.PermissionSource._

import scala.jdk.CollectionConverters._

final case class Permission(permission_name: String,
                            description: String,
                            resource_server_identifier: String,
                            resource_server_name: String,
                            sources: List[PermissionSource])

object Permission {
  final case class Create(permission_name: String,
                          description: String,
                          resource_server_identifier: String,
                          resource_server_name: String)


  implicit class PermissionOps0(underlying: JPermission) {
    def toScala: Permission = {
      Permission(
        permission_name = underlying.getName,
        description = underlying.getDescription,
        resource_server_identifier = underlying.getResourceServerId,
        resource_server_name = underlying.getResourceServerName,
        sources = underlying.getSources.asScala.map(_.convert).toList,
      )
    }
  }

  implicit class PermissionOps1(underlying: Permission.Create) {
    def toJava: JPermission = {
      val permission = new JPermission()
      permission.setName(underlying.permission_name)
      permission.setDescription(underlying.description)
      permission.setResourceServerId(underlying.resource_server_identifier)
      permission.setResourceServerName(underlying.resource_server_name)
      permission
    }
  }

  implicit class PermissionOps2(underlying: Permission) {
    def toJava: JPermission = {
      val permission = new JPermission()
      permission.setName(underlying.permission_name)
      permission.setDescription(underlying.description)
      permission.setResourceServerId(underlying.resource_server_identifier)
      permission.setResourceServerName(underlying.resource_server_name)
      permission
    }
  }
}
