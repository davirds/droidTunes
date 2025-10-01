package com.davirdgs.tunes.ui

import com.davirdgs.tunes.domain.models.Artist
import com.davirdgs.tunes.domain.models.Collection
import com.davirdgs.tunes.domain.models.Song

internal fun songMock(id: Int = 0) = Song(
    id = id,
    name = "Something $id",
    artworkUrl = "",
    artist = Artist(
        id = id,
        name = "Someone"
    ),
    timeMillis = 0,
    previewUrl = "",
    collection = Collection(
        id = id,
        name = "Some"
    )
)

internal fun songsMock(count: Int = 1) =
    (0..count).map {
        songMock(it)
    }
