package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{EnabledConnection => JEnabledConnection}
import com.skunkworks.modules.organizations.Connection._

final case class EnabledConnection(connection: Connection,
                                   assign_membership_on_login: Boolean,
                                   connection_id: String)

object EnabledConnection {
  implicit class EnabledConnectionOps(underlying: JEnabledConnection) {
    def toScala: EnabledConnection = {
      EnabledConnection(
        connection = underlying.getConnection.toScala,
        assign_membership_on_login = underlying.isAssignMembershipOnLogin,
        connection_id = underlying.getConnectionId
      )
    }
  }
}
