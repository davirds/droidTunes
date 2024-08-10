package com.davirdgs.tunes.ui.feature.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.component.SongsList
import com.davirdgs.tunes.ui.songsMock
import com.davirdgs.tunes.ui.theme.AppTheme

@Composable
internal fun AlbumContent(
    modifier: Modifier = Modifier,
    albumName: String,
    artistName: String,
    album: List<Song>,
    onPlaySong: (Song) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = albumName,
            style = MaterialTheme.typography.labelMedium.copy(
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.labelSmall.copy(
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.size(16.dp))
        SongsList(
            modifier = Modifier.fillMaxSize(),
            songs = album,
            onSongClick = onPlaySong
        )
    }
}

@Preview
@Composable
private fun AlbumContentPreview() {
    AppTheme {
        AlbumContent(
            albumName = "Something",
            artistName = "Someone",
            album = songsMock(10),
            onPlaySong = {}
        )
    }
}
