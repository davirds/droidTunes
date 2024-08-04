package com.davirdgs.tunes.data.model

data class Song(
    val id: Int,
    val name: String,
    val timeMillis: Int,
    val artworkUrl: String,
    val artist: Artist,
    val collection: Collection
)

data class Artist(
    val id: Int,
    val name: String,
)

data class Collection(
    val id: Int,
    val name: String,
)
