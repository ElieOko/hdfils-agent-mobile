package com.partners.hdfils_agent.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val nom: String = "",
    val prenom: String = "",
    val telephone: String = "",
    val avenue: String = "",
    val quartier: String = "",
    val numero_parcelle: String = "",
    val commune_id: Int = 0,
    val montant_souscription: Int = 0,
    val devise: String = "",
    val nombre_tranches: Int = 0,
    val tranche: String = "",
    val commentaire: String = "",
)

@Serializable
data class ClientCreate(
    val nom: String,
    val prenom: String,
    val telephone: String,
    val commune_id: Int,
    val avenue: String,
    val quartier: String,
    val numero_parcelle: String,
    val montant_souscription: Int,
    val devise: String,
    val nombre_tranches: Int,
)

fun Client.displayName(): String {
    return listOf(prenom, nom)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "Client" }
}

fun Client.identityKey(): String {
    return listOf(telephone, nom, prenom, numero_parcelle, commune_id.toString(), avenue)
        .joinToString("|") { it.trim().lowercase() }
}

@Serializable
data class Tranche(
    val numero: Int = 0,
    val montant: Int = 0,
)

@Serializable
data class Souscription(
    val montant_souscription: Int = 0,
    val devise: String = "",
    val nombre_tranches: Int = 0,
    val tranche: String = "",
    val tranches: List<Tranche> = emptyList(),
)

@Serializable
data class ClientS(
    var id: Int = 0,
    val code: String = "",
    val nom: String = "",
    val prenom: String = "",
    val telephone: String = "",
    val montant_souscription: Int = 0,
    val devise: String = "",
    val nombre_tranches: Int = 0,
    val tranche: String = "",
    val tranches: List<Tranche>? = emptyList(),
    val created_at: String? = null,
    val updated_at: String? = null,
    val address_client: List<Address>? = emptyList(),
)

@Serializable
data class Address(
    var id: Int = 0,
    var client_id: Int = 0,
    var avenue: String = "",
    var numero_parcelle: String = "",
    var quartier: String = "",
    var commune_id: Int = 0,
    var created_at: String? = null,
    var updated_at: String? = null,
)
