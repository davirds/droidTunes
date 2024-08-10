package com.davirdgs.tunes.data.model

data class Song(
    val id: Int,
    val name: String,
    val timeMillis: Int,
    val artworkUrl: String,
    val artist: Artist,
    val collection: Collection,
    val previewUrl: String
) {
    val largeArtWorkUrl: String by lazy { artworkUrl.resize(500) }
}

data class Artist(
    val id: Int,
    val name: String,
)

data class Collection(
    val id: Int,
    val name: String,
)

private fun String.resize(size: Int): String {
    val regex = "/(\\d*)x(\\d*)[bb.jpg]+".toRegex()
    return try {
        this.replace(regex, "/${size}x${size}bb.jpg")
    } catch (e: Exception) {
        this
    }
}
