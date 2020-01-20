package com.example.revolut

import android.os.Build
import android.os.Bundle
import androidx.annotation.StyleableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.adapter.CurrencyAdapter
import com.example.revolut.model.Currency
import com.example.revolut.viewmodel.CurrencyViewModel
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer


class MainActivity : AppCompatActivity() {

    private val oneSecond = TimeUnit.SECONDS.toMillis(1)

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CurrencyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var currencyViewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currencyViewModel = ViewModelProviders.of(this)[CurrencyViewModel::class.java]

        viewManager = LinearLayoutManager(this)
        viewAdapter = CurrencyAdapter(getCurrencyList())

        recyclerView = findViewById<RecyclerView>(R.id.currency_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                recyclerView.scrollToPosition(CurrencyAdapter.topPosition)
            }
        })

        currencyViewModel.getCurrencyRepository().observe(this, Observer {
            viewAdapter.updateEachSecond(it)
        })

        fixedRateTimer("timer", false, 0, oneSecond) {
            if (isEmulator()) {
                currencyViewModel.updateCurrency(viewAdapter.getBase(), getCurrencyList())
            } else {
                currencyViewModel.updateCurrency(viewAdapter.getBase())
            }
        }
    }

    private fun getCurrencyList(): MutableList<Currency> {
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

    private fun isEmulator() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
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
