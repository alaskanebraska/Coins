package com.example.pavel.coins.Api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var ourInstance : Retrofit ?= null

    const val START = 1
    const val LIMIT = 30

    val instance:Retrofit
    get() {
        if(ourInstance == null)
        {
            ourInstance = Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return ourInstance!!
    }
}