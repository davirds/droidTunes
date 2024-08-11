package com.davirdgs.tunes.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.davirdgs.tunes.R
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.component.EmptyState
import com.davirdgs.tunes.ui.component.ErrorState
import com.davirdgs.tunes.ui.component.LoadingState
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
            onRetry = viewModel::onSearch,
            onLoadMore = viewModel::loadMore,
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
    onSongClick: (Song) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
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
            text = stringResource(id = R.string.home_title),
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
        when {
            uiState.songs.isNotEmpty() -> SongsList(
                songs = uiState.songs,
                onSongClick = onSongClick,
                onLoadMore = onLoadMore
            )
            uiState.showLoading -> LoadingState()
            uiState.showError -> ErrorState(onRetry = onRetry)
            else -> EmptyState(message = stringResource(id = R.string.home_empty_title))
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val songs = songsMock(0)
    AppTheme {
        HomeScreen(
            uiState = HomeUiState(songs = songs, showError = true),
            onQueryChange = { },
            onSearch = { },
            onRetry = { },
            onLoadMore = { },
            onSongClick = { },
        )
    }
}
