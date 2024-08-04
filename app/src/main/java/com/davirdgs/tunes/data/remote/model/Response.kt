package com.davirdgs.tunes.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BaseResponse<T>(
    @SerialName("resultCount")
    val resultCount: Int,
    @SerialName("results")
    val results: List<T>
)

@Serializable
internal data class Track(
    @SerialName("artistId")
    val artistId: Int,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("artistViewUrl")
    val artistViewUrl: String,
    @SerialName("artworkUrl100")
    val artworkUrl100: String,
    @SerialName("artworkUrl30")
    val artworkUrl30: String,
    @SerialName("artworkUrl60")
    val artworkUrl60: String,
    @SerialName("collectionArtistId")
    val collectionArtistId: Int? = null,
    @SerialName("collectionArtistName")
    val collectionArtistName: String? = null,
    @SerialName("collectionCensoredName")
    val collectionCensoredName: String,
    @SerialName("collectionExplicitness")
    val collectionExplicitness: String,
    @SerialName("collectionId")
    val collectionId: Int,
    @SerialName("collectionName")
    val collectionName: String,
    @SerialName("collectionPrice")
    val collectionPrice: Double,
    @SerialName("collectionViewUrl")
    val collectionViewUrl: String,
    @SerialName("contentAdvisoryRating")
    val contentAdvisoryRating: String? = null,
    @SerialName("country")
    val country: String,
    @SerialName("currency")
    val currency: String,
    @SerialName("discCount")
    val discCount: Int,
    @SerialName("discNumber")
    val discNumber: Int,
    @SerialName("isStreamable")
    val isStreamable: Boolean,
    @SerialName("kind")
    val kind: String,
    @SerialName("previewUrl")
    val previewUrl: String,
    @SerialName("primaryGenreName")
    val primaryGenreName: String,
    @SerialName("releaseDate")
    val releaseDate: String,
    @SerialName("trackCensoredName")
    val trackCensoredName: String,
    @SerialName("trackCount")
    val trackCount: Int,
    @SerialName("trackExplicitness")
    val trackExplicitness: String,
    @SerialName("trackId")
    val trackId: Int,
    @SerialName("trackName")
    val trackName: String,
    @SerialName("trackNumber")
    val trackNumber: Int,
    @SerialName("trackPrice")
    val trackPrice: Double,
    @SerialName("trackTimeMillis")
    val trackTimeMillis: Int,
    @SerialName("trackViewUrl")
    val trackViewUrl: String,
    @SerialName("wrapperType")
    val wrapperType: String
)
