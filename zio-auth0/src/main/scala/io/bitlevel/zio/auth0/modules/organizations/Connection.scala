package io.bitlevel.zio.auth0.modules.organizations

import com.auth0.json.mgmt.organizations.{Connection => JConnection}

final case class Connection(name: String,
                            strategy: String)

object Connection {
  implicit class ConnectionOps0(underlying: JConnection) {
    def toScala: Connection = {
      Connection(
        name = underlying.getName,
        strategy = underlying.getStrategy
      )
    }
  }

  implicit class ConnectionOps1(underlying: Connection) {
    def toJava: JConnection = {
      val connection = new JConnection()
      connection.setName(underlying.name)
      connection.setStrategy(underlying.strategy)
      connection
    }
  }
}