package com.davirdgs.tunes.ui.feature.player

import com.davirdgs.tunes.base.toJson
import com.davirdgs.tunes.data.model.Artist
import com.davirdgs.tunes.data.model.Collection
import com.davirdgs.tunes.data.model.Song
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SongParam(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("time_millis")
    val timeMillis: Int,
    @SerialName("artwork_url")
    val artworkUrl: String,
    @SerialName("artist_id")
    val artistId: Int,
    @SerialName("artist_name")
    val artistName: String,
    @SerialName("collection_id")
    val collectionId: Int,
    @SerialName("collection_name")
    val collectionName: String,
    @SerialName("preview_url")
    val previewUrl: String
)

internal fun Song.toSongParam(): String =
    SongParam(
        id = id,
        name = name,
        timeMillis = timeMillis,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        artistId = artist.id,
        artistName = artist.name,
        collectionId = collection.id,
        collectionName = collection.name
    ).toJson()

internal fun SongParam.toSong(): Song =
    Song(
        id = id,
        name = name,
        timeMillis = timeMillis,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        artist = Artist(
            id = artistId,
            name = artistName
        ),
        collection = Collection(
            id = collectionId,
            name = collectionName
        )
    )
