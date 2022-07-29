package com.skunkworks.modules.users.domain

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
}
