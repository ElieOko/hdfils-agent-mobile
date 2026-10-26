package com.partners.hdfils_agent.presentation.ui.pages

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.partners.hdfils_agent.domain.models.DateFilter
import com.partners.hdfils_agent.domain.models.DeviseFilter
import com.partners.hdfils_agent.domain.models.communeLabel
import com.partners.hdfils_agent.domain.models.displayName
import com.partners.hdfils_agent.domain.models.identityKey
import com.partners.hdfils_agent.domain.models.matchesDate
import com.partners.hdfils_agent.domain.models.matchesDevise
import com.partners.hdfils_agent.domain.models.matchesSearch
import com.partners.hdfils_agent.presentation.ui.components.EmptyHint
import com.partners.hdfils_agent.presentation.ui.components.InfoRow
import com.partners.hdfils_agent.presentation.ui.components.PrimaryButton
import com.partners.hdfils_agent.presentation.ui.components.ScreenHeader
import com.partners.hdfils_agent.presentation.ui.components.brandFieldColors
import com.partners.hdfils_agent.presentation.ui.components.focusBringIntoView
import com.partners.hdfils_agent.presentation.ui.components.formatMoney
import com.partners.hdfils_agent.presentation.ui.components.rememberClients
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import com.partners.hdfils_agent.presentation.ui.theme.Line

private sealed class ClientMode {
    data object List : ClientMode()
    data object Create : ClientMode()
    data class Detail(val index: Int, val key: String) : ClientMode()
}

@Composable
fun ClientSection(isConnected: Boolean, active: Boolean = true) {
    var mode by rememberSaveable { mutableStateOf("list") }
    var detailIndex by rememberSaveable { mutableIntStateOf(-1) }
    var detailKey by rememberSaveable { mutableStateOf("") }
    var query by rememberSaveable { mutableStateOf("") }
    var deviseName by rememberSaveable { mutableStateOf(DeviseFilter.ALL.name) }
    var dateName by rememberSaveable { mutableStateOf(DateFilter.ALL.name) }
    val form = remember { ClientFormState() }
    var confirmLeave by remember { mutableStateOf(false) }
    val deviseFilter = DeviseFilter.valueOf(deviseName)
    val dateFilter = DateFilter.valueOf(dateName)
    val screen = when (mode) {
        "create" -> ClientMode.Create
        "detail" -> ClientMode.Detail(detailIndex, detailKey)
        else -> ClientMode.List
    }

    fun leaveCreation() {
        if (form.isDirty) confirmLeave = true else mode = "list"
    }

    LaunchedEffect(active) {
        if (!active) confirmLeave = false
    }

    BackHandler(enabled = active && screen !is ClientMode.List) {
        if (screen is ClientMode.Create) leaveCreation() else mode = "list"
    }

    if (confirmLeave && active) {
        AlertDialog(
            onDismissRequest = { confirmLeave = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Brouillon",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "En revenant en arrière, cette création est en brouillon.",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            dismissButton = {
                TextButton(onClick = { confirmLeave = false }) {
                    Text("Rester", color = BrandBlue, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmLeave = false
                    mode = "list"
                }) {
                    Text("Revenir", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    AnimatedContent(
        targetState = screen,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            val opening = targetState !is ClientMode.List && initialState is ClientMode.List
            val easing = FastOutSlowInEasing
            if (opening) {
                (slideInHorizontally(tween(280, easing = easing)) { it } + fadeIn(tween(220))) togetherWith
                    (slideOutHorizontally(tween(280, easing = easing)) { -it / 4 } + fadeOut(tween(180)))
            } else {
                (slideInHorizontally(tween(280, easing = easing)) { -it / 4 } + fadeIn(tween(220))) togetherWith
                    (slideOutHorizontally(tween(280, easing = easing)) { it } + fadeOut(tween(180)))
            }
        },
        label = "client-mode"
    ) { current ->
        when (current) {
            ClientMode.List -> ClientListPage(
                query = query,
                onQueryChange = { query = it },
                deviseFilter = deviseFilter,
                onDeviseFilter = { deviseName = it.name },
                dateFilter = dateFilter,
                onDateFilter = { dateName = it.name },
                onResetFilters = {
                    query = ""
                    deviseName = DeviseFilter.ALL.name
                    dateName = DateFilter.ALL.name
                },
                onCreate = { mode = "create" },
                onOpen = { index, key ->
                    detailIndex = index
                    detailKey = key
                    mode = "detail"
                }
            )
            ClientMode.Create -> InscriptionPage(
                isConnected = isConnected,
                form = form,
                onBack = { leaveCreation() },
                onDone = { mode = "list" }
            )
            is ClientMode.Detail -> ClientDetailPage(
                index = current.index,
                key = current.key,
                onBack = { mode = "list" }
            )
        }
    }
}

@Composable
private fun ClientListPage(
    query: String,
    onQueryChange: (String) -> Unit,
    deviseFilter: DeviseFilter,
    onDeviseFilter: (DeviseFilter) -> Unit,
    dateFilter: DateFilter,
    onDateFilter: (DateFilter) -> Unit,
    onResetFilters: () -> Unit,
    onCreate: () -> Unit,
    onOpen: (Int, String) -> Unit,
) {
    val clients = rememberClients().filterNotNull().filter { it.hasVisibleIdentity() }
    val searched = remember(clients, query) { clients.filter { it.matchesSearch(query) } }
    val deviseBase = remember(searched, dateFilter) { searched.filter { it.matchesDate(dateFilter) } }
    val dateBase = remember(searched, deviseFilter) { searched.filter { it.matchesDevise(deviseFilter) } }
    val visible = remember(deviseBase, deviseFilter) { deviseBase.filter { it.matchesDevise(deviseFilter) } }
    val filtersOn = query.isNotBlank() || deviseFilter != DeviseFilter.ALL || dateFilter != DateFilter.ALL
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        ScreenHeader(
            title = "Clients",
            subtitle = clientCountLabel(clients.size, visible.size, filtersOn)
        )
        PrimaryButton(
            text = "Nouveau client",
            onClick = onCreate,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusBringIntoView(),
            singleLine = true,
            placeholder = { Text("Avenue, quartier, numéro ou nom") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue) },
            trailingIcon = if (query.isNotEmpty()) {
                {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Effacer la recherche")
                    }
                }
            } else {
                null
            },
            shape = RoundedCornerShape(14.dp),
            colors = brandFieldColors()
        )
        FilterChipRow {
            DeviseFilter.entries.forEach { filter ->
                val count = deviseBase.count { it.matchesDevise(filter) }
                TagChip(
                    label = "${deviseFilterLabel(filter)} · $count",
                    selected = deviseFilter == filter,
                    onClick = { onDeviseFilter(filter) }
                )
            }
        }
        FilterChipRow {
            DateFilter.entries.forEach { filter ->
                val count = dateBase.count { it.matchesDate(filter) }
                TagChip(
                    label = "${dateFilterLabel(filter)} · $count",
                    selected = dateFilter == filter,
                    onClick = { onDateFilter(filter) }
                )
            }
        }
        if (dateFilter != DateFilter.ALL) {
            Text(
                text = "Sans date d'ajout : uniquement dans Tous.",
                color = InkMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
        when {
            clients.isEmpty() -> Box(Modifier.weight(1f)) {
                EmptyHint("Les clients ajoutés apparaîtront dans cette liste.")
            }
            visible.isEmpty() -> Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyHint("Aucun client ne correspond à ces filtres.")
                TextButton(onClick = onResetFilters) {
                    Text("Réinitialiser les filtres", color = BrandBlue, fontWeight = FontWeight.Bold)
                }
            }
            else -> LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(
                    items = visible,
                    key = { index, client -> "$index-${client.identityKey()}" }
                ) { _, client ->
                    ClientRow(
                        client = client,
                        onClick = { onOpen(clients.indexOf(client), client.identityKey()) }
                    )
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun FilterChipRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = { content() }
    )
}

@Composable
private fun TagChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color.Black
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = Color.Black,
            selectedContainerColor = BrandBlue,
            selectedLabelColor = Color.White
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = Line,
            selectedBorderColor = BrandBlue
        )
    )
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

private fun clientCountLabel(total: Int, shown: Int, filtersOn: Boolean): String {
    if (total == 0) return "Aucun client enregistré"
    if (!filtersOn || shown == total) {
        return if (total == 1) "1 client enregistré" else "$total clients enregistrés"
    }
    val shownLabel = if (shown == 1) "1 affiché" else "$shown affichés"
    return "$shownLabel sur $total"
}

private fun deviseFilterLabel(filter: DeviseFilter): String = when (filter) {
    DeviseFilter.ALL -> "Tous"
    DeviseFilter.DOLLAR -> "$"
    DeviseFilter.FC -> "FC"
}

private fun dateFilterLabel(filter: DateFilter): String = when (filter) {
    DateFilter.ALL -> "Tous"
    DateFilter.TODAY -> "Aujourd'hui"
    DateFilter.YESTERDAY -> "Hier"
    DateFilter.FIVE_DAYS_AGO -> "Il y a 5 jours"
    DateFilter.THIS_MONTH -> "Ce mois"
}

private fun Client.hasVisibleIdentity(): Boolean {
    return nom.isNotBlank() || prenom.isNotBlank() || telephone.isNotBlank()
}
