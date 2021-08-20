package com.aibles.home.domain.model.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val incompleteResult: Boolean,
    @SerialName("items") val items: List<UserRemote>
)
