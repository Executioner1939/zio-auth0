package com.skunkworks.modules.users.domain
import com.auth0.json.mgmt.users.{ProfileData => JProfileData}
final case class ProfileData(email: String,
                             email_verified: Boolean,
                             name: String,
                             username: String,
                             given_name: String,
                             phone_number: String,
                             phone_verified: String,
                             family_name: String)

object ProfileData {
  implicit class ProfileDataOps(underlying: JProfileData) {
    def convert: ProfileData = {
      ProfileData(
        email          = underlying.getEmail,
        email_verified = underlying.isEmailVerified,
        name           = underlying.getName,
        username       = underlying.getUsername,
        given_name     = underlying.getGivenName,
        phone_number   = underlying.getPhoneNumber,
        phone_verified = underlying.isPhoneVerified,
        family_name    = underlying.getFamilyName
      )
    }
  }
}