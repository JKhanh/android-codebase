package com.aibles.home.domain.model.dto.remote

import com.aibles.home.domain.model.dto.local.UserLocal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("gravatar_id")
    val gravatarId: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("id")
    val id: Int,
    @SerialName("login")
    val login: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("score")
    val score: Double
){
    fun mapToLocal() =
        UserLocal(avatarUrl, gravatarId, htmlUrl, id, login, reposUrl, score)
}