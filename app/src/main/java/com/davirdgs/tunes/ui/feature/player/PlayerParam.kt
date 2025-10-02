package com.davirdgs.tunes.ui.feature.player

import com.davirdgs.tunes.domain.models.Artist
import com.davirdgs.tunes.domain.models.Collection
import com.davirdgs.tunes.domain.models.Song

internal fun Song.toPlayerScreen() =
    Player(
        id = id,
        name = name,
        timeMillis = timeMillis,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        artistId = artist.id,
        artistName = artist.name,
        collectionId = collection.id,
        collectionName = collection.name
    )

internal fun Player.toSong(): Song =
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
