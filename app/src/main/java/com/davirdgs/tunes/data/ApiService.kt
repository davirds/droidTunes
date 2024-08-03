package com.davirdgs.tunes.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search")
    suspend fun search(
        @Query("media") media: String = "music",
        @Query("term") term: String
    ): String // TODO create the right model
}
