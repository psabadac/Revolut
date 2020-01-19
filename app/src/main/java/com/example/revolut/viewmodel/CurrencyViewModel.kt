package com.example.revolut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.revolut.model.Currency
import com.example.revolut.networking.CurrencyRepository
import com.example.revolut.model.CurrencyResponse
import kotlin.random.Random

class CurrencyViewModel : ViewModel() {

    private val currencyData = MutableLiveData<CurrencyResponse>()

    fun updateCurrency(base: String?) {
       CurrencyRepository.getCurrencyResponse(base, currencyData)
    }

    // For testing purposes only
    fun updateCurrency(base: String?, currencyList: List<Currency>) {
        val currencyListWithoutBaseCurrency =  currencyList.filter { it.title != base }.map { it.title to Random.nextDouble(0.1, 1.9) }.toMap()
        currencyData.postValue(CurrencyResponse("2018-09-06", base, currencyListWithoutBaseCurrency))
    }

    fun getCurrencyRepository(): LiveData<CurrencyResponse> {
        return currencyData
    }
}