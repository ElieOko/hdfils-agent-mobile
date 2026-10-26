package com.partners.hdfils_agent.presentation.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.data.utils.AgentCodeValidator
import com.partners.hdfils_agent.data.utils.EndPoint.agentAuth
import com.partners.hdfils_agent.domain.models.AgentAuth
import com.partners.hdfils_agent.domain.remote.ClientKtor
import com.partners.hdfils_agent.domain.remote.ResponseAPI
import com.partners.hdfils_agent.domain.remote.ResponseAPIGenirc
import com.partners.hdfils_agent.domain.route.ScreenRoute
import com.partners.hdfils_agent.presentation.ui.components.AppField
import com.partners.hdfils_agent.presentation.ui.components.AppLogo
import com.partners.hdfils_agent.presentation.ui.components.PrimaryButton
import com.partners.hdfils_agent.presentation.ui.components.SessionMemory
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import io.ktor.client.call.body
import kotlinx.coroutines.launch

@Composable
fun AuthPage(navC: NavHostController, isConnected: Boolean) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var matricule by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf<String?>(null) }
    var hasTried by remember { mutableStateOf(false) }
    var isActive by remember { mutableStateOf(true) }

    fun submit() {
        hasTried = true
        val error = AgentCodeValidator.errorOrNull(matricule)
        codeError = error
        if (error != null) return
        if (!isConnected) {
            Toast.makeText(
                context,
                "Vous n'êtes pas connecté. Vérifiez votre connexion.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (!isActive) return
        isActive = false
        focusManager.clearFocus()
        scope.launch {
            try {
                val response = ClientKtor().postData(agentAuth, AgentAuth(matricule.trim()))
                when (val status = response.status.value) {
                    in 200..299 -> {
                        val res = response.body<ResponseAPIGenirc>()
                        val store = StoreData(context)
                        store.saveDataAgentAuth(res.agent)
                        SessionMemory.agent = res.agent
                        if (res.trash_client.isNotEmpty()) {
                            store.saveDataClientTrash(res.trash_client)
                            SessionMemory.trash = res.trash_client
                        }
                        if (res.client.isNotEmpty()) {
                            SessionMemory.clients = store.saveDataClient(res.client)
                        }
                        store.setLoggedIn(true)
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
                        codeError = res.message
                        Toast.makeText(context, res.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(context, "Réponse inattendue ($status)", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (error: Exception) {
                Log.e("AUTH", error.message ?: "auth")
                Toast.makeText(
                    context,
                    "Connexion impossible. Vérifiez le code ou le réseau.",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                isActive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppLogo(size = 92.dp)
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = "CasaSalubrite",
                            color = BrandBlueDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Connexion agent",
                            color = InkMuted,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "Saisissez votre code agent. La session reste ouverte sur cet appareil.",
                            color = InkMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp, bottom = 18.dp)
                        )
                        AppField(
                            value = matricule,
                            onValueChange = {
                                matricule = it
                                if (hasTried) codeError = AgentCodeValidator.errorOrNull(it)
                            },
                            label = "Code agent",
                            icon = Icons.Default.Lock,
                            isError = codeError != null,
                            supportingText = codeError,
                            imeAction = ImeAction.Done,
                            onImeAction = { submit() }
                        )
                        Spacer(Modifier.height(18.dp))
                        PrimaryButton(
                            text = "Se connecter",
                            onClick = { submit() },
                            loading = !isActive
                        )
                    }
                }
                Spacer(Modifier.height(96.dp))
            }
        }
    }
}
