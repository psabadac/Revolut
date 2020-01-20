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
import com.example.revolut.utils.Utils
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
        viewAdapter = CurrencyAdapter(Utils.getCurrencyList(resources))

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
            if (Utils.isEmulator()) {
                currencyViewModel.updateCurrency(viewAdapter.getBase(), Utils.getCurrencyList(resources))
            } else {
                currencyViewModel.updateCurrency(viewAdapter.getBase())
            }
        }
    }
}
