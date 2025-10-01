package com.davirdgs.tunes.domain

import com.davirdgs.tunes.data.remote.ApiService
import com.davirdgs.tunes.data.repositories.TunesRepositoryImpl
import com.davirdgs.tunes.domain.repositories.TunesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    internal fun provideTunesRepository(
        apiService: ApiService
    ): TunesRepository = TunesRepositoryImpl(apiService)
}