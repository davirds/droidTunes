package com.davirdgs.tunes.ui.feature.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.davirdgs.tunes.R
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.component.PlayerSeekProgress
import com.davirdgs.tunes.ui.songMock
import com.davirdgs.tunes.ui.theme.AppTheme

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
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val viewModel = hiltViewModel<PlayerViewModel>()
        PlayerScreen(
            uiState = viewModel.uiState,
            bottomSheetState = bottomSheetState,
            onNavigateBack = navigateBack,
            onOpenAlbum = viewModel::openAlbumBottomSheet,
            onCloseAlbum = viewModel::closeAlbumBottomSheet,
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
    bottomSheetState: SheetState,
    uiState: PlayerUiState,
    onNavigateBack: () -> Unit,
    onOpenAlbum: () -> Unit,
    onCloseAlbum: () -> Unit,
    onSeekChange: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit,
) {
    Column(
        modifier = modifier.statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerHeader(
            onBackClick = onNavigateBack,
            onOptionClick = onOpenAlbum,
        )
        Box(modifier = Modifier.weight(0.5f))
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(model = uiState.song.artworkUrl),
            contentDescription = "Artwork"
        )
        Box(modifier = Modifier.weight(0.5f))
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
        Box(modifier = Modifier.size(64.dp))
    }

    if (uiState.showBottomSheet) {
        AlbumBottomSheet(
            modifier = Modifier,
            bottomSheetState = bottomSheetState,
            albumName = uiState.song.collection.name,
            artistName = uiState.song.artist.name,
            album = uiState.album,
            onDismiss = onCloseAlbum
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
            imageVector = Icons.Filled.MoreVert,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = "options"
        )
    }
}

@Composable
private fun PlayerController(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String,
    position: Long,
    duration: Long,
    progress: Float,
    isPlaying: Boolean,
    onSeekChange: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onForward: () -> Unit,
    onBackward: () -> Unit
) {
    val playerAction = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
    val playerActionDescription = if (isPlaying) "pause" else "play"
    val playerActionClick = if (isPlaying) onPause else onPlay

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = songName,
            style = MaterialTheme.typography.labelLarge.copy(
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        )
        PlayerSeekProgress(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            backgroundColor = MaterialTheme.colorScheme.surface,
            progressColor = MaterialTheme.colorScheme.onSurface,
            thumbColor = MaterialTheme.colorScheme.onBackground,
            position = position,
            duration = duration,
            progress = progress,
            onSeekChange = onSeekChange
        )
        Spacer(modifier = Modifier.size(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onBackward() }
                    .padding(8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_backward),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = "backward"
            )
            Image(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable { playerActionClick() },
                painter = painterResource(id = playerAction),
                contentDescription = playerActionDescription
            )
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable { onForward() }
                    .padding(8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_forward),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = "forward"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PlayerScreenPreview() {
    val song = songMock()
    AppTheme {
        PlayerScreen(
            bottomSheetState = rememberModalBottomSheetState(),
            uiState = PlayerUiState(song = song),
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
