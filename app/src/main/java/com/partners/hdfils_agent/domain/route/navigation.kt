package com.partners.hdfils_agent.domain.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.partners.hdfils_agent.presentation.ui.pages.AuthPage
import com.partners.hdfils_agent.presentation.ui.pages.HomePage
import com.partners.hdfils_agent.presentation.ui.pages.HomePageMain
import com.partners.hdfils_agent.presentation.ui.pages.InscriptionPage


@Composable
fun Navigation(navC: NavHostController){
    NavHost(navController = navC, startDestination = ScreenRoute.Home.name, route = "root") {
        composable(ScreenRoute.Auth.name) {
            AuthPage(navC)
        }
        composable(ScreenRoute.Home.name) {
            HomePageMain(navC)
        }
        composable(ScreenRoute.Client.name) {
            InscriptionPage()
        }
    }

}