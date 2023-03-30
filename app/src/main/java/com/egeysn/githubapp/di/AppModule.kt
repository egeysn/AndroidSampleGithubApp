package com.egeysn.githubapp.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.egeysn.githubapp.BuildConfig
import com.egeysn.githubapp.data.local.UserDao
import com.egeysn.githubapp.data.local.UserDatabase
import com.egeysn.githubapp.data.services.localStorage.KeyValueStore
import com.egeysn.githubapp.data.services.localStorage.SharedPreferencesKeyValueStore
import com.egeysn.githubapp.domain.mappers.UserMapper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideSharedPreferencesKeyValueStore(preferences: SharedPreferences): KeyValueStore =
        SharedPreferencesKeyValueStore(preferences)

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Activity.MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun provideMovieDatabase(app: Application, gson: Gson): UserDatabase {
        return Room.databaseBuilder(app, UserDatabase::class.java, "user_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: UserDatabase): UserDao = db.dao

    @Provides
    @Singleton
    fun provideMovieMapper(): UserMapper = UserMapper()
}
