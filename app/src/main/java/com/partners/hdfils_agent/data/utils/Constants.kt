package com.partners.hdfils_agent.data.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.res.painterResource
import com.partners.hdfils_agent.domain.models.Route
import com.partners.hdfils_agent.R
import com.partners.hdfils_agent.domain.models.Commune
import com.partners.hdfils_agent.domain.route.ScreenRoute

class Constants {
    companion object {
        const val BASE_URL = "https://dummyjson.com/"
        const val HOST_DEV = "192.168.2.77:8023/api"
        const val HOST_PROD = "recolte.elieoko.com/api"
        const val IS_PROD = false
        const val TokenLocal = ""
        val routeList = listOf(
            Route(R.drawable.home, title = "Accueil", routeName = ScreenRoute.Home.name),
            Route(R.drawable.user_people, title = "Client", routeName = ScreenRoute.Client.name, position = 1),
            Route(R.drawable.trash_clean, title = "Vidage", routeName = ScreenRoute.Cleaner.name, position = 2),
            Route(R.drawable.user_, title = "Profil", routeName = ScreenRoute.Profil.name, position = 3)
        )
        const val API_INTERNET_MESSAGE="No Internet Connection"
        val listGenre = listOf("Mr(M)", "Mme(F)")
        val listVille = listOf("Kinshasa", "Boma", "Matadi")
        val listCommune = Commune().communeList()
        val listDevise = listOf("FC", "USD")
        val listTranche = listOf("1", "2", "3", "4", "6", "12")
    }
}