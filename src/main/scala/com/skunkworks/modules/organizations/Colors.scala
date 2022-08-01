package com.skunkworks.modules.organizations

import com.auth0.json.mgmt.organizations.{Colors => JColors}

final case class Colors(primary: String,
                        page_background: String)

object Colors {
  implicit class ColorsOps0(underlying: JColors) {
    def toScala: Colors = {
      Colors(
        primary = underlying.getPrimary,
        page_background = underlying.getPageBackground
      )
    }
  }

  implicit class ColorOps1(underlying: Colors) {
    def toJava: JColors = {
      new JColors(underlying.primary, underlying.page_background)
    }
  }
}
