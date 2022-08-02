package io.bitlevel.zio.auth0.modules.connections.domain

import io.bitlevel.zio.auth0.modules.domain.filters.{Fields, Page}

case class ConnectionFilter(strategy: Option[String],
                            name: Option[String],
                            includeTotals: Option[Boolean],
                            page: Option[Page],
                            fields: Option[Fields])
