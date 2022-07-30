package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Branding => JBranding}
import com.skunkworks.modules.organizations.Colors._

import java.net.URL

final case class Branding(logo_url: Option[URL],
                          colors: Option[Colors])

object Branding {
  implicit class BrandingOps(underlying: JBranding) {
    def toScala: Branding = {
      Branding(
        logo_url = Some(underlying.getLogoUrl).map(new URL(_)),
        colors = Some(underlying.getColors.asScala)
      )
    }
  }
}
