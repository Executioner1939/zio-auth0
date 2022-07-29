package com.skunkworks.modules.users.domain

final case class PermissionSource(source_type: String,
                                  source_name: String,
                                  source_id: String)
