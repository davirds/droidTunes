package com.davirdgs.tunes.domain.repositories

import com.davirdgs.tunes.domain.models.Artist
import com.davirdgs.tunes.domain.models.Collection
import com.davirdgs.tunes.domain.models.Song
import com.davirdgs.tunes.data.remote.ApiService
import com.davirdgs.tunes.data.models.BaseResponse
import com.davirdgs.tunes.data.models.Track
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TunesRepository {
    fun searchSongs(query: String, offset: Int, limit: Int): Flow<Result<List<Song>>>
    fun getAlbum(albumId: Int): Flow<Result<List<Song>>>
}
