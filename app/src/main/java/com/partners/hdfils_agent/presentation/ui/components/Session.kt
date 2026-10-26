package com.partners.hdfils_agent.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.domain.models.Agent
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.TrashClean

object SessionMemory {
    var agent: Agent? = null
    var clients: List<Client?> = emptyList()
    var trash: List<TrashClean> = emptyList()

    fun clear() {
        agent = null
        clients = emptyList()
        trash = emptyList()
    }
}

@Composable
fun rememberAgent(): Agent? {
    val context = LocalContext.current
    var agent by remember { mutableStateOf(SessionMemory.agent) }
    LaunchedEffect(Unit) {
        StoreData(context).getDataAgentAuth.collect {
            SessionMemory.agent = it
            agent = it
        }
    }
    return agent
}

@Composable
fun rememberClients(): List<Client?> {
    val context = LocalContext.current
    var items by remember { mutableStateOf(SessionMemory.clients) }
    LaunchedEffect(Unit) {
        StoreData(context).getDataClient.collect {
            SessionMemory.clients = it
            items = it
        }
    }
    return items
}

@Composable
fun rememberTrashList(): List<TrashClean> {
    val context = LocalContext.current
    var items by remember { mutableStateOf(SessionMemory.trash) }
    LaunchedEffect(Unit) {
        StoreData(context).getDataClientTrash.collect {
            SessionMemory.trash = it
            items = it
        }
    }
    return items
}
