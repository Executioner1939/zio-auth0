package com.skunkworks.modules.users.domain

import java.net.URI
import java.time.{LocalDateTime, ZoneId}
import com.auth0.json.mgmt.users.{User => JUser}

import scala.jdk.CollectionConverters._
final case class User(given_name: String,
                      family_name: String,
                      name: Option[String],
                      email: String,
                      email_verified: Boolean,
                      phone_number: String,
                      phone_verified: Boolean,
                      username: Option[String],
                      blocked: Boolean,
                      app_metadata: Map[String, Any],
                      user_metadata: Map[String, Any],
                      nickname: Option[String],
                      picture: Option[URI],
                      user_id: String,
                      multifactor: Option[List[String]],
                      last_ip: Option[String],
                      last_login: Option[LocalDateTime],
                      logins_count: Option[Int],
                      created_at: LocalDateTime,
                      updated_at: Option[LocalDateTime])

object User {
  case class Create(given_name: String,
                    family_name: String,
                    name: Option[String],
                    email: String,
                    verify_email: Boolean,
                    email_verified: Boolean,
                    phone_number: String,
                    phone_verified: Boolean,
                    username: Option[String],
                    connection: String,
                    blocked: Boolean,
                    app_metadata: Option[Map[String, AnyRef]],
                    user_metadata: Option[Map[String, AnyRef]],
                    nickname: Option[String],
                    picture: Option[URI],
                    user_id: Option[String],
                    password: Option[Array[Char]])

  implicit class UserOps(underlying: JUser) {
    def convert: User = {
      User(
        given_name     = underlying.getGivenName,
        family_name    = underlying.getFamilyName,
        name           = Some(underlying.getName),
        email          = underlying.getEmail,
        email_verified = underlying.isEmailVerified,
        phone_number   = underlying.getPhoneNumber,
        phone_verified = underlying.isPhoneVerified,
        username       = Some(underlying.getUsername),
        blocked        = underlying.isBlocked,
        app_metadata   = Some(underlying.getAppMetadata).map(_.asScala),
        user_metadata  = Some(underlying.getUserMetadata),
        nickname       = Some(underlying.getNickname),
        picture        = Some(underlying.getPicture),
        user_id        = underlying.getId,
        multifactor    = Some(underlying.getMultifactor).map(_.asScala),
        last_ip        = Some(underlying.getLastIP),
        last_login     = Some(underlying.getLastLogin).map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime),
        logins_count   = Some(underlying.getLoginsCount),
        created_at     = underlying.getCreatedAt.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime,
        updated_at     = Some(underlying.getUpdatedAt).map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime)
      )
    }
  }
}

