package com.skunkworks.modules.tickets

import com.auth0.json.mgmt.EmailVerificationIdentity
import com.auth0.json.mgmt.tickets.{PasswordChangeTicket, EmailVerificationTicket}
import com.skunkworks.core.Client
import com.skunkworks.modules.tickets.domain.EmailVerificationTicketRequest
import zio.Task

case class TicketService(client: Client) {

  /**
   * Create an Email Verification Ticket. A token with scope create:user_tickets is needed.
   * See https://auth0.com/docs/api/management/v2#!/Tickets/post_email_verification
   *
   * @param userId                 the user ID of for whom the ticket should be created.
   * @param resultUrl              URL the user will be redirected to in the classic Universal Login experience once the ticket is used.
   * @param ttlSec                 Number of seconds for which the ticket is valid before expiration. If unspecified or set to 0, this
   *                               value defaults to 432000 seconds (5 days).
   * @param includeEmailInRedirect Whether to include the email address as part of the returnUrl in the reset_email (true), or not (false).
   * @return URL representing the ticket
   */
  def requestEmailVerification(userId: String,
                               resultUrl: String,
                               ttlSec: Option[Int],
                               includeEmailInRedirect: Option[Boolean]): Task[String] = {
    val params = new EmailVerificationTicket(userId)
    params.setResultUrl(resultUrl)
    ttlSec.foreach(i => params.setTTLSeconds(i.intValue()))
    includeEmailInRedirect.foreach(b => params.setIncludeEmailInRedirect(b.booleanValue()))

    client
      .execute(() => client.management.tickets().requestEmailVerification(params))
      .map(_.getTicket)
  }


  /**
   * Create an Email Verification Ticket. A token with scope create:user_tickets is needed.
   * See https://auth0.com/docs/api/management/v2#!/Tickets/post_email_verification
   *
   * @param userId                 the user ID of for whom the ticket should be created.
   * @param clientId               ID of the client. If provided for tenants using New Universal Login experience, the user will be prompted
   *                               to redirect to the default login route of the corresponding application once the ticket is used.
   * @param organizationId         the ID of the Organization. If provided, organization parameters will be made available to the email
   *                               template and organization branding will be applied to the prompt. In addition, the redirect link in the
   *                               prompt will include organization_id and organization_name query string parameters.
   * @param ttlSec                 Number of seconds for which the ticket is valid before expiration. If unspecified or set to 0, this
   *                               value defaults to 432000 seconds (5 days).
   * @param includeEmailInRedirect Whether to include the email address as part of the returnUrl in the reset_email (true), or not (false).
   * @return URL representing the ticket
   */
  def requestEmailVerification(userId: String,
                               clientId: Option[String],
                               organizationId: Option[String],
                               ttlSec: Option[Int],
                               includeEmailInRedirect: Option[Boolean]): Task[String] = {
    val params = new EmailVerificationTicket(userId)
    clientId.foreach(params.setClientId)
    organizationId.foreach(params.setOrganizationId)
    ttlSec.foreach(i => params.setTTLSeconds(i.intValue()))
    includeEmailInRedirect.foreach(b => params.setIncludeEmailInRedirect(b.booleanValue()))

    client
      .execute(() => client.management.tickets().requestEmailVerification(params))
      .map(_.getTicket)
  }


  /**
   * Create a Password Change Ticket. A token with scope create:user_tickets is needed.
   * See https://auth0.com/docs/api/management/v2#!/Tickets/post_password_change
   *
   * @param userId                 ID of for whom the ticket should be created.
   * @param clientId               ID of the client. If provided for tenants using New Universal Login experience, the user
   *                               will be prompted to redirect to the default login route of the corresponding application
   *                               once the ticket is used.
   * @param organizationId         the ID of the Organization. If provided, organization parameters will be made
   *                               available to the email template and organization branding will be applied to the
   *                               prompt. In addition, the redirect link in the prompt will include organization_id
   *                               and organization_name query string parameters.
   * @param ttlSec                 Number of seconds for which the ticket is valid before expiration. If unspecified or set to 0, this
   *                               value defaults to 432000 seconds (5 days).
   * @param markEmailAsVerified    Whether to set the email_verified attribute to true (true) or whether it should not be updated (false).
   * @param includeEmailInRedirect Whether to include the email address as part of the returnUrl in the reset_email (true), or not (false).
   * @return URL representing the ticket.
   */
  def requestPasswordChange(userId: String,
                            clientId: Option[String],
                            organizationId: Option[String],
                            ttlSec: Option[Int],
                            markEmailAsVerified: Option[Boolean],
                            includeEmailInRedirect: Option[Boolean]): Task[String] = {
    val params = new PasswordChangeTicket(userId)
    clientId.foreach(params.setClientId)
    organizationId.foreach(params.setOrganizationId)

    client
      .execute(() => client.management.tickets().requestPasswordChange(params))
      .map(_.getTicket)
  }

  /**
   * Create a Password Change Ticket. A token with scope create:user_tickets is needed.
   * See https://auth0.com/docs/api/management/v2#!/Tickets/post_password_change
   *
   * @param userId                 ID of for whom the ticket should be created.
   * @param resultUrl              URL the user will be redirected to in the classic Universal Login experience once the ticket is used.
   * @param ttlSec                 Number of seconds for which the ticket is valid before expiration. If unspecified or set to 0, this
   *                               value defaults to 432000 seconds (5 days).
   * @param markEmailAsVerified    Whether to set the email_verified attribute to true (true) or whether it should not be updated (false).
   * @param includeEmailInRedirect Whether to include the email address as part of the returnUrl in the reset_email (true), or not (false).
   * @return URL representing the ticket.
   */
  def requestPasswordChange(userId: String,
                            resultUrl: String,
                            ttlSec: Option[Int],
                            markEmailAsVerified: Option[Boolean],
                            includeEmailInRedirect: Option[Boolean]): Task[String] = {
    val params = new PasswordChangeTicket(userId)
    ttlSec.foreach(i => params.setTTLSeconds(i.intValue()))
    markEmailAsVerified.foreach(b => params.setMarkEmailAsVerified(b.booleanValue()))
    includeEmailInRedirect.foreach(b => params.setIncludeEmailInRedirect(b.booleanValue()))

    client
      .execute(() => client.management.tickets().requestPasswordChange(params))
      .map(_.getTicket)
  }

  /**
   * Create a Password Change Ticket. A token with scope create:user_tickets is needed.
   * See https://auth0.com/docs/api/management/v2#!/Tickets/post_password_change
   *
   * @param connectionId           ID of for whom the ticket should be created.
   * @param emailAddress           URL the user will be redirected to in the classic Universal Login experience once the ticket is used.
   * @param clientId               ID of the client. If provided for tenants using New Universal Login experience, the user
   *                               will be prompted to redirect to the default login route of the corresponding application
   *                               once the ticket is used.
   * @param organizationId         the ID of the Organization. If provided, organization parameters will be made
   *                               available to the email template and organization branding will be applied to the
   *                               prompt. In addition, the redirect link in the prompt will include organization_id
   *                               and organization_name query string parameters.
   * @param ttlSec                 Number of seconds for which the ticket is valid before expiration. If unspecified or set to 0, this
   *                               value defaults to 432000 seconds (5 days).
   * @param markEmailAsVerified    Whether to set the email_verified attribute to true (true) or whether it should not be updated (false).
   * @param includeEmailInRedirect Whether to include the email address as part of the returnUrl in the reset_email (true), or not (false).
   * @return URL representing the ticket.
   */
  def requestPasswordChange(connectionId: String,
                            emailAddress: String,
                            clientId: Option[String],
                            organizationId: Option[String],
                            ttlSec: Option[Int],
                            markEmailAsVerified: Option[Boolean],
                            includeEmailInRedirect: Option[Boolean]): Task[String] = {
    val params = new PasswordChangeTicket(emailAddress, connectionId)
    clientId.foreach(params.setClientId)
    organizationId.foreach(params.setOrganizationId)
    ttlSec.foreach(i => params.setTTLSeconds(i.intValue()))
    markEmailAsVerified.foreach(b => params.setMarkEmailAsVerified(b.booleanValue()))
    includeEmailInRedirect.foreach(b => params.setIncludeEmailInRedirect(b.booleanValue()))

    client
      .execute(() => client.management.tickets().requestPasswordChange(params))
      .map(_.getTicket)
  }
}
