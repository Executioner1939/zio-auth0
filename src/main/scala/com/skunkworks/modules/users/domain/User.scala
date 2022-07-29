package com.skunkworks.modules.users.domain

import java.net.URI
import java.time.LocalDateTime

final case class User(given_name: String,
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
                      app_metadata: Map[String, Any],
                      user_metadata: Map[String, Any],
                      nickname: Option[String],
                      picture: Option[URI],
                      user_id: String,
                      password: Option[String],
                      multifactor: Option[List[String]],
                      last_ip: Option[String],
                      string: Option[LocalDateTime],
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
}

