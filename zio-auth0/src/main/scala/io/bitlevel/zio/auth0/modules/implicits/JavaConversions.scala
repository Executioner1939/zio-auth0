package io.bitlevel.zio.auth0.modules.implicits

import com.auth0.client.mgmt.filter.{ConnectionFilter => JConnectionFilter, LogEventFilter => JLogEventFilter, PageFilter => JPageFilter, RolesFilter => JRolesFilter, UserFilter => JUserFilter}
import io.bitlevel.zio.auth0.modules.connections.domain.ConnectionFilter
import io.bitlevel.zio.auth0.modules.domain.filters.{LogEventFilter, PageFilter}
import io.bitlevel.zio.auth0.modules.roles.domain.RolesFilter
import io.bitlevel.zio.auth0.modules.users.domain.UserFilter
import io.bitlevel.zio.auth0.modules.users.domain.UserFilter.MaybeModifyOps

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
        .ifSome(underlying.includeTotals)(_.withTotals(_))
    }
  }

  implicit class RolesFilterOps(underlying: RolesFilter) {
    def toJava: JRolesFilter = {
      new JRolesFilter()
        .ifSome(underlying.name_filter)(_.withName(_))
        .ifSome(underlying.includeTotals)(_.withTotals(_))
        .ifSome(underlying.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
    }
  }

  implicit class ConnectionFilterOps(underlying: ConnectionFilter) {
    def toJava: JConnectionFilter = {
      new JConnectionFilter()
        .ifSome(underlying.strategy)(_.withStrategy(_))
        .ifSome(underlying.name)(_.withName(_))
        .ifSome(underlying.includeTotals)(_.withTotals(_))
        .ifSome(underlying.page)((a, b) => a.withPage(b.pageNumber, b.amountPerPage))
        .ifSome(underlying.fields)((a, b) => a.withFields(b.fields, b.includeFields))
    }
  }
}
