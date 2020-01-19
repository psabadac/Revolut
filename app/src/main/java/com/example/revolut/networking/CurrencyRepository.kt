package com.example.revolut.networking

import androidx.lifecycle.MutableLiveData
import com.example.revolut.model.CurrencyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CurrencyRepository {

    private val currencyService = RetrofitService.createService(CurrencyService::class.java)

    fun getCurrencyResponse(base: String?, currencyData:MutableLiveData<CurrencyResponse>) {
        currencyService.getCurrencyResponse(base).enqueue(object : Callback<CurrencyResponse> {
            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                currencyData.postValue(null)
            }

            override fun onResponse(
                call: Call<CurrencyResponse>,
                response: Response<CurrencyResponse>
            ) {
                if (response.isSuccessful) {
                    currencyData.postValue(response.body())
                } else {
                    currencyData.postValue(null)
                }
            }
        })
    }
}