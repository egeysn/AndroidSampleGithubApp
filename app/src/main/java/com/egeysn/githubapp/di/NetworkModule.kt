package com.egeysn.githubapp.di

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.egeysn.githubapp.BuildConfig
import com.egeysn.githubapp.data.remote.api_services.GithubApiService
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): GithubApiService {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build().create(GithubApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: LoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        builder: OkHttpClient.Builder
    ): OkHttpClient {
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
            builder.addInterceptor(chuckerInterceptor)
        }
        builder.addInterceptor(
            Interceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder().build()
                val requestBuilder = original.newBuilder().url(url)
                requestBuilder.addHeader("Accept", "application/vnd.github+json")
                requestBuilder.addHeader("Authorization", BuildConfig.BEARER_TOKEN)
                requestBuilder.addHeader("X-GitHub-Api-Version", "2022-11-28")
                chain.proceed(requestBuilder.build())
            },
        )
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): LoggingInterceptor =
        LoggingInterceptor.Builder().setLevel(Level.BODY).log(Log.VERBOSE).build()

    @Singleton
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext appContext: Context): ChuckerInterceptor =
        ChuckerInterceptor.Builder(appContext)
            .collector(ChuckerCollector(appContext))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
}
