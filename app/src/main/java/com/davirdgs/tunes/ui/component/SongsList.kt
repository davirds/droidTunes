package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.R
import com.davirdgs.tunes.domain.models.Song
import com.davirdgs.tunes.ui.feature.home.SongItem

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    navbarPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues(),
    songs: List<Song>,
    onSongClick: (Song) -> Unit,
    onLoadMore: (() -> Unit)? = null
) {
    if (onLoadMore != null) {
        val reachedBottom: Boolean by remember { derivedStateOf { state.reachedBottom() } }
        LaunchedEffect(reachedBottom) {
            if (reachedBottom) onLoadMore()
        }
    }

    LazyColumn(
        modifier = modifier
            .testTag(stringResource(id = R.string.test_tag_songs_list))
            .fillMaxSize()
            .imePadding(),
        state = state,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 16.dp + navbarPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = songs,
            key = { song -> song.id }
        ) { song ->
            SongItem(
                name = song.name,
                artist = song.artist.name,
                artwork = song.artworkUrl,
                onClick = { onSongClick(song) }
            )
        }
    }
}

internal fun LazyListState.reachedBottom(buffer: Int = 5): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull() ?: return false
    return lastVisibleItem.index >= this.layoutInfo.totalItemsCount - 1 - buffer
}
