package com.davirdgs.tunes.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.feature.home.SongItem

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    navbarPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues(),
    songs: List<Song>,
    onSongClick: (Song) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 16.dp + navbarPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(songs) { song ->
            SongItem(
                name = song.name,
                artist = song.artist.name,
                artwork = song.artworkUrl,
                onClick = { onSongClick(song) }
            )
        }
    }
}
