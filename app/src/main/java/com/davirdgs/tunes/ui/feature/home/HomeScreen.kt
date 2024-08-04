package com.davirdgs.tunes.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_SCREEN = "home"

fun NavGraphBuilder.homeScreen() {
    composable(route = HOME_SCREEN) {
        HomeScreen()
    }
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "HOME")
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}
