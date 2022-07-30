package com.skunkworks.modules.roles.domain

import com.skunkworks.modules.domain.Page

final case class RolesFilter(name_filter: Option[String],
                             page: Option[Page],
                             totals: Option[Boolean])
