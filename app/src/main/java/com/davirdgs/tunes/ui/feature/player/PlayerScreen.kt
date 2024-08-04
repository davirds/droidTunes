package com.davirdgs.tunes.ui.feature.player

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.davirdgs.tunes.data.model.Artist
import com.davirdgs.tunes.data.model.Collection
import com.davirdgs.tunes.data.model.Song

internal const val SONG_PARAM = "song"
private const val PLAYER_PATH = "player"
private const val PLAYER_SCREEN = "$PLAYER_PATH?$SONG_PARAM={$SONG_PARAM}"

fun NavController.navigateToPlayer(song: Song) {
    val songParam = song.toSongParam()
    Log.d("APP", "Selected song: $songParam")
    this.navigate("$PLAYER_PATH?$SONG_PARAM=$songParam")
}

fun NavGraphBuilder.playerScreen() {
    composable(route = PLAYER_SCREEN) {
        val viewModel = hiltViewModel<PlayerViewModel>()
        PlayerScreen(
            uiState = viewModel.uiState
        )
    }
}

@Composable
internal fun PlayerScreen(
    modifier: Modifier = Modifier,
    uiState: PlayerUiState
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp),
            painter = rememberAsyncImagePainter(model = uiState.song.artworkUrl),
            contentDescription = "Artwork"
        )
    }
}

@Preview
@Composable
private fun PlayerScreenPreview() {
    val song = Song(
        id = 0,
        name = "Something",
        artworkUrl = "",
        artist = Artist(
            id = 0,
            name = "Someone"
        ),
        timeMillis = 0,
        previewUrl = "",
        collection = Collection(
            id = 0,
            name = "Some"
        )
    )

    PlayerScreen(
        uiState = PlayerUiState(song = song)
    )
}
