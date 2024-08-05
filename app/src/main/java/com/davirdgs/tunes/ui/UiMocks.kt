package com.davirdgs.tunes.ui

import com.davirdgs.tunes.data.model.Artist
import com.davirdgs.tunes.data.model.Collection
import com.davirdgs.tunes.data.model.Song

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

internal fun songsMock(count: Int = 1) = buildList {
    repeat(count) { add(songMock(it)) }
}
