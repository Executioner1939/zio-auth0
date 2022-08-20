package io.bitlevel.zio.auth0.modules.users

import faker.Faker
import io.bitlevel.zio.auth0.modules.users.domain.User

object UserData {
  val CONNECTION: String = "Username-Password-Authentication"

  lazy val create: () => User.Create = () => User.Create(
    given_name     = Faker.en_US.firstName(),
    family_name    = Faker.en_US.lastName(),
    name           = Some(Faker.en_US.fullName()),
    email          = Faker.en_US.emailAddress(),
    verify_email   = false,
    email_verified = false,
    phone_number   = None,
    phone_verified = None,
    username       = None,
    connection     = CONNECTION,
    blocked        = false,
    app_metadata   = None,
    user_metadata  = None,
    nickname       = None,
    picture        = None,
    user_id        = None,
    password       = Some(Faker.en_US.password().toCharArray)
  )
}
