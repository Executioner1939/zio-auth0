package com.skunkworks.modules.roles.domain

import com.auth0.json.mgmt.{ Role => JRole }

final case class Role(id: String,
                      name: String,
                      description: Option[String])

object Role {
  implicit class RoleOps(underlying: JRole) {
    def convert: Role = {
      Role(
        id          = underlying.getId,
        name        = underlying.getName,
        description = Option(underlying.getDescription)
      )
    }
  }
}
