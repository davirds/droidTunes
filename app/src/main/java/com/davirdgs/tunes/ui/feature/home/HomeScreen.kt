package com.davirdgs.tunes.ui.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.davirdgs.tunes.data.model.Artist
import com.davirdgs.tunes.data.model.Collection
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.theme.AppTheme

const val HOME_SCREEN = "home"

fun NavGraphBuilder.homeScreen(
    navigateToPlayer: (Song) -> Unit
) {
    composable(route = HOME_SCREEN) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(
            uiState = viewModel.uiState,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch,
            onSongClick = navigateToPlayer
        )
    }
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSongClick: (Song) -> Unit
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            text = "Songs",
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            maxLines = 1,
            value = uiState.query,
            onValueChange = onQueryChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() })
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(uiState.songs) { song ->
                SongItem(
                    name = song.name,
                    artist = song.artist.name,
                    artwork = song.artworkUrl,
                    onClick = { onSongClick(song) }
                )
            }
        }
    }
}

@Composable
private fun SongItem(
    modifier: Modifier = Modifier,
    name: String,
    artist: String,
    artwork: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(model = artwork),
            contentDescription = "Song Artwork"
        )
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val songs = listOf(
        Song(
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
        ),
        Song(
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
        ),
        Song(
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
    )
    AppTheme {
        HomeScreen(
            uiState = HomeUiState(songs = songs),
            onQueryChange = { },
            onSearch = { },
            onSongClick = { }
        )
    }
}
