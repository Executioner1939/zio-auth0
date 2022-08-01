package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Organization => JOrganization}
import com.skunkworks.modules.organizations.Branding._
import com.skunkworks.modules.organizations.EnabledConnection._

import scala.jdk.CollectionConverters._

final case class Organization(id: Option[String] = None,
                              name: Option[String] = None,
                              display_name: Option[String] = None,
                              metadata: Option[Map[String, AnyRef]] = None,
                              branding: Option[Branding] = None,
                              enabled_connections: Option[List[EnabledConnection]] = None)

object Organization {
  implicit class OrganizationOps0(underlying: JOrganization) {
    def toScala: Organization = {
      Organization(
        id = Option(underlying.getId),
        name = Option(underlying.getName),
        display_name = Option(underlying.getDisplayName),
        metadata = Option(underlying.getMetadata).map(_.asScala.toMap),
        branding = Option(underlying.getBranding).map(_.toScala),
        enabled_connections = Option(underlying.getEnabledConnections).map(_.asScala.map(_.toScala).toList)
      )
    }
  }

  implicit class OrganizationOps1(underlying: Organization) {
    def toJava: JOrganization = {
      val organization = new JOrganization()
      organization.setName(underlying.name.orNull)
      organization.setDisplayName(underlying.display_name.orNull)
      organization.setMetadata(underlying.metadata.map(_.asJava).orNull)
      organization.setBranding(underlying.branding.map(_.toJava).orNull)
      organization.setEnabledConnections(underlying.enabled_connections.map(_.map(_.toJava).asJava).orNull)
      organization
    }
  }
}