package com.davirdgs.tunes.domain.repositories

import com.davirdgs.tunes.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface TunesRepository {
    fun searchSongs(query: String, offset: Int, limit: Int): Flow<Result<List<Song>>>
    fun getAlbum(albumId: Int): Flow<Result<List<Song>>>
}
