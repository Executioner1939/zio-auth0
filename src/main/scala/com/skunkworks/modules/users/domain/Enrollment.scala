package com.skunkworks.modules.users.domain

import com.auth0.json.mgmt.guardian.{Enrollment => JEnrollment}

import java.time.{LocalDateTime, ZoneId}

final case class Enrollment(id: String,
                            status: String,
                            name: String,
                            identifier: String,
                            phone_number: Option[String],
                            auth_method: Option[String],
                            enrolled_at: Option[LocalDateTime])

object Enrollment {
  def fromJava(enrollment: JEnrollment): Enrollment = {
    Enrollment(
      id           = enrollment.getId,
      status       = enrollment.getStatus,
      name         = enrollment.getStatus,
      identifier   = enrollment.getIdentifier,
      phone_number = Option(enrollment.getPhoneNumber),
      auth_method  = Option(enrollment.getAuthMethod),
      enrolled_at  = Option(enrollment.getEnrolledAt).map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime)
    )
  }
}
