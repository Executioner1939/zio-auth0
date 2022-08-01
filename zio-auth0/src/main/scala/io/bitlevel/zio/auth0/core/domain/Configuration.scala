package io.bitlevel.zio.auth0.core.domain

final case class Configuration(domain: String,
                               clientId: String,
                               clientSecret: String)
