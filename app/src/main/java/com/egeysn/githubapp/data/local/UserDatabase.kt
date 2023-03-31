package com.egeysn.githubapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.egeysn.githubapp.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 4,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserDao
}
