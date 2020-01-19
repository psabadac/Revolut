package com.example.revolut

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrencyViewModel : ViewModel() {

    private val currencyData = MutableLiveData<CurrencyResponse>()

    fun updateCurrency(base: String?) {
       CurrencyRepository.getCurrencyResponse(base, currencyData)
    }

    fun getCurrencyRepository(): LiveData<CurrencyResponse> {
        return currencyData
    }
}