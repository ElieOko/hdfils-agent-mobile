package com.partners.hdfils_agent

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.partners.hdfils_agent.domain.connectivity.AndroidConnectivityObserver
import com.partners.hdfils_agent.domain.connectivity.ConnectivityViewModel
import com.partners.hdfils_agent.domain.route.Navigation
import com.partners.hdfils_agent.presentation.ui.theme.HdfilsagentTheme

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                navHostController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Navigation(navHostController,isConnected)
                }
            }
        }
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
