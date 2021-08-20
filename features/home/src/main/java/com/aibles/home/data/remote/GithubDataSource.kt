package com.aibles.home.data.remote

import javax.inject.Inject

class GithubDataSource @Inject constructor(
    private val service: GithubService
) {
    suspend fun getAllUser(query: String) = service.getAllUser(query)
}