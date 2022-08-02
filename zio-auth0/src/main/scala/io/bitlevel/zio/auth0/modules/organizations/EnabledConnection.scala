package io.bitlevel.zio.auth0.modules.organizations

import com.auth0.json.mgmt.organizations.{EnabledConnection => JEnabledConnection}
import io.bitlevel.zio.auth0.modules.connections.domain.Connection
import io.bitlevel.zio.auth0.modules.connections.domain.Connection._

final case class EnabledConnection(connection: Option[Connection] = None,
                                   assign_membership_on_login: Option[Boolean] = None,
                                   connection_id: Option[String] = None)

object EnabledConnection {
  implicit class EnabledConnectionOps0(underlying: JEnabledConnection) {
    def toScala: EnabledConnection = {
      EnabledConnection(
        connection                 = Option(underlying.getConnection).map(_.toScala),
        assign_membership_on_login = Option(underlying.isAssignMembershipOnLogin),
        connection_id              = Option(underlying.getConnectionId)
      )
    }
  }

  implicit class EnabledConnectionOps1(underlying: EnabledConnection) {
    def toJava: JEnabledConnection = {
      val enabledConnection = new JEnabledConnection()
      enabledConnection.setConnectionId(underlying.connection_id.orNull)
      enabledConnection.setConnection(underlying.connection.map(_.toJava).orNull)
      enabledConnection.setAssignMembershipOnLogin(underlying.assign_membership_on_login.map(boolean2Boolean).orNull)
      enabledConnection
    }
  }
}
