package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Connection => JConnection}

final case class Connection(name: Option[String] = None,
                            strategy: Option[String] = None)

object Connection {
  implicit class ConnectionOps0(underlying: JConnection) {
    def toScala: Connection = {
      Connection(
        name = Option(underlying.getName),
        strategy = Option(underlying.getStrategy)
      )
    }
  }

  implicit class ConnectionOps1(underlying: Connection) {
    def toJava: JConnection = {
      val connection = new JConnection()
      connection.setName(underlying.name.orNull)
      connection.setStrategy(underlying.strategy.orNull)
      connection
    }
  }
}