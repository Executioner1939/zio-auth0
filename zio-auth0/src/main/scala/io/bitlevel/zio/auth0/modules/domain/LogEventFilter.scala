package io.bitlevel.zio.auth0.modules.domain

final case class LogEventFilter(checkpoint: Option[Checkpoint],
                                includeTotals: Option[Boolean] = None,
                                query: Option[String] = None,
                                sort: Option[String] = None,
                                page: Option[Page] = None,
                                fields: Option[Fields] = None)