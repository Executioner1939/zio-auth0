package io.bitlevel.zio.auth0.modules.roles.domain

import com.auth0.json.mgmt.{Role => JRole}

final case class Role(id: String,
                      name: String,
                      description: Option[String])

object Role {
  final case class Create(name: String,
                          description: Option[String]) {
    def toJava: JRole = {
      val create = new JRole()
      create.setName(name)
      create.setDescription(description.orNull)
      create
    }
  }

  final case class Update(name: Option[String],
                          description: Option[String]) {
    def toJava: JRole = {
      val update = new JRole()
      update.setName(name.orNull)
      update.setDescription(description.orNull)
      update
    }
  }

  implicit class RoleOps(underlying: JRole) {
    def convert: Role = {
      Role(
        id = underlying.getId,
        name = underlying.getName,
        description = Option(underlying.getDescription)
      )
    }
  }
}
