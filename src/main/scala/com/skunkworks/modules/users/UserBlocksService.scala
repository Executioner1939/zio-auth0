package com.skunkworks.modules.users

import com.skunkworks.core.Client
import com.skunkworks.modules.users.domain.UserBlocks
import com.skunkworks.modules.users.domain.UserBlocks._
import zio.Task

/**
 * Class that provides an implementation of the User Blocks methods of the Management API.
 *
 * @see https://auth0.com/docs/api/management/v2#!/User_Blocks
 * @param client the underlying HTTP Client
 */
case class UserBlocksService(client: Client) {

  /**
   * Request all the User Blocks. A token with scope read:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/User_Blocks/get_user_blocks_by_id
   * @param userId the user id.
   * @return a Request to execute.
   */
  def get(userId: String): Task[UserBlocks] = {
    client
      .execute(() => client.management.userBlocks().get(userId))
      .map(_.asScala)
  }

  /**
   * Delete any existing User Blocks. A token with scope update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/User_Blocks/delete_user_blocks_by_id
   * @param userId the user id.
   * @return a Request to execute.
   */
  def delete(userId: String): Task[Unit] = {
    client
      .execute(() => client.management.userBlocks().delete(userId))
      .unit
  }

  /**
   * Request all the User Blocks for a given identifier. A token with scope read:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/User_Blocks/get_user_blocks
   * @param identifier the identifier. Either a username, phone_number, or email.
   * @return a Request to execute.
   */
  def getByIdentifier(identifier: String): Task[UserBlocks] = {
    client
      .execute(() => client.management.userBlocks().getByIdentifier(identifier))
      .map(_.asScala)
  }

  /**
   * Delete any existing User Blocks for a given identifier. A token with scope update:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/User_Blocks/delete_user_blocks
   * @param identifier the identifier. Either a username, phone_number, or email.
   * @return a Request to execute.
   */
  def deleteByIdentifier(identifier: String): Task[Unit] = {
    client
      .execute(() => client.management.userBlocks().deleteByIdentifier(identifier))
      .unit
  }
}
