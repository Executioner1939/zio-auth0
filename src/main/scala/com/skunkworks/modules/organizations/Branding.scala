package com.skunkworks.modules.organizations

import
import java.net.URL

final case class Branding(logo_url: Option[URL],
                          colors: Option[Colors])

object Branding {
  implicit class BrandingOps(underlying: B)
}
