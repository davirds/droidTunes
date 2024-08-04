package com.davirdgs.tunes.ui.feature.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val ALBUM_SCREEN = "album"

fun NavGraphBuilder.albumScreen() {
    composable(route = ALBUM_SCREEN) {
        AlbumScreen()
    }
}

@Composable
internal fun AlbumScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Album")
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    AlbumScreen()
}
