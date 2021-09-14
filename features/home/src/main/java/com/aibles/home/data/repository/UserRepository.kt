package com.aibles.home.data.repository

import com.aibles.common.utils.Resource
import com.aibles.home.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun queryUser(query: String): Flow<Resource<List<User>>>
    fun getAllUser(): Flow<Resource<List<User>>>
}