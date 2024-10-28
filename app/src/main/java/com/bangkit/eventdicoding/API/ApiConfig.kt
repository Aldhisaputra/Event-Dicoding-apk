package com.bangkit.eventdicoding.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val API_Link = "https://event-api.dicoding.dev/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(API_Link)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient())
                .build()
        }

        private fun getHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        fun getApiService(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}
