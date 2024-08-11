package com.davirdgs.tunes.ui.feature.player

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.davirdgs.tunes.base.jsonParam
import com.davirdgs.tunes.data.TunesRepository
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.player.PlayerExecutor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
internal class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerExecutor: PlayerExecutor,
    private val tunesRepository: TunesRepository
) : ViewModel() {
    private val songParam by lazy { savedStateHandle.jsonParam<SongParam>(SONG_PARAM) }
    private var _uiState by mutableStateOf(PlayerUiState(songParam.toSong()))
    val uiState: PlayerUiState
        get() = _uiState

    init {
        loadAlbumSongs()
        subscribePlayerEvents()
        startPlayer(_uiState.song)
    }

    fun playSong(song: Song) {
        _uiState = _uiState.copy(song = song)
        startPlayer(_uiState.song)
    }

    fun play() {
        playerExecutor.play()
    }

    fun pause() {
        playerExecutor.pause()
    }

    fun forward() {
        playerExecutor.seekForward()
    }

    fun rewind() {
        playerExecutor.seekBack()
    }

    fun seekTo(percentage: Float) {
        val position = (_uiState.duration * percentage).toLong()
        playerExecutor.seekTo(position)
    }

    private fun startPlayer(song: Song) {
        val mediaItem = song.toMediaItem()
        playerExecutor.startPlayer(mediaItem)
    }

    private fun loadAlbumSongs() {
        _uiState = _uiState.copy(showAlbumLoading = true)
        viewModelScope.launch {
            tunesRepository.getAlbum(_uiState.song.collection.id)
                .collectLatest { result ->
                    _uiState = result.fold(
                        onSuccess = { album ->
                            _uiState.copy(album = album, showAlbumLoading = false)
                        },
                        onFailure = {
                            _uiState.copy(showAlbumLoading = false, showAlbumError = true)
                        }
                    )
                }
        }
    }

    private fun subscribePlayerEvents() {
        viewModelScope.launch {
            playerExecutor.mediaStateFlow.collectLatest { mediaState ->
                _uiState = _uiState.copy(
                    isPlaying = mediaState.isPlaying,
                    position = mediaState.position,
                    duration = mediaState.duration,
                    progress = mediaState.progress
                )
            }
        }
    }
}

internal data class PlayerUiState(
    val song: Song,
    val album: List<Song> = emptyList(),
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val showPlayerLoading: Boolean = false,
    val showPlayerError: Boolean = false,
    val showAlbumLoading: Boolean = false,
    val showAlbumError: Boolean = false
)

private fun Song.toMediaItem() = MediaItem.Builder()
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setTitle(name)
            .setArtworkUri(Uri.parse(largeArtWorkUrl))
            .setArtist(artist.name)
            .setAlbumTitle(collection.name)
            .build()
    )
    .setUri(previewUrl)
    .build()
