package com.partners.hdfils_agent.presentation.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.partners.hdfils_agent.domain.models.Route
import com.partners.hdfils_agent.domain.route.ScreenRoute
import com.partners.hdfils_agent.presentation.ui.components.ClientStatsCardWithChart
import com.partners.hdfils_agent.presentation.ui.components.Space
import com.partners.hdfils_agent.presentation.ui.components.WasteCollectionStatsCard

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePageMain(navC: NavHostController) {
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    var page by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            Card() {
                NavigationBar(
                ){
                    Route().bottomNavigationItems().forEachIndexed {index,navigationItem ->
                        NavigationBarItem(
                            selected = index == navigationSelectedItem,
                            label = {
                                Text(navigationItem.title)
                            },
                            icon = {
                                Icon(
                                    painterResource(navigationItem.icon),
                                    modifier = Modifier.size(25.dp),
                                    contentDescription = navigationItem.title
                                )
                            },
                            onClick = {
                            navigationSelectedItem = index
                            navC.navigate(navigationItem.routeName) {
                                popUpTo(navC.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {

    }
}

@Composable
fun HomePage(navC: NavHostController) {
    Column {
        Space(y = 30)
        ClientStatsCardWithChart()
        WasteCollectionStatsCard()
    }
}