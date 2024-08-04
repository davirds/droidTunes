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
    fun searchSongs(): Flow<List<Song>>
}

internal class TunesRepositoryImpl @Inject constructor(
    private val remoteSource: ApiService
) : TunesRepository {

    override fun searchSongs() = flow {
        try {
            val response = remoteSource.search(
                term = "Twenty one pilots",
                limit = 25,
                offset = 0
            )
            emit(response.toSongList())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

private fun BaseResponse<Track>.toSongList(): List<Song> =
    results.map(Track::toSongList)

private fun Track.toSongList() =
    Song(
        artist = toArtist(),
        collection = toCollection(),
        artworkUrl = artworkUrl100,
        id = trackId,
        name = trackName,
        timeMillis = trackTimeMillis,
        previewUrl = previewUrl
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
