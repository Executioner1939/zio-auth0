package io.bitlevel.zio.auth0.modules.roles.domain

import io.bitlevel.zio.auth0.modules.domain.filters.Page

final case class RolesFilter(name_filter: Option[String],
                             page: Option[Page],
                             includeTotals: Option[Boolean])
