package com.davirdgs.tunes.data.remote

import com.davirdgs.tunes.data.remote.model.BaseResponse
import com.davirdgs.tunes.data.remote.model.Track
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ApiService {
    @GET("/search")
    suspend fun search(
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("term") term: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): BaseResponse<Track>

    @GET("/lookup?id=974485462&entity=song")
    suspend fun lookup(
        @Query("id") id: Int,
        @Query("entity") entity: String = "song",
    ): BaseResponse<Track>
}
