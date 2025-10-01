package com.davirdgs.tunes.data.mappers

import com.davirdgs.tunes.data.models.BaseResponse
import com.davirdgs.tunes.data.models.Track
import com.davirdgs.tunes.domain.models.Artist
import com.davirdgs.tunes.domain.models.Collection
import com.davirdgs.tunes.domain.models.Song


internal fun BaseResponse<Track>.toSongList(): List<Song> =
    results
        .filter { it.kind == "song" }
        .map(Track::toSong)

internal fun Track.toSong() =
    Song(
        artist = toArtist(),
        collection = toCollection(),
        artworkUrl = artworkUrl100,
        id = trackId!!,
        name = trackName!!,
        timeMillis = trackTimeMillis!!,
        previewUrl = previewUrl!!
    )

internal fun Track.toArtist() =
    Artist(
        id = artistId,
        name = artistName
    )

internal fun Track.toCollection() =
    Collection(
        id = collectionId,
        name = collectionName
    )
