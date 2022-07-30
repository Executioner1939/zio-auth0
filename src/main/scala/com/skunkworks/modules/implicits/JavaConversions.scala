package com.skunkworks.modules.implicits

import com.auth0.client.mgmt.filter.{LogEventFilter => JLogEventFilter, PageFilter => JPageFilter, RolesFilter => JRolesFilter, UserFilter => JUserFilter}
import com.skunkworks.modules.domain.{LogEventFilter, PageFilter}
import com.skunkworks.modules.roles.domain.RolesFilter
import com.skunkworks.modules.users.domain.UserFilter
import com.skunkworks.modules.users.domain.UserFilter.MaybeModifyOps

object JavaConversions {
  implicit class UserFilterOps(underlying: UserFilter) {
    def toJava: JUserFilter = {
      new JUserFilter()
        .ifSome(underlying.includeTotals)(_.withTotals(_))
        .ifSome(underlying.searchEngineVersion)(_.withSearchEngine(_))
        .ifSome(underlying.sort)(_.withSort(_))
        .ifSome(underlying.query)(_.withQuery(_))
        .ifSome(underlying.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
        .ifSome(underlying.fields)((a, b) => a.withFields(b.fields, b.includeFields))
    }
  }

  implicit class LogEventFilterOps(underlying: LogEventFilter) {
    def toJava: JLogEventFilter = {
      new JLogEventFilter()
        .ifSome(underlying.includeTotals)(_.withTotals(_))
        .ifSome(underlying.checkpoint)((a, b) => a.withCheckpoint(b.from, b.take))
        .ifSome(underlying.sort)(_.withSort(_))
        .ifSome(underlying.query)(_.withQuery(_))
        .ifSome(underlying.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
        .ifSome(underlying.fields)((a, b) => a.withFields(b.fields, b.includeFields))
    }
  }

  implicit class PageFilterOps(underlying: PageFilter) {
    def toJava: JPageFilter = {
      new JPageFilter()
        .ifSome(underlying.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
        .ifSome(underlying.from)(_.withFrom(_))
        .ifSome(underlying.take)(_.withTake(_))
        .ifSome(underlying.totals)(_.withTotals(_))
    }
  }

  implicit class RolesFilterOps(underlying: RolesFilter) {
    def toJava: JRolesFilter = {
      new JRolesFilter()
        .ifSome(underlying.name_filter)(_.withName(_))
        .ifSome(underlying.totals)(_.withTotals(_))
        .ifSome(underlying.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
    }
  }
}
