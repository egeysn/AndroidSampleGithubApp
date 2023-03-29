package com.egeysn.githubapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.egeysn.githubapp.data.local.entities.MovieEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM popularMovies")
    suspend fun getPopularMovies(): List<MovieEntity>

    @Query("DELETE FROM popularMovies")
    suspend fun deleteAllMovies()
}
