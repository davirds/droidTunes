package com.davirdgs.tunes.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.component.SearchField
import com.davirdgs.tunes.ui.component.SongsList
import com.davirdgs.tunes.ui.songsMock
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
        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = uiState.query,
            onValueChange = onQueryChange,
            onSearch = onSearch,
            maxLines = 1,
        )
        if (uiState.songs.isEmpty()) {
            EmptyState()
        } else {
            SongsList(
                songs = uiState.songs,
                onSongClick = onSongClick
            )
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.6f)
                .padding(bottom = 120.dp),
            text = "No songs found yet",
            style = MaterialTheme.typography.labelMedium.copy(
                lineBreak = LineBreak.Heading,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val songs = songsMock(5)
    AppTheme {
        HomeScreen(
            uiState = HomeUiState(songs = songs),
            onQueryChange = { },
            onSearch = { },
            onSongClick = { }
        )
    }
}
