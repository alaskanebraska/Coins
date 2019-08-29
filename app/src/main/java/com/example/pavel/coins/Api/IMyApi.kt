package com.example.pavel.coins.Api

import com.example.pavel.coins.Model.Response
import com.example.pavel.coins.Model.ResponseTwo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface IMyApi {


    @GET("v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=f6a8d6b1-c5f5-4c7b-812d-7f6758c7c8d3")
    fun getFullCoins(@Query("start") start: Int, @Query("limit") limit: Int): Single<Response>


    @GET("v1/global-metrics/quotes/latest?CMC_PRO_API_KEY=f6a8d6b1-c5f5-4c7b-812d-7f6758c7c8d3")
    fun getDetails(): Observable<ResponseTwo>

}