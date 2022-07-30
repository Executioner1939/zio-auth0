package com.skunkworks.modules.users.domain
import com.auth0.json.mgmt.{Permission => JPermission}
import com.skunkworks.modules.users.domain.PermissionSource._

import scala.jdk.CollectionConverters._

final case class Permission(permission_name: String,
                            description: String,
                            resource_server_identifier: String,
                            resource_server_name: String,
                            sources: List[PermissionSource]) {

  def toJPermission: JPermission = {
    val permission = new JPermission
    permission.setName(permission_name)
    permission.setDescription(description)
    permission.setResourceServerId(resource_server_identifier)
    permission.setResourceServerName(resource_server_name)
    permission
  }
}

object Permission {
  implicit class PermissionOps(underlying: JPermission) {
    def convert: Permission = {
      Permission(
        permission_name = underlying.getName,
        description = underlying.getDescription,
        resource_server_identifier = underlying.getResourceServerId,
        resource_server_name = underlying.getResourceServerName,
        sources = underlying.getSources.asScala.map(_.convert).toList,
      )
    }
  }

  final case class Create(permission_name: String,
                          description: String,
                          resource_server_identifier: String,
                          resource_server_name: String) {
    def toJPermission: JPermission = {
      val permission = new JPermission()
      permission.setName(permission_name)
      permission.setDescription(description)
      permission.setResourceServerId(resource_server_identifier)
      permission.setResourceServerName(resource_server_name)
      permission
    }
  }
}
