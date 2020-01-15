package com.example.revolut

import android.os.Bundle
import androidx.annotation.StyleableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                recyclerView.scrollToPosition(0)
            }
        })
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
            val currency = Currency(currencyTypedArray.getString(titleIndex),
                                    currencyTypedArray.getString(descriptionIndex),
                                    currencyTypedArray.getResourceId(countryFlagIndex, invalidResourceId))
            currencyList.add(currency)
            currencyTypedArray.recycle()
        }
        currencyListTypedArray.recycle()

        return currencyList
    }
}
