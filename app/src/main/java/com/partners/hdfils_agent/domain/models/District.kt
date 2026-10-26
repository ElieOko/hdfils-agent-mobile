package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class District(
    val nom     : String,
    val commune : List<Commune>
)
data class DistrictDefault(
    val id  : Int,
    val nom : String
)
