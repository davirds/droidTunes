package com.davirdgs.tunes

import com.davirdgs.tunes.data.remote.ApiService
import com.davirdgs.tunes.data.remote.model.BaseResponse
import com.davirdgs.tunes.data.remote.model.Track
import io.mockk.coEvery
import java.lang.Exception

internal fun ApiService.returnsEmpty(func: suspend ApiService.() -> BaseResponse<Track>) =
    returnsSuccess(items = 0, func)

internal fun ApiService.returnsSuccess(
    items: Int = 5,
    func: suspend ApiService.() -> BaseResponse<Track>
) =
    coEvery { func() } returns BaseResponse(
        resultCount = items,
        results = buildList {
            repeat(items) { index ->
                add(createTrackMock(index))
            }
        }
    )

internal fun ApiService.returnsError(func: suspend ApiService.() -> BaseResponse<Track>) =
    coEvery { func() } throws Exception()

private fun createTrackMock(id: Int) = Track(
    kind = "song",
    artistId = id,
    artistName = "Someone",
    trackId = id,
    trackName = "Something $id",
    collectionId = id,
    collectionName = "Something by Someone",
    trackNumber = id,
    discCount = id,
    discNumber = id,
    collectionArtistId = id,
    trackCount = id,
    artistViewUrl = "",
    artworkUrl100 = "",
    artworkUrl30 = "",
    artworkUrl60 = "",
    collectionArtistName = "",
    collectionCensoredName = "",
    collectionExplicitness = "",
    collectionPrice = null,
    collectionViewUrl = "",
    contentAdvisoryRating = "",
    country = "",
    currency = "",
    isStreamable = true,
    previewUrl = "",
    primaryGenreName = "",
    releaseDate = "",
    trackCensoredName = null,
    trackExplicitness = null,
    trackPrice = null,
    trackTimeMillis = 0,
    trackViewUrl = "",
    wrapperType = "",
)
