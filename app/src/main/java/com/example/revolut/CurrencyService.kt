package com.example.revolut

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyService {
    @GET("/latest")
    fun getCurrencyResponse(@Query("base") base: String?): Call<CurrencyResponse>
}
