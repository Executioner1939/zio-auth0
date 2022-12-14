package io.bitlevel.zio.auth0.modules.users

import io.bitlevel.zio.auth0.core.Client
import io.bitlevel.zio.auth0.core.domain.Configuration
import zio.test.{Live, ZIOSpec}
import zio.{Scope, ZIO, ZLayer}
import zio.System
import zio.config.ConfigDescriptor._
import zio.config._

abstract class SharedClientSpec extends ZIOSpec[Client] {
  private val CONFIG_AUTH0: ConfigDescriptor[Configuration] = (
    string("AUTH0_DOMAIN") zip
    string("AUTH0_CLIENT_ID") zip
    string("AUTH0_CLIENT_SECRET")
  ).to[Configuration]

  override def bootstrap: ZLayer[Scope, Any, Client] = {
    ZLayer.scoped(ZIO.withSystemScoped(System.SystemLive)) >>>
    ZConfig.fromSystemEnv(CONFIG_AUTH0) >>>
    Client.layer
  }
}