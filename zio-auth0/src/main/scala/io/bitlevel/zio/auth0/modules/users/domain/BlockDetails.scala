package io.bitlevel.zio.auth0.modules.users.domain

import com.auth0.json.mgmt.userblocks.{BlockDetails => JBlockDetails}

final case class BlockDetails(identifier: String,
                              ip: String)

object BlockDetails {
  implicit class BlockDetailsOps(underlying: JBlockDetails) {
    def asScala: BlockDetails = {
      BlockDetails(
        identifier = underlying.getIdentifier,
        ip = underlying.getIP
      )
    }
  }
}
