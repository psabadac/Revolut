package com.example.revolut.networking

import com.example.revolut.model.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("/latest")
    fun getCurrencyResponse(@Query("base") base: String?): Call<CurrencyResponse>
}
