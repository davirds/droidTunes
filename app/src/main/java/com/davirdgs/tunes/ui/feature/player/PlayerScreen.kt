package com.davirdgs.tunes.ui.feature.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.songMock
import com.davirdgs.tunes.ui.theme.AppTheme
import kotlinx.coroutines.launch

internal const val SONG_PARAM = "song"
private const val PLAYER_PATH = "player"
private const val PLAYER_SCREEN = "$PLAYER_PATH?$SONG_PARAM={$SONG_PARAM}"

fun NavController.navigateToPlayer(song: Song) {
    val songParam = song.toSongParam()
    this.navigate("$PLAYER_PATH?$SONG_PARAM=$songParam")
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.playerScreen(
    navigateBack: () -> Unit,
) {
    composable(route = PLAYER_SCREEN) {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(
                skipHiddenState = false,
                initialValue = SheetValue.Hidden
            )
        )
        val viewModel = hiltViewModel<PlayerViewModel>()
        PlayerScreen(
            uiState = viewModel.uiState,
            scaffoldState = scaffoldState,
            onNavigateBack = navigateBack,
            onOpenAlbum = { scope.launch { scaffoldState.bottomSheetState.expand() } },
            onCloseAlbum = { scope.launch { scaffoldState.bottomSheetState.hide() } },
            onPLaySong = { song ->
                scope.launch { scaffoldState.bottomSheetState.hide() }
                viewModel.playSong(song)
            },
            onSeekChange = viewModel::seekTo,
            onPlay = viewModel::play,
            onPause = viewModel::pause,
            onForward = viewModel::forward,
            onBackward = viewModel::rewind
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerScreen(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    uiState: PlayerUiState,
    onNavigateBack: () -> Unit,
    onOpenAlbum: () -> Unit,
    onCloseAlbum: () -> Unit,
    onPLaySong: (Song) -> Unit,
    onSeekChange: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
) {
    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            PlayerHeader(
                modifier = Modifier.statusBarsPadding(),
                onBackClick = onNavigateBack,
                onOptionClick = onOpenAlbum,
            )
        },
        content = { padding ->
            PlayerContent(
                modifier = Modifier.padding(padding),
                uiState = uiState,
                onSeekChange = onSeekChange,
                onPlay = onPlay,
                onPause = onPause,
                onForward = onForward,
                onBackward = onBackward,
            )
        },
        sheetShadowElevation = 16.dp,
        sheetContentColor = MaterialTheme.colorScheme.onBackground,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.surface)
        },
        sheetContent = {
            AlbumContent(
                albumName = uiState.song.collection.name,
                artistName = uiState.song.artist.name,
                album = uiState.album,
                onPlaySong = onPLaySong
            )
        }
    )
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    uiState: PlayerUiState,
    onSeekChange: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(0.6f))
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(model = uiState.song.largeArtWorkUrl),
            contentDescription = "Artwork"
        )
        Spacer(modifier = Modifier.weight(0.4f))
        PlayerController(
            modifier = Modifier,
            songName = uiState.song.name,
            artistName = uiState.song.artist.name,
            position = uiState.position,
            duration = uiState.duration,
            progress = uiState.progress,
            isPlaying = uiState.isPlaying,
            onSeekChange = onSeekChange,
            onPlay = onPlay,
            onPause = onPause,
            onForward = onForward,
            onBackward = onBackward
        )
        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .size(32.dp)
        )
    }
}

@Composable
private fun PlayerHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { onBackClick() }
                .padding(8.dp)
                .size(32.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "back"
        )
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable { onOptionClick() }
                .padding(8.dp)
                .size(32.dp),
            imageVector = Icons.AutoMirrored.Filled.List,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "options"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PlayerScreenPreview() {
    val song = songMock()
    AppTheme {
        PlayerScreen(
            scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberStandardBottomSheetState(
                    initialValue = SheetValue.Hidden,
                    skipHiddenState = false
                )
            ),
            uiState = PlayerUiState(song = song),
            onPLaySong = { },
            onNavigateBack = { },
            onOpenAlbum = { },
            onCloseAlbum = { },
            onSeekChange = { },
            onPlay = { },
            onPause = { },
            onForward = { },
            onBackward = { }
        )
    }
}
