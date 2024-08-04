package com.davirdgs.tunes.data

import com.davirdgs.tunes.data.remote.ApiService
import com.davirdgs.tunes.data.remote.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface TunesRepository {
    fun search(): Flow<List<Track>>
}

internal class TunesRepositoryImpl @Inject constructor(
    private val remoteSource: ApiService
) : TunesRepository {

    override fun search() = flow {
        try {
            val response = remoteSource.search(
                term = "Twenty one pilots",
                limit = 25,
                offset = 0
            )
            emit(response.results)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
