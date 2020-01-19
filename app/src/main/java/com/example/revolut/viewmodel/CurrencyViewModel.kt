package com.example.revolut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.revolut.networking.CurrencyRepository
import com.example.revolut.model.CurrencyResponse

class CurrencyViewModel : ViewModel() {

    private val currencyData = MutableLiveData<CurrencyResponse>()

    fun updateCurrency(base: String?) {
       CurrencyRepository.getCurrencyResponse(base, currencyData)
    }

    fun getCurrencyRepository(): LiveData<CurrencyResponse> {
        return currencyData
    }
}