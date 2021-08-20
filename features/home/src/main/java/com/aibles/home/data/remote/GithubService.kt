package com.aibles.home.data.remote

import com.aibles.common.utils.Resource
import com.aibles.home.domain.model.dto.remote.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    suspend fun getAllUser(
        @Query("q") query: String,
        @Query("sort") sort: String = "followers"
    ): Resource<UserResponse>
}