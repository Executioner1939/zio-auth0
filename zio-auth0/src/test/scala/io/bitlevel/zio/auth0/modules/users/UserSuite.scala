package io.bitlevel.zio.auth0.modules.users

import io.bitlevel.zio.auth0.core.Client
import io.bitlevel.zio.auth0.modules.users.domain.User
import zio._
import zio.test._

object UserSuite extends SharedClientSpec {

  def before(): URIO[UserService, UserService] = {
    ZIO.service[UserService]
  }

  def after(service: UserService): Task[Unit] = {
    for {
      users <- service.list(filters = None)
      _     <- ZIO.foreachDiscard(users)(u => service.delete(u.user_id))
    } yield ()
  }

  def spec: Spec[Client, Any] = {
    suite("User API")(
      test("can create a new user") {
        for {
          users <- ZIO.service[UserService]
          user  <- users.create(UserData.create())
        } yield assertTrue(user.name != null)
      },

      test("can update an existing user") {
        for {
          users <- ZIO.service[UserService]
          user0 <- users.create(UserData.create())
          user1 <- users.update(user0.user_id, User.Update(name = Some("Updated")))
        } yield assertTrue(user1.name.contains("Updated"))
      },

      test("can list all users") {
        for {
          users <- ZIO.service[UserService]

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
      },

      test("can find a user by user id") {
        for {
          users <- ZIO.service[UserService]
          user0 <- users.create(UserData.create())
          user1 <- users.getById(user0.user_id, filters = None)
        } yield assertTrue(user0.user_id == user1.user_id)
      },

      test("can list user by email address") {
        for {
          users <- ZIO.service[UserService]
          user0 <- users.create(UserData.create())
          user1 <- users.listByEmail(user0.email, filters = None)
        } yield assertTrue(user1.exists(_.user_id == user0.user_id))
      }

    ) @@ TestAspect.aroundAllWith(before())(after(_).ignore)
  }.provideLayer(UserService.layer)
}