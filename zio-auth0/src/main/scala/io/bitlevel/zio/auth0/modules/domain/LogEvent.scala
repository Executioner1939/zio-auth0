package io.bitlevel.zio.auth0.modules.domain

import com.auth0.json.mgmt.logevents.{LogEvent => JLogEvent}

import java.time.{LocalDateTime, ZoneId}
import scala.jdk.CollectionConverters._

final case class LogEvent(_id: String,
                          log_id: String,
                          date: LocalDateTime,
                          `type`: String,
                          clientId: String,
                          client_name: String,
                          ip: String,
                          user_id: String,
                          user_name: String,
                          details: Map[String, AnyRef])

object LogEvent {
  implicit class LogEventOps(underlying: JLogEvent) {
    def toScala: LogEvent = {
      LogEvent(
        _id = underlying.getId,
        log_id = underlying.getLogId,
        date = underlying.getDate.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime,
        `type` = underlying.getType,
        clientId = underlying.getClientId,
        client_name = underlying.getClientName,
        ip = underlying.getIP,
        user_id = underlying.getUserId,
        user_name = underlying.getUserName,
        details = underlying.getDetails.asScala.toMap
      )
    }
  }
}
