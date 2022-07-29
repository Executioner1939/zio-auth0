package com.skunkworks.modules.users.domain

import com.auth0.json.mgmt.users.{Identity => JIdentity}
import com.skunkworks.modules.users.domain.ProfileData._

final case class Identity(connection: String,
                          user_id: String,
                          provider: String,
                          isSocial: Boolean,
                          access_token: Option[String],
                          profileData: Option[ProfileData])

object Identity {
  implicit class IdentityOps(underlying: JIdentity) {
    def convert: Identity = {
      Identity(
        connection   = underlying.getConnection,
        user_id      = underlying.getUserId,
        provider     = underlying.getProvider,
        isSocial     = underlying.isSocial,
        access_token = Some(underlying.getAccessToken),
        profileData  = Some(underlying.getProfileData).map(_.convert)
      )
    }
  }
}