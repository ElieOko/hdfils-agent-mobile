package com.partners.hdfils_agent.domain.route

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.partners.hdfils_agent.presentation.ui.pages.AuthPage
import com.partners.hdfils_agent.presentation.ui.pages.HomePageMain
import com.partners.hdfils_agent.presentation.ui.pages.InscriptionPage


@Composable
fun Navigation(
    navC: NavHostController,
    isConnected: Boolean,
    startDestination: String = ScreenRoute.Auth.name,
){
    val context = LocalContext.current
    val backStackEntry by navC.currentBackStackEntryAsState()
    val atRoot = backStackEntry == null || navC.previousBackStackEntry == null
    var lastExitAt by remember { mutableLongStateOf(0L) }
    BackHandler(enabled = atRoot) {
        val now = android.os.SystemClock.elapsedRealtime()
        if (now - lastExitAt < 2_000L) {
            (context as? Activity)?.finish()
        } else {
            lastExitAt = now
            Toast.makeText(context, "Appuyez encore pour quitter", Toast.LENGTH_SHORT).show()
        }
    }
    NavHost(navController = navC, startDestination = startDestination, route = "root") {
        composable(ScreenRoute.Auth.name) {
            AuthPage(navC,isConnected)
        }
        composable(ScreenRoute.Home.name) {
            HomePageMain(navC,isConnected)
        }
        composable(ScreenRoute.Client.name) {
            InscriptionPage(isConnected)
        }
    }

}