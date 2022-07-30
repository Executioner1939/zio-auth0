package com.skunkworks.modules.users.domain

import com.auth0.json.mgmt.userblocks.{UserBlocks => JUserBlocks}
import com.skunkworks.modules.users.domain.BlockDetails._

import scala.jdk.CollectionConverters._

final case class UserBlocks(blocked_for: List[BlockDetails])

object UserBlocks {
  implicit class UserBlocksOps(underlying: JUserBlocks) {
    def asScala: UserBlocks = {
      UserBlocks(
        blocked_for = underlying.getBlockedFor.asScala.map(_.asScala).toList
      )
    }
  }
}

