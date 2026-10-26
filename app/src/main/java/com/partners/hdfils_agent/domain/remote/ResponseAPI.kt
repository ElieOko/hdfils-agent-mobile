package com.partners.hdfils_agent.domain.remote

import com.partners.hdfils_agent.domain.models.Address
import com.partners.hdfils_agent.domain.models.Agent
import com.partners.hdfils_agent.domain.models.ClientS
import com.partners.hdfils_agent.domain.models.Souscription
import com.partners.hdfils_agent.domain.models.TrashClean
import com.partners.hdfils_agent.domain.models.TrashCleanSingle
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAPI(
    val message: String = ""
)

@Serializable
data class ResponseAPIClient(
    val clients: List<ClientS> = emptyList()
)

@Serializable
data class ResponseAPIGenirc(
    val message: String = "",
    val agent: Agent = Agent(),
    val client: List<ClientS> = emptyList(),
    val trash_client: List<TrashClean> = emptyList()
)

@Serializable
data class ResponseCreateClient(
    val message: String = "",
    val client: Address? = null,
    val souscription: Souscription? = null
)

@Serializable
data class ResponseAPIGeneric(
    val trash_clients: List<TrashClean> = emptyList()
)

@Serializable
data class ResponseAPIGenericClean(
    val data: TrashCleanSingle,
    val message: String = ""
)
