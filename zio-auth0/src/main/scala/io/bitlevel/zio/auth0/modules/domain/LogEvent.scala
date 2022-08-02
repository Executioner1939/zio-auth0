package io.bitlevel.zio.auth0.modules.domain

import com.auth0.json.mgmt.logevents.{LogEvent => JLogEvent}

import java.time.{LocalDateTime, ZoneId}
import scala.jdk.CollectionConverters._

final case class LogEvent(_id: Option[String] = None,
                          log_id: Option[String] = None,
                          date: Option[LocalDateTime] = None,
                          `type`: Option[String] = None,
                          clientId: Option[String] = None,
                          client_name: Option[String] = None,
                          ip: Option[String] = None,
                          user_id: Option[String] = None,
                          user_name: Option[String] = None,
                          details: Option[Map[String, AnyRef]] = None)

object LogEvent {
  implicit class LogEventOps(underlying: JLogEvent) {
    def toScala: LogEvent = {
      LogEvent(
        _id         = Option(underlying.getId),
        log_id      = Option(underlying.getLogId),
        date        = Option(underlying.getDate).map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime),
        `type`      = Option(underlying.getType),
        clientId    = Option(underlying.getClientId),
        client_name = Option(underlying.getClientName),
        ip          = Option(underlying.getIP),
        user_id     = Option(underlying.getUserId),
        user_name   = Option(underlying.getUserName),
        details     = Option(underlying.getDetails).map(_.asScala.toMap)
      )
    }
  }
}
