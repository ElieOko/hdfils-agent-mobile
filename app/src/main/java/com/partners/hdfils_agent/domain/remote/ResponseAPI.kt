package com.partners.hdfils_agent.domain.remote

import com.partners.hdfils_agent.domain.models.Agent
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.TrashClean
import com.partners.hdfils_agent.domain.models.TrashCleanSingle
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAPI(
    val message: String
)

@Serializable
data class ResponseAPIClient(
    val clients: List<Client>
)
@Serializable
data class ResponseAPIGenirc(val message: String, val agent : Agent, val client: List<Client>, val trash_client : List<TrashClean>)

@Serializable
data class ResponseAPIGeneric(
    val trash_clients: List<TrashClean>
)
@Serializable
data class ResponseAPIGenericClean(
    val data: TrashCleanSingle,
    val message: String
)

