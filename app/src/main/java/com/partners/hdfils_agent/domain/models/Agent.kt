package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Agent(
    val code : String,
    val nom : String,
    val prenom : String,
    val postnom : String?,
    val genre : String?,
    val telephone : String?,
    val address : String?,
)
@Serializable
data class AgentAuth(val code: String)
