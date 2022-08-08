package io.bitlevel.zio.auth0.modules.users

import io.bitlevel.zio.auth0.core.Client
import zio._
import zio.test._

object UserSuite extends SharedClientSpec {
  def spec: Spec[Scope with Client, Any] = {
    suite("User API")(

      test("create a new user") {
        for {
          users <- ZIO.service[UsersService]
          user  <- users.create(UserData.create)
        } yield assertTrue(user.getName != null)
      }

    ).provideLayer(UsersService.layer)
  }
}
