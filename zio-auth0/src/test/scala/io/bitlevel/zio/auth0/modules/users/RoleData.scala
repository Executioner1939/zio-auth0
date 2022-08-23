package io.bitlevel.zio.auth0.modules.users

import faker.Faker
import io.bitlevel.zio.auth0.modules.roles.domain.Role

object RoleData {
  val create: () => Role.Create = () => Role.Create(
    name = Faker.en_US.loremWord(),
    description = Some(Faker.en_US.loremParagraph(8))
  )
}