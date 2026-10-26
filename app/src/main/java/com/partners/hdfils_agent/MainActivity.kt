package com.partners.hdfils_agent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.partners.hdfils_agent.data.shared.StoreData
import com.partners.hdfils_agent.domain.connectivity.AndroidConnectivityObserver
import com.partners.hdfils_agent.domain.connectivity.ConnectivityViewModel
import com.partners.hdfils_agent.domain.route.Navigation
import com.partners.hdfils_agent.domain.route.ScreenRoute
import com.partners.hdfils_agent.presentation.ui.theme.AppBackground
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_agent.presentation.ui.theme.HdfilsagentTheme
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0xFF307EC7.toInt()),
            navigationBarStyle = SystemBarStyle.light(0xFFFFFFFF.toInt(), 0xFFFFFFFF.toInt())
        )
        setContent {
            val viewModel = viewModel<ConnectivityViewModel> {
                ConnectivityViewModel(
                    connectivityObserver = AndroidConnectivityObserver(
                        context = applicationContext
                    )
                )
            }
            val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

            HdfilsagentTheme {
                CasaApp(isConnected)
            }
        }
    }
}

@Composable
private fun CasaApp(isConnected: Boolean) {
    val context = LocalContext.current
    val store = remember { StoreData(context) }
    var ready by remember { mutableStateOf(false) }
    var loggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(store) {
        loggedIn = store.isLoggedIn.first()
        ready = true
    }

    if (!ready) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = BrandBlue)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "CasaSalubrite",
                    color = BrandBlueDeep,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        val navHostController = rememberNavController()
        Navigation(
            navC = navHostController,
            isConnected = isConnected,
            startDestination = if (loggedIn) ScreenRoute.Home.name else ScreenRoute.Auth.name
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HdfilsagentTheme {
        Greeting("Android")
    }
}
