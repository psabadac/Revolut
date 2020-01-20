package com.example.revolut.utils

import android.content.res.Resources
import android.os.Build
import androidx.annotation.StyleableRes
import com.example.revolut.R
import com.example.revolut.model.Currency

object Utils {
    fun getCurrencyList(resources: Resources): MutableList<Currency> {
        val currencyList = mutableListOf<Currency>()
        val invalidResourceId = -1
        @StyleableRes val titleIndex = 0
        @StyleableRes val descriptionIndex = 1
        @StyleableRes val countryFlagIndex = 2

        val currencyListTypedArray = resources.obtainTypedArray(R.array.currencies_list)
        for (i in 0..currencyListTypedArray.length()) {
            val currencyId = currencyListTypedArray.getResourceId(i, invalidResourceId)
            if (currencyId == invalidResourceId) {
                continue
            }

            val currencyTypedArray = resources.obtainTypedArray(currencyId)
            val currency = Currency(
                currencyTypedArray.getString(titleIndex),
                currencyTypedArray.getString(descriptionIndex),
                currencyTypedArray.getResourceId(countryFlagIndex, invalidResourceId)
            )
            currencyList.add(currency)
            currencyTypedArray.recycle()
        }
        currencyListTypedArray.recycle()

        return currencyList
    }

    fun isEmulator() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator"))
}