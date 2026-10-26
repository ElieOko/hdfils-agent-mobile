package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Trash(
    val client_id: Int,
    val state_trash_id: Int
)

@Serializable
data class TrashClean(
    var id: Int? = null,
    var client_id: Int? = null,
    var state_trash_id: Int? = null,
    var is_active: Int? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var client: ClientS? = null,
)

@Serializable
data class TrashCleanSingle(
    val id: Int,
    val client_id: Int,
    var state_trash_id: Int = 0,
    val created_at: String
)
