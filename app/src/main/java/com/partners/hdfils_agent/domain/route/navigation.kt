package com.partners.hdfils_agent.domain.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.partners.hdfils_agent.presentation.ui.pages.AuthPage
import com.partners.hdfils_agent.presentation.ui.pages.HomePageMain
import com.partners.hdfils_agent.presentation.ui.pages.InscriptionPage


@Composable
fun Navigation(navC: NavHostController, isConnected: Boolean){
    NavHost(navController = navC, startDestination = ScreenRoute.Auth.name, route = "root") {
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