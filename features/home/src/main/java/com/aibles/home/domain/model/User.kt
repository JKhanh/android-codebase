package com.aibles.home.domain.model

data class User(
    val avatarUrl: String,
    val gravatarId: String,
    val htmlUrl: String,
    val id: Int,
    val login: String,
    val reposUrl: String,
    val score: Double
)
