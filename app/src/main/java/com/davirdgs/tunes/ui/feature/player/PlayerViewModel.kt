package com.davirdgs.tunes.ui.feature.player

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.toRoute
import com.davirdgs.tunes.domain.models.Song
import com.davirdgs.tunes.domain.repositories.TunesRepository
import com.davirdgs.tunes.player.PlayerExecutor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerExecutor: PlayerExecutor,
    private val tunesRepository: TunesRepository
) : ViewModel() {
    private val routeParam by lazy { savedStateHandle.toRoute(Player::class) }
    private var _uiState = MutableStateFlow(PlayerUiState(routeParam.toSong()))
    val uiState: StateFlow<PlayerUiState>
        get() = _uiState

    init {
        loadAlbumSongs()
        subscribePlayerEvents()
        startPlayer(_uiState.value.song)
    }

    fun playSong(song: Song) {
        _uiState.update { it.copy(song = song) }
        startPlayer(_uiState.value.song)
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
        val position = (_uiState.value.duration * percentage).toLong()
        playerExecutor.seekTo(position)
    }

    fun retry() {
        loadAlbumSongs()
    }

    private fun startPlayer(song: Song) {
        val mediaItem = song.toMediaItem()
        playerExecutor.startPlayer(mediaItem)
    }

    private fun loadAlbumSongs() {
        _uiState.update { it.copy(showAlbumLoading = true) }
        viewModelScope.launch {
            tunesRepository.getAlbum(_uiState.value.song.collection.id)
                .collectLatest { result ->
                    _uiState.update {
                        result.fold(
                            onSuccess = { album ->
                                it.copy(album = album, showAlbumLoading = false)
                            },
                            onFailure = { error ->
                                Log.d("PlayerViewModel", "loadAlbumSongs: $error")
                                it.copy(showAlbumLoading = false, showAlbumError = true)
                            }
                        )
                    }
                }
        }
    }

    private fun subscribePlayerEvents() {
        viewModelScope.launch {
            playerExecutor.mediaStateFlow.collectLatest { mediaState ->
                _uiState.update {
                    it.copy(
                        isPlaying = mediaState.isPlaying,
                        position = mediaState.position,
                        duration = mediaState.duration,
                        progress = mediaState.progress
                    )
                }
            }
        }
    }
}

internal data class PlayerUiState(
    val song: Song,
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val showPlayerLoading: Boolean = false,
    val showPlayerError: Boolean = false,
    override val album: List<Song> = emptyList(),
    override val showAlbumLoading: Boolean = false,
    override val showAlbumError: Boolean = false
) : AlbumUiState

internal interface AlbumUiState {
    val album: List<Song>
    val showAlbumLoading: Boolean
    val showAlbumError: Boolean
}

private fun Song.toMediaItem() = MediaItem.Builder()
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setTitle(name)
            .setArtworkUri(largeArtWorkUrl.toUri())
            .setArtist(artist.name)
            .setAlbumTitle(collection.name)
            .build()
    )
    .setUri(previewUrl)
    .build()
