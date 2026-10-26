package com.partners.hdfils_agent.data.shared

import android.content.Context
import android.util.Log
import com.partners.hdfils_agent.data.utils.EndPoint
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseAPIClient
import com.partners.hdfils_agent.domain.remote.ResponseAPIGeneric
import io.ktor.client.call.body

sealed class SyncResult {
    data object Success : SyncResult()
    data class Error(val message: String) : SyncResult()
}

suspend fun syncLocalData(context: Context): SyncResult {
    return try {
        val store = StoreData(context)
        val clientResponse = ClientKtor().getData(EndPoint.clientAction)
        when (val status = clientResponse.status.value) {
            in 200..299 -> {
                val res = clientResponse.body<ResponseAPIClient>()
                store.saveDataClient(res.clients)
            }
            in 400..499 -> {
                val res = clientResponse.body<ResponseAPI>()
                return SyncResult.Error(res.message)
            }
            else -> return SyncResult.Error("Erreur serveur")
        }

        val trashResponse = ClientKtor().getData(EndPoint.trashAction)
        when (val status = trashResponse.status.value) {
            in 200..299 -> {
                val res = trashResponse.body<ResponseAPIGeneric>()
                store.saveDataTrash(res.trash_clients)
            }
            in 400..499 -> {
                val res = trashResponse.body<ResponseAPI>()
                return SyncResult.Error(res.message)
            }
            else -> return SyncResult.Error("Erreur serveur")
        }
        SyncResult.Success
    } catch (error: Exception) {
        Log.e("SYNC", error.message ?: "sync")
        SyncResult.Error(error.message ?: "Impossible d'actualiser les données")
    }
}
