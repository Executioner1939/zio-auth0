package io.bitlevel.zio.auth0.modules.users.domain

import com.auth0.json.mgmt.{PermissionSource => JPermissionSource}

final case class PermissionSource(source_type: String,
                                  source_name: String,
                                  source_id: String)

object PermissionSource {
  implicit class PermissionSourceOps(underlying: JPermissionSource) {
    def convert: PermissionSource = {
      PermissionSource(
        source_type = underlying.getType,
        source_name = underlying.getName,
        source_id = underlying.getId
      )
    }
  }
}
