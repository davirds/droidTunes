package com.davirdgs.tunes.ui.feature.home

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.davirdgs.tunes.data.model.Artist
import com.davirdgs.tunes.data.model.Collection
import com.davirdgs.tunes.data.model.Song

const val HOME_SCREEN = "home"

fun NavGraphBuilder.homeScreen() {
    composable(route = HOME_SCREEN) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(
            uiState = viewModel.uiState,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch
        )
    }
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TextField(
            modifier = Modifier
                .statusBarsPadding()
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
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.songs) { song ->
                SongItem(
                    song = song
                )
            }
        }
    }
}

@Composable
private fun SongItem(
    modifier: Modifier = Modifier,
    song: Song
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(48.dp)
                .clip(RoundedCornerShape(4.dp)),
            painter = rememberAsyncImagePainter(model = song.artworkUrl),
            contentScale = ContentScale.Fit,
            contentDescription = "Song Artwork"
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = song.name)
            Text(text = "by ${song.artist.name}")
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
            collection = Collection(
                id = 0,
                name = "Some"
            )
        )
    )
    HomeScreen(
        uiState = HomeUiState(songs = songs),
        onQueryChange = { },
        onSearch = { }
    )
}
