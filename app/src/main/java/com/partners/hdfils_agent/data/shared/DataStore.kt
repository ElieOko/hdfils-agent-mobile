package com.partners.hdfils_agent.data.shared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.partners.hdfils_agent.domain.models.Agent
import com.partners.hdfils_agent.domain.models.AgentAuth
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.TrashClean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class StoreData(private val context: Context) {
    private val gson: Gson = Gson()
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val USERNAME = stringPreferencesKey("user__")
        val TRASH = stringPreferencesKey("trash__")
        val CLIENT = stringPreferencesKey("client_auth__")
        val CLIENT_ = stringPreferencesKey("client__")
        val AGENT = stringPreferencesKey("agent_auth__")


    }

    val getData: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME] ?: ""
        }
    fun isKeyStored(key: Preferences.Key<String>): Flow<Boolean>  =
        context.dataStore.data.map {
                preference -> preference.contains(key)
        }
//    val getDataClient: Flow<Client> = context.dataStore.data
//        .map { preferences ->
//            val jsonString = preferences[USERNAME] ?: ""
//            gson.fromJson(jsonString, Client::class.java)
//        }

    val getDataTrash: Flow<TrashClean> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[TRASH] ?: ""
            gson.fromJson(jsonString, TrashClean::class.java)
        }
    val getDataAgentAuth: Flow<Agent> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[AGENT] ?: ""
            gson.fromJson(jsonString, Agent::class.java)
        }

    val getDataClientTrash: Flow<List<TrashClean>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[TRASH]?.takeIf { it.isNotEmpty() } ?: ""
            if (jsonString.isEmpty()) {
                emptyList()
            } else {
                try {
                    gson.fromJson(jsonString, Array<TrashClean>::class.java).toList()
                } catch (e: JsonSyntaxException) {
                    emptyList()
                }
            }
        }.flowOn(Dispatchers.IO)


    val getDataClient: Flow<List<Client?>> = context.dataStore.data
        .map { preferences ->
        val jsonString = preferences[CLIENT_]?.takeIf { it.isNotEmpty() } ?: ""
        if (jsonString.isEmpty()) {
            emptyList()
        } else {
            try {
                gson.fromJson(jsonString, Array<Client>::class.java).toList()
            } catch (e: JsonSyntaxException) {
                emptyList()
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveDataClientTrash(trash: List<TrashClean>) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(trash)
            preferences[TRASH] = jsonString
        }
    }
    suspend fun saveDataClient(client: List<Client>) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(client)
            preferences[CLIENT_] = jsonString
        }
    }

    suspend fun saveDataAgentAuth(agent: Agent?) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(agent)
            preferences[AGENT] = jsonString
        }
    }
    suspend fun saveDataTrash(trash:List<TrashClean>) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(trash)
            preferences[TRASH] = jsonString
        }
    }
//    suspend fun saveDataClient(client:Client?) {
//        context.dataStore.edit { preferences ->
//            val jsonString = gson.toJson(client)
//            preferences[USERNAME] = jsonString
//        }
//    }

}


