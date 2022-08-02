package io.bitlevel.zio.auth0.modules.organizations

import com.auth0.json.mgmt.organizations.{Connection => JConnection}

final case class Connection(name: String,
                            strategy: Option[String] = None)

object Connection {
  implicit class ConnectionOps1(underlying: Connection) {
    def toJava: JConnection = {
      val connection = new JConnection()
      connection.setName(underlying.name.orNull)
      connection.setStrategy(underlying.strategy.orNull)
      connection
    }
  }
}
