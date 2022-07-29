package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Connection => JConnection}

final case class Connection(name: String,
                            strategy: String)

object Connection {
  implicit class ConnectionOps(underlying: JConnection) {
    def toScala: Connection = {
      Connection(
        name = underlying.getName,
        strategy = underlying.getStrategy
      )
    }
  }
}