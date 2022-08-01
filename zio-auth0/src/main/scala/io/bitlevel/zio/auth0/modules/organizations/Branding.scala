package io.bitlevel.zio.auth0.modules.organizations

import com.auth0.json.mgmt.organizations.{Branding => JBranding}
import io.bitlevel.zio.auth0.modules.organizations.Colors._

import java.net.URL

final case class Branding(logo_url: Option[URL],
                          colors: Option[Colors])

object Branding {
  implicit class BrandingOps0(underlying: JBranding) {
    def toScala: Branding = {
      Branding(
        logo_url = Some(underlying.getLogoUrl).map(new URL(_)),
        colors = Some(underlying.getColors.toScala)
      )
    }
  }

  implicit class BrandingOps1(underlying: Branding) {
    def toJava: JBranding = {
      val branding = new JBranding()
      branding.setColors(underlying.colors.map(_.toJava).orNull)
      branding.setLogoUrl(underlying.logo_url.map(_.toString).orNull)
      branding
    }
  }
}
