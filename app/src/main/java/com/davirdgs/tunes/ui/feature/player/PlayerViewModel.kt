package com.davirdgs.tunes.ui.feature.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.davirdgs.tunes.base.jsonParam
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.player.PlayerExecutor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerExecutor: PlayerExecutor
) : ViewModel() {
    private val songParam by lazy { savedStateHandle.jsonParam<SongParam>(SONG_PARAM) }
    private var _uiState by mutableStateOf(PlayerUiState(songParam))
    val uiState: PlayerUiState
        get() = _uiState

    init {
        val mediaItem = MediaItem.fromUri(_uiState.song.previewUrl)
        playerExecutor.startPlayer(mediaItem, 0L)
    }
}

internal data class PlayerUiState(
    val song: Song,
    val showLoading: Boolean = false,
    val showError: Boolean = false
) {
    constructor(songParam: SongParam) : this(song = songParam.toSong())
}
