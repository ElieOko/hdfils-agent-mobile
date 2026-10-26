package com.partners.hdfils_agent.presentation.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.navigation.NavHostController
import com.partners.hdfils_agent.domain.models.ClientS
import com.partners.hdfils_agent.domain.models.Route
import com.partners.hdfils_agent.domain.models.Souscription
import com.partners.hdfils_agent.domain.models.communeLabel
import com.partners.hdfils_agent.presentation.ui.components.ClientStatsCardWithChart
import com.partners.hdfils_agent.presentation.ui.components.EmptyHint
import com.partners.hdfils_agent.presentation.ui.components.ScreenHeader
import com.partners.hdfils_agent.presentation.ui.components.SectionLabel
import com.partners.hdfils_agent.presentation.ui.components.SubscriptionBlock
import com.partners.hdfils_agent.presentation.ui.components.WasteCollectionStatsCard
import com.partners.hdfils_agent.presentation.ui.components.rememberAgent
import com.partners.hdfils_agent.presentation.ui.components.rememberClients
import com.partners.hdfils_agent.presentation.ui.components.rememberTrashList
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted

@Composable
fun HomePageMain(navC: NavHostController, isConnected: Boolean) {
    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                Route().bottomNavigationItems().forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        label = { Text(navigationItem.title) },
                        icon = {
                            Icon(
                                painter = painterResource(navigationItem.icon),
                                contentDescription = navigationItem.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandBlue,
                            selectedTextColor = BrandBlue,
                            indicatorColor = BrandBlueLight,
                            unselectedIconColor = InkMuted,
                            unselectedTextColor = InkMuted
                        ),
                        onClick = { navigationSelectedItem = index }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(AppBackground)
        ) {
            when (navigationSelectedItem) {
                0 -> HomePage()
                1 -> InscriptionPage(isConnected)
                2 -> CheckBopeto(isConnected)
                3 -> ProfilePage(navC, isConnected)
            }
        }
    }
}

@Composable
fun HomePage() {
    val agent = rememberAgent()
    val clients = rememberClients()
    val trash = rememberTrashList()
    val totals = clients
        .groupBy { it.devise.ifBlank { "FC" } }
        .map { (devise, list) -> devise to list.sumOf { it.montant_souscription } }
        .filter { it.second > 0 }
    val greeting = agent?.prenom?.takeIf { it.isNotBlank() }?.let { "Bonjour $it" } ?: "Bonjour"

    Column(Modifier.fillMaxSize()) {
        ScreenHeader(
            title = greeting,
            subtitle = agent?.code?.takeIf { it.isNotBlank() }?.let { "Code $it · Casa Na yo" }
                ?: "Casa Na yo"
        )
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
        ClientStatsCardWithChart(clientCount = clients.size, totals = totals)
        WasteCollectionStatsCard(
            pleine = trash.count { it.state_trash_id == 3 },
            enCours = trash.count { it.state_trash_id == 2 },
            propre = trash.count { it.state_trash_id == 1 }
        )
        Spacer(Modifier.size(8.dp))
        SectionLabel("Clients")
        if (clients.isEmpty()) {
            EmptyHint("Aucun client pour le moment. Inscrivez un foyer depuis l'onglet Client.")
        } else {
            clients.forEach { ClientOverviewCard(it) }
        }
        Spacer(Modifier.size(18.dp))
        }
    }
}

@Composable
private fun ClientOverviewCard(client: ClientS) {
    val address = client.address_client?.firstOrNull()
    val place = listOfNotNull(
        address?.avenue?.takeIf { it.isNotBlank() },
        address?.numero_parcelle?.takeIf { it.isNotBlank() }?.let { "n° $it" },
        address?.quartier?.takeIf { it.isNotBlank() },
        communeLabel(address?.commune_id).takeIf { it != "—" }
    ).joinToString(", ")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BrandBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = client.prenom.firstOrNull()?.uppercase() ?: "C",
                        color = BrandBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Column(Modifier.padding(start = 12.dp)) {
                    Text(
                        text = listOf(client.prenom, client.nom).filter { it.isNotBlank() }.joinToString(" ").ifBlank { "Client" },
                        color = Ink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = client.code.ifBlank { client.telephone },
                        color = BrandBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            if (place.isNotBlank()) {
                Text(
                    text = place,
                    color = InkMuted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            if (client.montant_souscription > 0 || !client.tranches.isNullOrEmpty()) {
                Spacer(Modifier.size(8.dp))
                SubscriptionBlock(
                    Souscription(
                        montant_souscription = client.montant_souscription,
                        devise = client.devise,
                        nombre_tranches = client.nombre_tranches,
                        tranche = client.tranche,
                        tranches = client.tranches.orEmpty()
                    )
                )
            }
        }
    }
}
