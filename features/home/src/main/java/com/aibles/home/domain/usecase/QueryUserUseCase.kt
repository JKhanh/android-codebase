package com.aibles.home.domain.usecase

import com.aibles.home.data.repository.UserRepository
import javax.inject.Inject

class QueryUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(query: String) =
        repository.queryUser(query)
}