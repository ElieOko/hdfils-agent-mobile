package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val nom             : String= "",
    val prenom          : String="",
    val telephone       : String="",
    val avenue          : String="",
    val quartier        : String="",
    val numero_parcelle : String="",
    val commune_id      : Int=0
)

@Serializable
data class ClientS(
    var id          : Int = 0,
    val nom         : String= "",
    val prenom      : String="",
    val telephone   : String="",
    val address_client : List<Address> ,
)

@Serializable
data class Address(
    var id              : Int = 0,
    var avenue          : String = "",
    var numero_parcelle : String = "",
    var quartier        : String = "",
    var commune_id      : Int = 0
)