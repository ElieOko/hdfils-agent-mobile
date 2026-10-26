package com.partners.hdfils_agent.presentation.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.domain.models.cityLabel
import com.partners.hdfils_agent.domain.route.ScreenRoute
import com.partners.hdfils_agent.presentation.ui.components.AppLogo
import com.partners.hdfils_agent.presentation.ui.components.BrandWordmark
import com.partners.hdfils_agent.presentation.ui.components.InfoRow
import com.partners.hdfils_agent.presentation.ui.components.formatApiDate
import com.partners.hdfils_agent.presentation.ui.components.rememberAgent
import com.partners.hdfils_agent.presentation.ui.components.rememberClients
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDark
import com.partners.hdfils_agent.presentation.ui.theme.Danger
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(navC: NavHostController, isConnected: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val agent = rememberAgent()
    val clients = rememberClients()
    var confirmLogout by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(BrandBlue, BrandBlueDark)))
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo(size = 108.dp)
            Spacer(Modifier.height(12.dp))
            BrandWordmark()
            Text(
                text = agent?.displayName() ?: "Profil agent",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = agent?.code?.takeIf { it.isNotBlank() }?.let { "Code $it" } ?: "Session locale",
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (isConnected) "En ligne" else "Hors ligne",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                InfoRow(Icons.Default.Person, "Nom complet", agent?.displayName().orEmpty())
                InfoRow(Icons.Default.Home, "Code agent", agent?.code.orEmpty())
                InfoRow(Icons.Default.Phone, "Téléphone", agent?.telephone.orEmpty())
                InfoRow(Icons.Default.Person, "Genre", agent?.genre.orEmpty())
                InfoRow(Icons.Default.Place, "Adresse", agent?.address.orEmpty().ifBlank { "Non renseignée" })
                InfoRow(Icons.Default.DateRange, "Date de naissance", formatApiDate(agent?.date_naissance))
                InfoRow(Icons.Default.Place, "Ville", cityLabel(agent?.city_id))
                InfoRow(Icons.Default.DateRange, "Compte créé", formatApiDate(agent?.created_at))
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Activité", color = InkMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "${clients.size}",
                    color = BrandBlue,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (clients.size > 1) "clients suivis sur cette session" else "client suivi sur cette session",
                    color = Ink,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        if (confirmLogout) {
            Text(
                text = "Déconnecter cet agent et effacer la session sur cet appareil ?",
                color = Ink,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        StoreData(context)
                        navC.navigate(ScreenRoute.Auth.name) {
                            popUpTo(navC.graph.id) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Danger)
            ) {
                androidx.compose.material3.Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Text("  Confirmer la déconnexion", fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { confirmLogout = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Ink)
            ) {
                Text("Annuler")
            }
        } else {
            Button(
                onClick = { confirmLogout = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Danger
                )
            ) {
                androidx.compose.material3.Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Text("  Se déconnecter", fontWeight = FontWeight.SemiBold)
            }
        }
        Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
            Text("Casa Na yo · Agent", color = InkMuted, fontSize = 12.sp)
        }
        }
    }
}
