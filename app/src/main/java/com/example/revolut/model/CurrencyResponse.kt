package com.example.revolut.model

data class CurrencyResponse(val date: String?, val base: String?, val rates: Map<String?, Double>)