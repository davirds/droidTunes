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
    val artworkUrl30: String? = null,
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
    val collectionPrice: Double? = null,
    @SerialName("collectionViewUrl")
    val collectionViewUrl: String,
    @SerialName("contentAdvisoryRating")
    val contentAdvisoryRating: String? = null,
    @SerialName("country")
    val country: String,
    @SerialName("currency")
    val currency: String,
    @SerialName("discCount")
    val discCount: Int? = null,
    @SerialName("discNumber")
    val discNumber: Int? = null,
    @SerialName("isStreamable")
    val isStreamable: Boolean = false,
    @SerialName("kind")
    val kind: String? = null,
    @SerialName("previewUrl")
    val previewUrl: String? = null,
    @SerialName("primaryGenreName")
    val primaryGenreName: String,
    @SerialName("releaseDate")
    val releaseDate: String,
    @SerialName("trackCensoredName")
    val trackCensoredName: String? = null,
    @SerialName("trackCount")
    val trackCount: Int,
    @SerialName("trackExplicitness")
    val trackExplicitness: String? = null,
    @SerialName("trackId")
    val trackId: Int? = null,
    @SerialName("trackName")
    val trackName: String? = null,
    @SerialName("trackNumber")
    val trackNumber: Int? = null,
    @SerialName("trackPrice")
    val trackPrice: Double? = null,
    @SerialName("trackTimeMillis")
    val trackTimeMillis: Int? = null,
    @SerialName("trackViewUrl")
    val trackViewUrl: String? = null,
    @SerialName("wrapperType")
    val wrapperType: String
)
