package com.skunkworks.modules.users.domain

import com.skunkworks.modules.domain.{Fields, Page}

final case class UserFilter(includeTotals: Option[Boolean] = None,
                            searchEngineVersion: Option[String] = None,
                            query: Option[String] = None,
                            sort: Option[String] = None,
                            page: Option[Page] = None,
                            fields: Option[Fields] = None)

object UserFilter {
  implicit class MaybeModifyOps[T](underlying: T) {
    def ifSome[X](a: Option[X])(f: (T, X) => T): T = a.fold(underlying)(f(underlying, _))
  }
}