package com.partners.hdfils_agent.presentation.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.EndPoint.agentAuth
import com.partners.hdfils_agent.domain.models.AgentAuth
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseAPIGenirc
import com.partners.hdfils_agent.domain.route.ScreenRoute
import com.partners.hdfils_agent.presentation.ui.components.AppField
import com.partners.hdfils_agent.presentation.ui.components.AppLogo
import com.partners.hdfils_agent.presentation.ui.components.BrandWordmark
import com.partners.hdfils_agent.presentation.ui.components.PrimaryButton
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_agent.presentation.ui.theme.Danger
import com.partners.hdfils_agent.presentation.ui.theme.Ink
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import io.ktor.client.call.body
import kotlinx.coroutines.launch

@Composable
fun AuthPage(navC: NavHostController, isConnected: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var matricule by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BrandBlue, BrandBlueDeep)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))
            AppLogo(size = 112.dp)
            Spacer(Modifier.height(14.dp))
            BrandWordmark()
            Text(
                text = "Espace agent terrain",
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
            Spacer(Modifier.height(28.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text(
                        text = "Connexion",
                        color = Ink,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Entrez votre code agent pour ouvrir la tournée.",
                        color = InkMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                    )
                    AppField(
                        value = matricule,
                        onValueChange = { matricule = it },
                        label = "Code agent",
                        icon = Icons.Default.Lock
                    )
                    if (!isConnected) {
                        Text(
                            text = "Vous êtes hors ligne. Vérifiez la connexion avant de continuer.",
                            color = Danger,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    Spacer(Modifier.height(18.dp))
                    PrimaryButton(
                        text = "Se connecter",
                        loading = loading,
                        enabled = matricule.isNotBlank(),
                        onClick = {
                            if (!isConnected) {
                                Toast.makeText(
                                    context,
                                    "Vous n'êtes pas connecté, vérifiez votre connexion.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                scope.launch {
                                    loading = true
                                    try {
                                        val response = ClientKtor().postData(agentAuth, AgentAuth(matricule.trim()))
                                        when (response.status.value) {
                                            in 200..299 -> {
                                                val res = response.body<ResponseAPIGenirc>()
                                                val store = StoreData(context)
                                                store.saveDataAgentAuth(res.agent)
                                                store.saveDataClient(res.client)
                                                store.saveDataClientTrash(res.trash_client)
                                                Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                                                navC.navigate(ScreenRoute.Home.name) {
                                                    popUpTo(navC.graph.id) { inclusive = true }
                                                }
                                            }
                                            in 500..599 -> {
                                                Toast.makeText(context, "Erreur serveur", Toast.LENGTH_LONG).show()
                                            }
                                            in 400..499 -> {
                                                val res = response.body<ResponseAPI>()
                                                Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("AuthPage", e.message ?: "login")
                                        Toast.makeText(
                                            context,
                                            "Impossible de joindre le serveur.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = Color.White,
                trackColor = BrandBlueDeep
            )
        }
    }
}
