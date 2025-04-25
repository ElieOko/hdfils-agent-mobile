package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Trash(
    val client_id : Int,
    val state_trash_id : Int
)

@Serializable
data class TrashClean(
    var id : Int?,
    var client_id : Int?,
    var state_trash_id : Int?,
    var is_active : Int?,
    var created_at : String?,
    var client : ClientS? ,
)

@Serializable
data class TrashCleanSingle(
    val id              : Int,
    val client_id       : Int,
    var state_trash_id  : Int = 0,
    val created_at      : String
)