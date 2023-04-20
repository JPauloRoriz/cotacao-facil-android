package com.example.cotacaofacil.data.service.settings

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://receitaws.com.br/"

val retrofitConfig: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }).build())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

