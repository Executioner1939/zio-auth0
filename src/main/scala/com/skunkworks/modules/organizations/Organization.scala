package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Organization => JOrganization}
import com.skunkworks.modules.organizations.EnabledConnection._

import scala.jdk.CollectionConverters._

final case class Organization(id: String,
                              name: String,
                              display_name: String,
                              metadata: Map[String, AnyRef],
                              branding: Branding,
                              enabled_connections: List[EnabledConnection])

object Organization {
  implicit class OrganizationOps(underlying: JOrganization) {
    Organization(
      id                  = underlying.getId,
      name                = underlying.getName,
      display_name        = underlying.getDisplayName,
      metadata            = underlying.getMetadata.asScala.toMap,
      branding            = underlying.getBranding.toScala,
      enabled_connections = underlying.getEnabledConnections.asScala.map(_.toScala).toList
    )
  }
}
