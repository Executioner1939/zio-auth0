package io.bitlevel.zio.auth0.modules.connections

import com.auth0.client.mgmt.filter.{ConnectionFilter => JConnectionFilter}
import io.bitlevel.zio.auth0.core.Client
import io.bitlevel.zio.auth0.modules.connections.domain.Connection._
import io.bitlevel.zio.auth0.modules.connections.domain.{Connection, ConnectionFilter}
import io.bitlevel.zio.auth0.modules.implicits.JavaConversions._
import zio.Task

import scala.jdk.CollectionConverters._

/**
 * Class that provides an implementation of the Connections methods of the Management API.
 *
 * @see https://auth0.com/docs/api/management/v2#!/Connections
 * @param client the underlying HTTP Client
 */
case class ConnectionService(client: Client) {

  /**
   * Create a Connection. A token with scope create:connections is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Connections/post_connections
   * @param connection the connection data to set.
   * @return a Request to execute.
   */
  def create(connection: Connection.Create): Task[Connection] = client
    .execute(() => client.management.connections().create(connection.toJava))
    .map(_.toScala)


  /**
   * Delete an existing Connection. A token with scope delete:connections is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Connections/delete_connections_by_id
   * @param connectionId the connection id.
   * @return a Request to execute.
   */
  def delete(connectionId: String): Task[Unit] = client
    .execute(() => client.management.connections().delete(connectionId))
    .unit

  /**
   * Request all the ConnectionsEntity. A token with scope read:connections is needed.
   * See https://auth0.com/docs/api/management/v2#!/Connections/get_connections
   *
   * @param filter the filter to use. Can be null.
   * @return a Request to execute.
   */
  def listAll(filter: ConnectionFilter): Task[List[Connection]] = client
    .execute(() => client.management.connections().listAll(filter.toJava))
    .map(_.getItems.asScala.map(_.toScala).toList)

  /**
   * Request a Connection. A token with scope read:connections is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Connections/get_connections_by_id
   * @param connectionId the id of the connection to retrieve.
   * @param filter       the filter to use
   * @return a Request to execute.
   */
  def getById(connectionId: String, filter: Option[ConnectionFilter]): Task[Connection] = {
    val params = filter.fold(new JConnectionFilter())(_.toJava)

    client
      .execute(() => client.management.connections().get(connectionId, params))
      .map(_.toScala)
  }

  /**
   * Delete an existing User from the given Database Connection. A token with scope delete:users is needed.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Connections/delete_users_by_email
   * @param connectionId the connection id where the user is stored.
   * @param emailAddress the email of the user to delete.
   * @return a Request to execute.
   */
  def deleteUser(connectionId: String, emailAddress: String): Task[Unit] = client
    .execute(() => client.management.connections().deleteUser(connectionId, emailAddress))
    .unit


  /**
   * Update an existing Connection. A token with scope update:connections is needed. Note that if the 'options' value is present it will
   * override all the 'options' values that currently exist.
   *
   * @see https://auth0.com/docs/api/management/v2#!/Connections/patch_connections_by_id
   * @param connectionId the connection id.
   * @param connection   the connection data to set. It can't include name or strategy.
   * @return a Request to execute.
   */
  def update(connectionId: String, connection: Connection.Update): Task[Connection] = client
    .execute(() => client.management.connections().update(connectionId, connection.toJava))
    .map(_.toScala)
}
