package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Colors => JColors}

final case class Colors(primary: String,
                        page_background: String)

object Colors {
  implicit class Colors(underlying: JColors) {
    def asScala: Colors = {
      Colors(
        primary = underlying.getPrimary,
        page_background = underlying.getPageBackground
      )
    }
  }
}
