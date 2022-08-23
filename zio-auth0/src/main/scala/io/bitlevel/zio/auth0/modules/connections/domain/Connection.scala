package io.bitlevel.zio.auth0.modules.connections.domain

import com.auth0.json.mgmt.{Connection => JConnection}

import scala.jdk.CollectionConverters._

final case class Connection(name: String,
                            strategy: String,
                            display_name: Option[String],
                            options: Option[Map[String, AnyRef]],
                            enabled_clients: Option[List[String]],
                            realms: Option[List[String]],
                            metadata: Option[Map[String, String]])

object Connection {
  final case class Create(name: String,
                          strategy: String,
                          display_name: Option[String] = None,
                          options: Option[Map[String, AnyRef]] = None,
                          enabled_clients: Option[List[String]] = None,
                          realms: Option[List[String]] = None,
                          metadata: Option[Map[String, String]] = None)

  final case class Update(name: Option[String] = None,
                          strategy: Option[String] = None,
                          display_name: Option[String] = None,
                          options: Option[Map[String, AnyRef]] = None,
                          enabled_clients: Option[List[String]] = None,
                          realms: Option[List[String]] = None,
                          metadata: Option[Map[String, String]] = None)

  implicit class ConnectionOps0(underlying: JConnection) {
    def toScala: Connection = {
      Connection(
        name     = underlying.getName,
        strategy = underlying.getStrategy,
        display_name = Option(underlying.getDisplayName),
        enabled_clients = Option(underlying.getEnabledClients).map(_.asScala.toList),
        realms = Option(underlying.getRealms).map(_.asScala.toList),
        options = Option(underlying.getOptions).map(_.asScala.toMap),
        metadata = Option(underlying.getMetadata).map(_.asScala.toMap)
      )
    }
  }

  implicit class ConnectionOps1(underlying: Connection.Create) {
    def toJava: JConnection = {
      val connection = new JConnection(underlying.name, underlying.strategy)
      connection.setDisplayName(underlying.display_name.orNull)
      connection.setOptions(underlying.options.map(_.asJava).orNull)
      connection.setEnabledClients(underlying.enabled_clients.map(_.asJava).orNull)
      connection.setRealms(underlying.realms.map(_.asJava).orNull)
      connection.setMetadata(underlying.metadata.map(_.asJava).orNull)
      connection
    }
  }

  implicit class ConnectionOps2(underlying: Connection.Update) {
    def toJava: JConnection = {
      val connection = new JConnection(underlying.name.orNull, underlying.strategy.orNull)
      connection.setDisplayName(underlying.display_name.orNull)
      connection.setOptions(underlying.options.map(_.asJava).orNull)
      connection.setEnabledClients(underlying.enabled_clients.map(_.asJava).orNull)
      connection.setRealms(underlying.realms.map(_.asJava).orNull)
      connection.setMetadata(underlying.metadata.map(_.asJava).orNull)
      connection
    }
  }
}