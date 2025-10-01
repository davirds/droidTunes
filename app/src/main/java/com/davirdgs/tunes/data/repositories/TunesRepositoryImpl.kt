package com.davirdgs.tunes.data.repositories

import com.davirdgs.tunes.data.mappers.toSongList
import com.davirdgs.tunes.data.remote.ApiService
import com.davirdgs.tunes.domain.repositories.TunesRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class TunesRepositoryImpl @Inject constructor(
    private val remoteSource: ApiService
) : TunesRepository {

    override fun searchSongs(
        query: String,
        offset: Int,
        limit: Int
    ) = flow {
        val result = runCatching {
            val response = remoteSource.search(
                term = query,
                offset = offset,
                limit = limit
            )
            response.toSongList()
        }
        emit(result)
    }

    override fun getAlbum(albumId: Int) = flow {
        val result = runCatching {
            val response = remoteSource.lookup(id = albumId)
            response.toSongList()
        }
        emit(result)
    }
}