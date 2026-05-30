package com.entrelacos.arandu.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.entrelacos.arandu.ui.components.BottomNavBar
import com.entrelacos.arandu.ui.tabs.CommunityTab
import com.entrelacos.arandu.ui.tabs.OpportunitiesTab
import com.entrelacos.arandu.ui.tabs.ProfileTab
import com.entrelacos.arandu.ui.tabs.SensoryTab

@Composable
fun MainScreen(
    navController: NavController
) {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    Scaffold(

        containerColor = Color(0xFFFDE7EF),

        bottomBar = {

            BottomNavBar(
                selectedIndex = selectedTab,
                onItemSelected = {
                    selectedTab = it
                }
            )
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when (selectedTab) {

                0 -> CommunityTab(navController)

                1 -> OpportunitiesTab()

                2 -> SensoryTab()

                3 -> ProfileTab(
                    navController = navController
                )
            }
        }
    }
}