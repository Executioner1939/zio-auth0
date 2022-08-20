package io.bitlevel.zio.auth0.modules.users

import faker.Faker
import io.bitlevel.zio.auth0.modules.users.domain.User

object UserData {
  lazy val create: () => User.Create = () => User.Create(
    given_name     = Faker.default.firstName(),
    family_name    = Faker.default.lastName(),
    name           = Some(Faker.default.fullName()),
    email          = Faker.default.emailAddress(),
    verify_email   = false,
    email_verified = false,
    phone_number   = Faker.default.cellPhoneNumber(),
    phone_verified = false,
    username       = Some(Faker.default.userName()),
    connection     = None,
    blocked        = false,
    app_metadata   = None,
    user_metadata  = None,
    nickname       = None,
    picture        = None,
    user_id        = None,
    password       = Some(Faker.default.password().toCharArray)
  )
}
