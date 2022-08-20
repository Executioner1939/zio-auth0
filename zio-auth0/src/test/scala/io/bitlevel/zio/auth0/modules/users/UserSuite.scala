package io.bitlevel.zio.auth0.modules.users

import io.bitlevel.zio.auth0.core.Client
import io.bitlevel.zio.auth0.modules.users.domain.User
import zio.{test, _}
import zio.test._

object UserSuite extends SharedClientSpec {
  def spec: Spec[Client, Any] = {
    suite("User API")(
      test("create a new user") {
        for {
          users <- ZIO.service[UsersService]
          user  <- users.create(UserData.create())
        } yield assertTrue(user.name != null)
      },

      test("update a user") {
        for {
          users <- ZIO.service[UsersService]
          user0 <- users.create(UserData.create())
          user1 <- users.update(user0.user_id, User.Update(name = Some("Updated")))
        } yield assertTrue(user1.name.contains("Updated"))
      },

      test("list users") {
        for {
          users <- ZIO.service[UsersService]

          user0 <- users.create(UserData.create())
          user1 <- users.create(UserData.create())
          user2 <- users.create(UserData.create())
          user3 <- users.create(UserData.create())

          usersList <- users.list(None)
        } yield assertTrue(
          usersList.exists(_.name == user0.name) &&
          usersList.exists(_.name == user1.name) &&
          usersList.exists(_.name == user2.name) &&
          usersList.exists(_.name == user3.name)
        )
      }
    ).provideLayer(UsersService.layer)
  }
}