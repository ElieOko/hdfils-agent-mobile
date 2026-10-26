package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Agent(
    val id: Int = 0,
    val code: String = "",
    val city_id: Int? = null,
    val nom: String = "",
    val prenom: String = "",
    val postnom: String? = null,
    val genre: String? = null,
    val telephone: String? = null,
    val address: String? = null,
    val date_naissance: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
) {
    fun displayName(): String {
        return listOf(prenom, nom, postnom.orEmpty())
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { "Agent" }
    }
}

@Serializable
data class AgentAuth(val code: String)
