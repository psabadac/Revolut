package com.example.revolut

data class CurrencyResponse(val date: String?, val base: String?, val rates: Map<String?, Double>)