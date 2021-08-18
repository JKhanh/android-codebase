package com.aibles.home.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aibles.home.domain.model.dto.local.UserLocal

@Database(
    entities = [UserLocal::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        fun buildDatabase(context: Context): UserDatabase =
            Room.databaseBuilder(context.applicationContext,
                UserDatabase::class.java,
            "user.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}