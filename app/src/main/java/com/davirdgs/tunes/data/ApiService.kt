package com.davirdgs.tunes.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search")
    suspend fun search(
        @Query("media") media: String = "music",
        @Query("term") term: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): String // TODO create the right model
}
