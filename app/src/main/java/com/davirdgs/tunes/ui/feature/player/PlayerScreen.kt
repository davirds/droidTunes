package com.davirdgs.tunes.ui.feature.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val PLAYER_SCREEN = "player"

fun NavGraphBuilder.playerScreen() {
    composable(route = PLAYER_SCREEN) {
        PlayerScreen()
    }
}

@Composable
internal fun PlayerScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Player")
    }
}

@Preview
@Composable
private fun PlayerScreenPreview() {
    PlayerScreen()
}
