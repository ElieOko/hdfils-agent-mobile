package com.partners.hdfils_agent.presentation.ui.pages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.communeLabel
import com.partners.hdfils_agent.domain.models.displayName
import com.partners.hdfils_agent.domain.models.identityKey
import com.partners.hdfils_agent.presentation.ui.components.EmptyHint
import com.partners.hdfils_agent.presentation.ui.components.InfoRow
import com.partners.hdfils_agent.presentation.ui.components.PrimaryButton
import com.partners.hdfils_agent.presentation.ui.components.ScreenHeader
import com.partners.hdfils_agent.presentation.ui.components.formatMoney
import com.partners.hdfils_agent.presentation.ui.components.rememberClients
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted

private sealed class ClientMode {
    data object List : ClientMode()
    data object Create : ClientMode()
    data class Detail(val index: Int, val key: String) : ClientMode()
}

@Composable
fun ClientSection(isConnected: Boolean) {
    var mode by rememberSaveable { mutableStateOf("list") }
    var detailIndex by rememberSaveable { mutableStateOf(-1) }
    var detailKey by rememberSaveable { mutableStateOf("") }
    val screen = when (mode) {
        "create" -> ClientMode.Create
        "detail" -> ClientMode.Detail(detailIndex, detailKey)
        else -> ClientMode.List
    }

    BackHandler(enabled = screen !is ClientMode.List) {
        mode = "list"
    }

    when (screen) {
        ClientMode.List -> ClientListPage(
            onCreate = { mode = "create" },
            onOpen = { index, key ->
                detailIndex = index
                detailKey = key
                mode = "detail"
            }
        )
        ClientMode.Create -> InscriptionPage(
            isConnected = isConnected,
            onDone = { mode = "list" }
        )
        is ClientMode.Detail -> ClientDetailPage(
            index = screen.index,
            key = screen.key,
            onBack = { mode = "list" }
        )
    }
}

@Composable
private fun ClientListPage(
    onCreate: () -> Unit,
    onOpen: (Int, String) -> Unit,
) {
    val clients = rememberClients().filterNotNull().filter { it.hasVisibleIdentity() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        ScreenHeader(
            title = "Clients",
            subtitle = when (clients.size) {
                0 -> "Aucun client enregistré"
                1 -> "1 client enregistré"
                else -> "${clients.size} clients enregistrés"
            }
        )
        PrimaryButton(
            text = "Nouveau client",
            onClick = onCreate,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        if (clients.isEmpty()) {
            EmptyHint("Les clients ajoutés apparaîtront dans cette liste.")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(
                    items = clients,
                    key = { index, client -> "$index-${client.identityKey()}" }
                ) { index, client ->
                    ClientRow(
                        client = client,
                        onClick = { onOpen(index, client.identityKey()) }
                    )
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun ClientRow(client: Client, onClick: () -> Unit) {
    val place = listOf(
        communeLabel(client.commune_id),
        client.quartier
    ).filter { it.isNotBlank() && it != "—" }.joinToString(" · ")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(BrandBlueLight)
                    .padding(9.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = client.displayName(),
                    color = Ink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (client.telephone.isNotBlank()) {
                    Text(client.telephone, color = InkMuted, fontSize = 13.sp)
                }
                if (place.isNotBlank()) {
                    Text(
                        text = place,
                        color = InkMuted,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (client.montant_souscription > 0) {
                    Text(
                        text = formatMoney(client.montant_souscription, client.devise),
                        color = BrandBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Voir le détail",
                tint = InkMuted
            )
        }
    }
}

@Composable
private fun ClientDetailPage(
    index: Int,
    key: String,
    onBack: () -> Unit,
) {
    val clients = rememberClients().filterNotNull()
    val client = clients.getOrNull(index)?.takeIf { it.identityKey() == key }
        ?: clients.firstOrNull { it.identityKey() == key }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        ScreenHeader(
            title = client?.displayName() ?: "Client",
            subtitle = "Détail client",
            onBack = onBack
        )
        if (client == null) {
            EmptyHint("Ce client n'est plus dans la liste.")
            return@Column
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    InfoRow(Icons.Default.Person, "Nom complet", client.displayName())
                    InfoRow(Icons.Default.Phone, "Téléphone", client.telephone)
                    InfoRow(Icons.Default.Place, "Commune", communeLabel(client.commune_id))
                    InfoRow(Icons.Default.Place, "Avenue", client.avenue)
                    InfoRow(Icons.Default.Place, "Quartier", client.quartier)
                    InfoRow(Icons.Default.Home, "Numéro de parcelle", client.numero_parcelle)
                    InfoRow(
                        Icons.Default.Info,
                        "Montant de souscription",
                        if (client.montant_souscription > 0) {
                            formatMoney(client.montant_souscription, client.devise)
                        } else {
                            ""
                        }
                    )
                    InfoRow(Icons.Default.Info, "Devise", client.devise)
                    InfoRow(
                        Icons.Default.Info,
                        "Nombre de tranches",
                        if (client.nombre_tranches > 0) client.nombre_tranches.toString() else ""
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

private fun Client.hasVisibleIdentity(): Boolean {
    return nom.isNotBlank() || prenom.isNotBlank() || telephone.isNotBlank()
}
