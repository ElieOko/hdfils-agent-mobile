package com.partners.hdfils_agent.presentation.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.EndPoint
import com.partners.hdfils_agent.domain.models.Souscription
import com.partners.hdfils_agent.domain.models.Trash
import com.partners.hdfils_agent.domain.models.TrashClean
import com.partners.hdfils_agent.domain.models.communeLabel
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseAPIClient
import com.partners.hdfils_agent.domain.remote.ResponseAPIGeneric
import com.partners.hdfils_agent.domain.remote.ResponseAPIGenericClean
import com.partners.hdfils_agent.presentation.ui.components.AppLogo
import com.partners.hdfils_agent.presentation.ui.components.EmptyHint
import com.partners.hdfils_agent.presentation.ui.components.InfoRow
import com.partners.hdfils_agent.presentation.ui.components.MAlertDialog
import com.partners.hdfils_agent.presentation.ui.components.MCard
import com.partners.hdfils_agent.presentation.ui.components.ScreenHeader
import com.partners.hdfils_agent.presentation.ui.components.SubscriptionBlock
import com.partners.hdfils_agent.presentation.ui.components.formatApiDate
import com.partners.hdfils_agent.presentation.ui.components.rememberAgent
import com.partners.hdfils_agent.presentation.ui.components.rememberTrashList
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import io.ktor.client.call.body
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CheckBopeto(isConnected: Boolean = false) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val agent = rememberAgent()
    val listTrash = rememberTrashList()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var selected by remember { mutableStateOf<TrashClean?>(null) }
    var refreshing by remember { mutableStateOf(false) }
    var sending by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("") }
    var titleMsg by remember { mutableStateOf("Information") }

    fun refresh() {
        if (!isConnected) {
            Toast.makeText(
                context,
                "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        scope.launch {
            refreshing = true
            try {
                val clientsResponse = ClientKtor().getData(EndPoint.clientAction)
                when (clientsResponse.status.value) {
                    in 200..299 -> {
                        val res = clientsResponse.body<ResponseAPIClient>()
                        StoreData(context).saveDataClient(res.clients)
                        Toast.makeText(context, "Mises à jour", Toast.LENGTH_SHORT).show()
                    }
                    in 500..599 -> Toast.makeText(context, "Erreur serveur", Toast.LENGTH_LONG).show()
                    in 400..499 -> {
                        val res = clientsResponse.body<ResponseAPI>()
                        Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                    }
                }
                val trashResponse = ClientKtor().getData(EndPoint.trashAction)
                when (trashResponse.status.value) {
                    in 200..299 -> {
                        val res = trashResponse.body<ResponseAPIGeneric>()
                        StoreData(context).saveDataTrash(res.trash_clients)
                    }
                    in 500..599 -> Toast.makeText(context, "Erreur serveur", Toast.LENGTH_LONG).show()
                    in 400..499 -> {
                        val res = trashResponse.body<ResponseAPI>()
                        Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Erreur réseau", Toast.LENGTH_LONG).show()
            } finally {
                refreshing = false
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        containerColor = AppBackground,
        sheetContainerColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = { BottomSheetDefaults.DragHandle(color = BrandBlue) },
        sheetContent = {
            TrashDetailSheet(selected)
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(AppBackground)
        ) {
            ScreenHeader(
                title = "Vidage",
                subtitle = agent?.displayName() ?: "Poubelles des clients",
                onAction = { if (!refreshing) refresh() },
                actionIcon = Icons.Default.Refresh,
                actionDescription = "Actualiser"
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(8.dp))
                if (listTrash.isEmpty()) {
                    EmptyHint("Aucune poubelle enregistrée. Actualisez lorsque vous êtes en ligne.")
                } else {
                    listTrash.forEach { item ->
                        MCard(
                            items = item,
                            onClick = {
                                selected = item
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            onclickClean = {
                                if (item.state_trash_id == 3 || item.state_trash_id == 1) {
                                    titleMsg = "Information"
                                    msg = if (item.state_trash_id == 1) {
                                        "Cette poubelle est actuellement propre"
                                    } else {
                                        "Votre poubelle est encours de nettoyage"
                                    }
                                    isShow = true
                                } else if (!isConnected) {
                                    titleMsg = "Information"
                                    msg = "Vous n'êtes pas connecté veuillez vérifier votre connexion !!!"
                                    isShow = true
                                } else if (!sending) {
                                    sending = true
                                    scope.launch {
                                        try {
                                            val trashId = item.id
                                            if (trashId == null) {
                                                msg = "Cette poubelle ne peut pas être mise à jour."
                                                titleMsg = "Information"
                                                isShow = true
                                            } else {
                                                val response = ClientKtor().postData(
                                                    EndPoint.trashAction,
                                                    Trash(client_id = trashId, state_trash_id = 2)
                                                )
                                                when (response.status.value) {
                                                    in 200..299 -> {
                                                        val res = response.body<ResponseAPIGenericClean>()
                                                        Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                                                        msg = res.message
                                                        titleMsg = "Succès"
                                                        isShow = true
                                                    }
                                                    in 500..599 -> {
                                                        msg = "Erreur serveur réssayer plus tard nous resolvons ce problème"
                                                        titleMsg = "Erreur"
                                                        isShow = true
                                                    }
                                                    in 400..499 -> {
                                                        val res = response.body<ResponseAPI>()
                                                        msg = res.message
                                                        titleMsg = "Erreur"
                                                        isShow = true
                                                    }
                                                }
                                            }
                                        } catch (e: Exception) {
                                            msg = "Impossible de joindre le serveur."
                                            titleMsg = "Erreur"
                                            isShow = true
                                        } finally {
                                            sending = false
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (isShow) {
        MAlertDialog(
            dialogTitle = titleMsg,
            dialogText = msg,
            onDismissRequest = { isShow = false },
            onConfirmation = { isShow = false }
        )
    }
}

@Composable
private fun TrashDetailSheet(item: TrashClean?) {
    if (item == null) {
        Column(Modifier.padding(24.dp)) {
            Text("Sélectionnez une poubelle", color = InkMuted)
            Spacer(Modifier.height(24.dp))
        }
        return
    }
    val client = item.client
    val address = client?.address_client?.firstOrNull()
    val title = when (item.state_trash_id) {
        3 -> "Cette poubelle est pleine"
        2 -> "Cette poubelle est en nettoyage"
        else -> "Cette poubelle est propre"
    }
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppLogo(size = 56.dp, ring = AppBackground)
            Column(Modifier.padding(start = 12.dp)) {
                Text(
                    text = listOfNotNull(client?.prenom, client?.nom).filter { it.isNotBlank() }.joinToString(" ").ifBlank { "Client" },
                    color = Ink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(client?.code.orEmpty(), color = BrandBlue, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Card(shape = RoundedCornerShape(16.dp), colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = AppBackground)) {
            Text(
                text = title,
                modifier = Modifier.padding(14.dp),
                color = Ink,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(12.dp))
        if (client != null && (client.montant_souscription > 0 || !client.tranches.isNullOrEmpty())) {
            Text("Souscription", color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            SubscriptionBlock(
                Souscription(
                    montant_souscription = client.montant_souscription,
                    devise = client.devise,
                    nombre_tranches = client.nombre_tranches,
                    tranche = client.tranche,
                    tranches = client.tranches.orEmpty()
                )
            )
            Spacer(Modifier.height(8.dp))
        }
        HorizontalDivider(color = AppBackground)
        InfoRow(Icons.Default.Place, "Commune", communeLabel(address?.commune_id))
        InfoRow(Icons.Default.Place, "Quartier", address?.quartier.orEmpty())
        InfoRow(Icons.Default.Place, "Avenue", listOf(address?.avenue.orEmpty(), address?.numero_parcelle.orEmpty()).filter { it.isNotBlank() }.joinToString(" n° "))
        InfoRow(Icons.Default.DateRange, "Enregistrée le", formatApiDate(item.created_at))
        Spacer(Modifier.height(28.dp))
    }
}
