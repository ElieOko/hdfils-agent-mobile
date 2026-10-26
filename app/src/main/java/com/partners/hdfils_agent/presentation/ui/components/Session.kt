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
import com.partners.hdfils_agent.domain.models.ClientS
import com.partners.hdfils_agent.domain.models.TrashClean

@Composable
fun rememberAgent(): Agent? {
    val context = LocalContext.current
    var agent by remember { mutableStateOf<Agent?>(null) }
    LaunchedEffect(Unit) {
        StoreData(context).getDataAgentAuth.collect { agent = it }
    }
    return agent
}

@Composable
fun rememberClients(): List<ClientS> {
    val context = LocalContext.current
    var items by remember { mutableStateOf<List<ClientS>>(emptyList()) }
    LaunchedEffect(Unit) {
        StoreData(context).getDataClient.collect { items = it }
    }
    return items
}

@Composable
fun rememberTrashList(): List<TrashClean> {
    val context = LocalContext.current
    var items by remember { mutableStateOf<List<TrashClean>>(emptyList()) }
    LaunchedEffect(Unit) {
        StoreData(context).getDataClientTrash.collect { items = it }
    }
    return items
}
