package com.aibles.home.domain.model.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aibles.home.domain.model.User

@Entity(tableName = "user_local")
data class UserLocal(
    val avatarUrl: String,
    val gravatarId: String,
    val htmlUrl: String,
    @PrimaryKey val id: Int,
    val login: String,
    val reposUrl: String,
    val score: Double
){
    fun mapToDomain() =
        User(avatarUrl, gravatarId, htmlUrl, id, login, reposUrl, score)
}
