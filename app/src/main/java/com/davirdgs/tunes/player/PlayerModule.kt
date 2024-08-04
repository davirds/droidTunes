package com.davirdgs.tunes.player

import android.content.ComponentName
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(UnstableApi::class)
@Module
@InstallIn(ServiceComponent::class)
object PlayerServiceModule {

    @Provides
    internal fun providesDataSourceFactory(): DataSource.Factory =
        DefaultHttpDataSource.Factory()

    @Provides
    internal fun providesMediaSourceFactory(
        dataSourceFactory: DataSource.Factory
    ): MediaSource.Factory =
        ProgressiveMediaSource.Factory(dataSourceFactory)

    @Provides
    internal fun providesPlayer(
        @ApplicationContext context: Context,
        mediaSourceFactory: MediaSource.Factory
    ): Player = ExoPlayer.Builder(context)
        .setMediaSourceFactory(mediaSourceFactory)
        .build()

    @ServiceScoped
    @Provides
    internal fun providesMediaSession(
        @ApplicationContext context: Context,
        player: Player
    ): MediaSession {
        return MediaSession.Builder(context, player)
            .build()
    }
}

@OptIn(UnstableApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Singleton
    @Provides
    internal fun provideMediaController(
        @ApplicationContext context: Context,
    ): MediaController.Builder {
        val sessionToken = SessionToken(context, ComponentName(context, PlayerService::class.java))
        return MediaController.Builder(context, sessionToken)
    }

    @Singleton
    @Provides
    fun providePlayerExecutor(
        mediaControllerBuilder: MediaController.Builder
    ): PlayerExecutor = PlayerExecutorImpl(mediaControllerBuilder)
}
