package com.davirdgs.tunes.data.remote

import com.davirdgs.tunes.data.remote.model.BaseResponse
import com.davirdgs.tunes.data.remote.model.Track
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ApiService {
    @GET("/search")
    suspend fun search(
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "musicTrack",
        @Query("term") term: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): BaseResponse<Track>
}
