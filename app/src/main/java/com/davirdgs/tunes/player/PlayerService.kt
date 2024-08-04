package com.davirdgs.tunes.player

import android.content.Intent
import android.os.Process
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession.run {
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
            release()
        }
        stopSelf()
        // TODO Force the process to die
        Process.killProcess(Process.myPid())
    }
}
