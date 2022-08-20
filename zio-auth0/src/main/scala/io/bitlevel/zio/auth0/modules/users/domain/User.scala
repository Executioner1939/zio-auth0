package io.bitlevel.zio.auth0.modules.users.domain

import com.auth0.json.mgmt.users.{User => JUser}

import java.net.URI
import java.time.{LocalDateTime, ZoneId}
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
                      app_metadata: Option[Map[String, Any]],
                      user_metadata: Option[Map[String, Any]],
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
  final case class Create(given_name: String,
                          family_name: String,
                          name: Option[String],
                          email: String,
                          verify_email: Boolean,
                          email_verified: Boolean,
                          phone_number: String,
                          phone_verified: Boolean,
                          username: Option[String],
                          connection: Option[String],
                          blocked: Boolean,
                          app_metadata: Option[Map[String, AnyRef]],
                          user_metadata: Option[Map[String, AnyRef]],
                          nickname: Option[String],
                          picture: Option[URI],
                          user_id: Option[String],
                          password: Option[Array[Char]]) {
    def toJava: JUser = {
      val user = new JUser()
      user.setGivenName(given_name)
      user.setFamilyName(family_name)
      user.setName(name.orNull)
      user.setNickname(nickname.orNull)
      user.setUsername(username.orNull)
      user.setEmail(email)
      user.setVerifyEmail(verify_email)
      user.setPhoneNumber(phone_number)
      user.setPhoneVerified(phone_verified)
      user.setConnection(connection.orNull)
      user.setBlocked(blocked)
      user.setAppMetadata(app_metadata.map(_.asJava).orNull)
      user.setUserMetadata(user_metadata.map(_.asJava).orNull)
      user.setPicture(picture.map(_.toString).orNull)
      user.setId(user_id.orNull)
      user.setPassword(password.orNull)
      user
    }
  }

  final case class Update(given_name: Option[String] = None,
                          family_name: Option[String] = None,
                          name: Option[String] = None,
                          email: Option[String] = None,
                          verify_email: Option[Boolean] = None,
                          email_verified: Option[Boolean] = None,
                          phone_number: Option[String] = None,
                          phone_verified: Option[Boolean] = None,
                          username: Option[String] = None,
                          connection: Option[String] = None,
                          blocked: Option[Boolean] = None,
                          app_metadata: Option[Map[String, AnyRef]] = None,
                          user_metadata: Option[Map[String, AnyRef]] = None,
                          nickname: Option[String] = None,
                          picture: Option[URI] = None) {
    def toJava: JUser = {
      val user = new JUser()
      user.setGivenName(given_name.orNull)
      user.setFamilyName(family_name.orNull)
      user.setName(name.orNull)
      user.setNickname(nickname.orNull)
      user.setUsername(username.orNull)
      user.setEmail(email.orNull)
      user.setVerifyEmail(verify_email.exists(_.booleanValue()))
      user.setPhoneNumber(phone_number.orNull)
      user.setPhoneVerified(phone_verified.exists(_.booleanValue()))
      user.setConnection(connection.orNull)
      user.setBlocked(blocked.exists(_.booleanValue()))
      user.setAppMetadata(app_metadata.map(_.asJava).orNull)
      user.setUserMetadata(user_metadata.map(_.asJava).orNull)
      user.setPicture(picture.map(_.toString).orNull)
      user
    }
  }

  implicit class UserOps(underlying: JUser) {
    def toScala: User = {
      User(
        given_name = underlying.getGivenName,
        family_name = underlying.getFamilyName,
        name = Some(underlying.getName),
        email = underlying.getEmail,
        email_verified = underlying.isEmailVerified,
        phone_number = underlying.getPhoneNumber,
        phone_verified = underlying.isPhoneVerified,
        username = Some(underlying.getUsername),
        blocked = underlying.isBlocked,
        app_metadata = Some(underlying.getAppMetadata).map(_.asScala.toMap),
        user_metadata = Some(underlying.getUserMetadata).map(_.asScala.toMap),
        nickname = Some(underlying.getNickname),
        picture = Some(underlying.getPicture).map(URI.create),
        user_id = underlying.getId,
        multifactor = Some(underlying.getMultifactor).map(_.asScala.toList),
        last_ip = Some(underlying.getLastIP),
        last_login = Some(underlying.getLastLogin).map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime),
        logins_count = Some(underlying.getLoginsCount),
        created_at = underlying.getCreatedAt.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime,
        updated_at = Some(underlying.getUpdatedAt).map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime)
      )
    }
  }
}

