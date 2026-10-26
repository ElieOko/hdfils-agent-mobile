package com.partners.hdfils_agent.presentation.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.partners.hdfils_agent.domain.models.Route
import com.partners.hdfils_agent.presentation.ui.components.ClientStatsCardWithChart
import com.partners.hdfils_agent.presentation.ui.components.Space
import com.partners.hdfils_agent.presentation.ui.components.TopBar
import com.partners.hdfils_agent.presentation.ui.components.WasteCollectionStatsCard
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlue
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueDeep
import com.partners.hdfils_agent.presentation.ui.theme.BrandBlueLight
import com.partners.hdfils_agent.presentation.ui.theme.InkMuted
import com.partners.hdfils_agent.presentation.ui.theme.Line

@Composable
fun HomePageMain(navC: NavHostController, isConnected: Boolean) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    val keyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (!keyboardOpen) {
                AppBottomBar(
                    selectedIndex = navigationSelectedItem,
                    onSelect = { navigationSelectedItem = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .imePadding()
                .fillMaxSize()
        ) {
            when (navigationSelectedItem) {
                0 -> HomePage(isConnected)
                1 -> InscriptionPage(isConnected)
                2 -> CheckBopeto(isConnected)
                3 -> ProfilePage(navC, isConnected)
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    Column {
        HorizontalDivider(color = Line, thickness = 1.dp)
        NavigationBar(
            containerColor = Color.White,
            contentColor = InkMuted,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets.navigationBars
        ) {
            Route().bottomNavigationItems().forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    icon = {
                        Icon(
                            painter = painterResource(navigationItem.icon),
                            contentDescription = navigationItem.title,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = navigationItem.title,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandBlue,
                        selectedTextColor = BrandBlueDeep,
                        indicatorColor = BrandBlueLight,
                        unselectedIconColor = InkMuted,
                        unselectedTextColor = InkMuted
                    )
                )
            }
        }
    }
}

@Composable
fun HomePage(isConnected: Boolean) {
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(isConnected = isConnected)
        Space(y = 4)
        ClientStatsCardWithChart()
        WasteCollectionStatsCard()
    }
}