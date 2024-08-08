package com.davirdgs.tunes.ui.feature.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.ui.component.SongsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlbumBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetState: SheetState,
    albumName: String,
    artistName: String,
    album: List<Song>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom),
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = albumName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.size(8.dp))
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
                    onSongClick = {}
                )
            }
        }
    )
}
