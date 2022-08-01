package io.bitlevel.zio.auth0.modules.domain

import com.auth0.client.mgmt.filter.{PageFilter => JPageFilter}

final case class PageFilter(page: Option[Page],
                            totals: Option[Boolean],
                            from: Option[String],
                            take: Option[Int])

object PageFilter {
  implicit class MaybeModifyOps(underlying: JPageFilter) {
    def ifSome[X](a: Option[X])(f: (JPageFilter, X) => JPageFilter): JPageFilter = a.fold(underlying)(f(underlying, _))
  }
}