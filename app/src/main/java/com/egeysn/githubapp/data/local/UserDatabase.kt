package com.egeysn.githubapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.egeysn.githubapp.data.local.entities.FavEntity
import com.egeysn.githubapp.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, FavEntity::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserDao
}
