package com.davirdgs.tunes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.davirdgs.tunes.ui.feature.album.albumScreen
import com.davirdgs.tunes.ui.feature.home.HOME_SCREEN
import com.davirdgs.tunes.ui.feature.home.homeScreen
import com.davirdgs.tunes.ui.feature.player.navigateToPlayer
import com.davirdgs.tunes.ui.feature.player.playerScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = HOME_SCREEN,
    ) {
        homeScreen(
            navigateToPlayer = navHostController::navigateToPlayer
        )

        playerScreen()

        albumScreen()
    }
}
