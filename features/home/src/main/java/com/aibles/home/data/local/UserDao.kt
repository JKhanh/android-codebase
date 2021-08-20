package com.aibles.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aibles.home.domain.model.dto.local.UserLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userLocal: UserLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userLocal: List<UserLocal>)

    @Query("SELECT * FROM user_local WHERE login LIKE :query")
    fun findUserWithName(query: String): Flow<List<UserLocal>>
}