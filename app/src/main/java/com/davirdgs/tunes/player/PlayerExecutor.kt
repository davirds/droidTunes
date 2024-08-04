package com.davirdgs.tunes.player

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface PlayerExecutor {
    val mediaStateFlow: Flow<MediaState>
    fun startPlayer(mediaItem: MediaItem, startPositionMs: Long = 0L)
    fun play()
    fun pause()
    fun seekToNext()
    fun seekToPrevious()
    fun seekTo(position: Long)
    fun seekForward()
    fun seekBack()
    fun stop()
}

internal class PlayerExecutorImpl @Inject constructor(
    private val mediaControllerBuilder: MediaController.Builder
) : PlayerExecutor, Player.Listener {
    private val handler = Handler(Looper.getMainLooper())
    private val _mediaStateFlow = MutableStateFlow(MediaState())
    private var _mediaController: ListenableFuture<MediaController>? = null
    private var _player: Player? = null

    private val currentPositionRunnable = Runnable {
        _player?.run {
            if (isPlaying) {
                val progress = calculateProgress(currentPosition, duration)
                _mediaStateFlow.update {
                    it.copy(position = currentPosition, progress = progress)
                }
                startCurrentPositionLoop()
            }
        }
    }

    override val mediaStateFlow: Flow<MediaState> = _mediaStateFlow

    private fun setupMediaController() {
        if (_mediaController == null) {
            _mediaController = mediaControllerBuilder.buildAsync()
        }
    }

    override fun startPlayer(mediaItem: MediaItem, startPositionMs: Long) {
        setupMediaController()
        _mediaController?.use { controller ->
            controller.removeListener(this@PlayerExecutorImpl)
            _player = controller
            controller.addListener(this@PlayerExecutorImpl)
            controller.playWhenReady = true
            controller.setMediaItem(mediaItem, startPositionMs)
            _mediaStateFlow.update {
                it.copy(isActive = true, progress = 0f, position = 0L, duration = 0L)
            }
            controller.prepare()
        }
    }

    override fun play() {
        _player?.run { play() }
    }

    override fun pause() {
        _player?.run { pause() }
    }

    override fun seekToNext() {
        _player?.run { seekToNext() }
    }

    override fun seekToPrevious() {
        _player?.run { seekToPrevious() }
    }

    override fun seekForward() {
        _player?.run {
            val position = currentPosition + 5000
            val progress = calculateProgress(position, duration)
            _mediaStateFlow.update { it.copy(position = position, progress = progress) }
            seekTo(position)
        }
    }

    override fun seekBack() {
        _player?.run {
            val position = currentPosition - 5000
            val progress = calculateProgress(position, duration)
            _mediaStateFlow.update { it.copy(position = position, progress = progress) }
            seekTo(position)
        }
    }

    override fun stop() {
        handler.removeCallbacks(currentPositionRunnable)
        _mediaStateFlow.update { it.copy(isActive = false) }
        _player?.run {
            clearMediaItems()
            stop()
            removeListener(this@PlayerExecutorImpl)
        }
    }

    override fun seekTo(position: Long) {
        _mediaStateFlow.update {
            val progress = calculateProgress(position, it.duration)
            it.copy(position = position, progress = progress)
        }
        _player?.run { seekTo(position) }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        if (events.contains(Player.EVENT_MEDIA_METADATA_CHANGED)) {
            _mediaStateFlow.update {
                it.copy(
                    metadata = player.mediaMetadata,
                    duration = player.duration.coerceAtLeast(0L)
                )
            }
        }

        if (events.contains(Player.EVENT_IS_LOADING_CHANGED)) {
            _mediaStateFlow.update {
                it.copy(
                    isLoading = player.isLoading
                )
            }
        }

        if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
            val isPlaying = player.isPlaying
            val duration = player.duration.coerceAtLeast(0L)
            val isActive = player.playbackState != Player.STATE_ENDED
            if (isPlaying) {
                startCurrentPositionLoop()
                _mediaStateFlow.update {
                    it.copy(isPlaying = true, isLoading = false, duration = duration)
                }
            } else {
                _mediaStateFlow.update {
                    it.copy(isPlaying = false, duration = duration, isActive = isActive)
                }
            }
        }
    }

    private fun startCurrentPositionLoop() {
        handler.removeCallbacks(currentPositionRunnable)
        handler.postDelayed(currentPositionRunnable, 1000)
    }
}

data class MediaState(
    val metadata: MediaMetadata = MediaMetadata.EMPTY,
    val position: Long = 0L,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val isActive: Boolean = false
) {
    val completionProgress: Int
        get() = (progress * 100).toInt()
}

private fun ListenableFuture<MediaController>.use(
    executor: Executor = MoreExecutors.directExecutor(),
    exec: (MediaController) -> Unit
) {
    addListener({
        exec(this.get())
    }, executor)
}

private fun calculateProgress(
    position: Long,
    duration: Long
): Float {
    val positionInSec = position / 1000f
    val durationInSec = duration / 1000f
    return if (position <= 0L || duration <= 0L) {
        0f
    } else {
        (positionInSec / durationInSec)
    }
}
