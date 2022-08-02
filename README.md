[![CircleCI](https://circleci.com/gh/Executioner1939/zio-auth0.svg?style=shield&circle-token=0788f14be0abb7f8ab8194fbd2cd179122b3ee85)](https://circleci.com/gh/Executioner1939/zio-auth0)
[![Maven Central](https://img.shields.io/maven-central/v/io.bitlevel/zio-auth0_2.13.svg)](https://search.maven.org/search?q=a:zio-auth0)

**NB This Project is not yet complete**

# Introduction
This library serves to be a simple wrapper around the Auth0 Java SDK, this is comptabile with the version 2 API which can be found here:
[Auth0 Management API V2](https://auth0.com/docs/api/management/v2)

The current state of the wrapper means it's mostly suitable for interacting with the Users, Roles, and Permissions endpoints. once this is
complete the rest of the endpoints will be implemented.

## Implementation Status

The status of each of the API endpoints is documented below:

### Users

| API                | Implementation Status |
|--------------------|-----------------------|
| Connections        | Done                  |
| Organizations      | TODO                  |
| Roles              | Done                  |
| User Blocks        | Done                  |
| Users              | Done                  |
| Users By Email     | Done                  |
| Blacklists         | TODO                  |
| Stats              | TODO                  |
| Tenants            | TODO                  |
| Anomaly            | TODO                  |
| Tickets            | Done                  |

### Administration
| API                | Implementation Status |
|--------------------|-----------------------|
| Client Grants      | TODO                  |
| Clients            | TODO                  |
| Custom Domains     | TODO                  |
| Device Credentials | TODO                  |
| Grants             | TODO                  |
| Hooks              | TODO                  |
| Log Streams        | TODO                  |
| Logs               | TODO                  |
| Prompts            | TODO                  |
| Resource Servers   | TODO                  |
| Rules              | TODO                  |
| Rules Config       | TODO                  |
| Actions            | TODO                  |
| Attack Protection  | TODO                  |
| Blacklists         | TODO                  |
| Guardian           | TODO                  |
| Jobs               | TODO                  |
| Keys               | TODO                  |
| Stats              | TODO                  |
| Tenants            | TODO                  |
| Anomaly            | TODO                  |
| Admin              | TODO                  |

## Milestones

### Milestone 1

The first milestone will be to wrap the Auth0 Java SDK completely, providing a more Scala / ZIO like experience while working with it.

### Milestone 2

The second Milestone will be replacing the underlying Java SDK with a ZIO STTP Client,
you will need to provide your own BodySerializer instance to use with STTP therefore allowing you to choose which JSON library you would
like to use for processing.

Using STTP will also mean you can use whatever HTTP Client implementation you feel comfortable with.

