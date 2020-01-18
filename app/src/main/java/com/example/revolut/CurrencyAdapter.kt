package com.example.revolut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.currency_item.view.*
import java.text.DecimalFormat
import kotlin.random.Random

class CurrencyAdapter(private val currencyList: MutableList<Currency>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val topPosition = 0
    private val decimalFormatter = DecimalFormat("#.##")

    private var base:String? = "EUR"
    private var amount = 100.0
    private var currencyResponse = getCurrencyResponse(base)

    class CurrencyViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_item, parent, false) as ViewGroup

        return CurrencyViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencyList[position]
        val root = holder.view
        root.currency_title.text = currency.title
        root.currency_description.text = currency.description
        root.country_flag.setBackgroundResource(currency.countryFlag)

        if (holder.adapterPosition != topPosition) {
            val index : Double = currencyResponse.rates[currency.title] ?: 1.0
            val amount = index * amount
            val stringAmount = decimalFormatter.format(amount)
            root.currency_amount.text.replace(0, root.currency_amount.text.length, stringAmount)
        } else {
            val stringAmount = decimalFormatter.format(amount)
            root.currency_amount.text.replace(0, root.currency_amount.text.length, stringAmount)
        }

        root.currency_amount.doAfterTextChanged {
            val stringAmount = it.toString()
            if (holder.adapterPosition == topPosition && amount.toString() != stringAmount) {
                amount = if (stringAmount.isNotEmpty()) stringAmount.toDouble() else 0.0
            }
        }

        root.setOnClickListener {
            val stringAmount = root.currency_amount.text.toString()
            root.currency_amount.requestFocus()
            root.currency_amount.setSelection(stringAmount.length)

            val fromPosition = holder.adapterPosition

            if (canMoveItem(fromPosition)) {
                updateCurrency(currencyList[fromPosition].title, stringAmount)
                moveItem(fromPosition)
            }
        }
    }

    private fun moveItem(fromPosition: Int) {
        val movingItem = currencyList.removeAt(fromPosition)
        currencyList.add(topPosition, movingItem)
        notifyItemMoved(fromPosition,topPosition)
    }

    private fun updateCurrency(newBase: String?, newAmount: String) {
        base = newBase
        amount = if (newAmount.isNotEmpty()) newAmount.toDouble() else 0.0
    }

    private fun canMoveItem(fromPosition: Int) : Boolean = !(fromPosition == topPosition || fromPosition < topPosition)

    fun updateEachSecond() {
        currencyResponse = getCurrencyResponse(base)
        notifyDataSetChanged()
    }

    private fun getCurrencyResponse(baseCurrency: String?) : CurrencyResponse {
        val currencyListWithoutBaseCurrency =  currencyList.filter { it.title != baseCurrency }.map { it.title to Random.nextDouble(0.1, 1.9) }.toMap()
        return CurrencyResponse("2018-09-06", baseCurrency, currencyListWithoutBaseCurrency)
    }

    override fun getItemCount() = currencyList.size
}
