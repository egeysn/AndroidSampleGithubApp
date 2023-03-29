package com.egeysn.githubapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.egeysn.githubapp.data.local.entities.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserDao
}
