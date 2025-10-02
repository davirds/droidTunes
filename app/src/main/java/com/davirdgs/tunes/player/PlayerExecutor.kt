package com.davirdgs.tunes.player
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface PlayerExecutor {
    val mediaStateFlow: StateFlow<MediaState>
    fun startPlayer(mediaItem: MediaItem, startPositionMs: Long = 0L)
    fun play()
    fun pause()
    fun seekTo(position: Long)
    fun seekForward()
    fun seekBack()
    fun stop()
    fun release()
}

private const val SEEK_INTERVAL_MS = 5000L

internal class PlayerExecutorImpl @Inject constructor(
    mediaControllerBuilder: MediaController.Builder
) : PlayerExecutor, Player.Listener {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var positionUpdateJob: Job? = null
    private val mediaControllerFuture: ListenableFuture<MediaController> =
        mediaControllerBuilder.buildAsync()
    private var player: Player? = null

    private val _mediaStateFlow = MutableStateFlow(MediaState())
    override val mediaStateFlow: StateFlow<MediaState>
        get() = _mediaStateFlow.asStateFlow()

    override fun startPlayer(mediaItem: MediaItem, startPositionMs: Long) {
        mediaControllerFuture.use { controller ->
            controller.removeListener(this@PlayerExecutorImpl)
            player = controller
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
        player?.run { play() }
    }

    override fun pause() {
        player?.run { pause() }
    }

    override fun seekForward() {
        player?.run {
            val position = currentPosition + SEEK_INTERVAL_MS
            seekTo(position)
        }
    }

    override fun seekBack() {
        player?.run {
            val position = currentPosition - SEEK_INTERVAL_MS
            seekTo(position)
        }
    }

    override fun seekTo(position: Long) {
        player?.run { seekTo(position) }
    }

    override fun stop() {
        stopCurrentPositionLoop()
        _mediaStateFlow.update { it.copy(isActive = false) }
        player?.run {
            clearMediaItems()
            stop()
            removeListener(this@PlayerExecutorImpl)
        }
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
                stopCurrentPositionLoop()
                _mediaStateFlow.update {
                    it.copy(isPlaying = false, duration = duration, isActive = isActive)
                }
            }
        }
    }

    private fun startCurrentPositionLoop() {
        stopCurrentPositionLoop()
        positionUpdateJob = scope.launch {
            while (isActive) {
                Log.d("AAAA", "startCurrentPositionLoop")
                player?.let {
                    _mediaStateFlow.update { state ->
                        state.copy(
                            position = it.currentPosition,
                            progress = calculateProgress(it.currentPosition, it.duration)
                        )
                    }
                }
                delay(1000)
            }
        }
    }

    private fun stopCurrentPositionLoop() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    override fun release() {
        stopCurrentPositionLoop()
        scope.cancel()
        player?.removeListener(this)
        MediaController.releaseFuture(mediaControllerFuture)
        player = null
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
)

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
