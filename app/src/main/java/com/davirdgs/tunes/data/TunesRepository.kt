package com.davirdgs.tunes.data

import com.davirdgs.tunes.data.model.Artist
import com.davirdgs.tunes.data.model.Collection
import com.davirdgs.tunes.data.model.Song
import com.davirdgs.tunes.data.remote.ApiService
import com.davirdgs.tunes.data.remote.model.BaseResponse
import com.davirdgs.tunes.data.remote.model.Track
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TunesRepository {
    fun searchSongs(query: String, offset: Int = 0, limit: Int = 25): Flow<Result<List<Song>>>
    fun getAlbum(albumId: Int): Flow<Result<List<Song>>>
}

internal class TunesRepositoryImpl @Inject constructor(
    private val remoteSource: ApiService
) : TunesRepository {

    override fun searchSongs(query: String, offset: Int, limit: Int) = flow {
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

private fun BaseResponse<Track>.toSongList(): List<Song> =
    results
        .filter { it.kind == "song" }
        .map(Track::toSong)

private fun Track.toSong() =
    Song(
        artist = toArtist(),
        collection = toCollection(),
        artworkUrl = artworkUrl100,
        id = trackId!!,
        name = trackName!!,
        timeMillis = trackTimeMillis!!,
        previewUrl = previewUrl!!
    )

private fun Track.toArtist() =
    Artist(
        id = artistId,
        name = artistName
    )

private fun Track.toCollection() =
    Collection(
        id = collectionId,
        name = collectionName
    )
