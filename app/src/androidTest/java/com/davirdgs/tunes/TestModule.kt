package com.davirdgs.tunes

import com.davirdgs.tunes.data.ServiceModule
import com.davirdgs.tunes.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

internal val apiServiceMock = mockk<ApiService>(relaxed = true)

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ServiceModule::class]
)
object TestModule {

    @Singleton
    @Provides
    internal fun provideService(): ApiService = apiServiceMock
}
