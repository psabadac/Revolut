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

    private var currencyBase:String? = "EUR"
    private var currentAmount = 0.0
    private var currencyResponse = getCurrencyResponse(currencyBase)

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

        holder.view.currency_title.text = currency.title
        holder.view.currency_description.text = currency.description
        holder.view.country_flag.setBackgroundResource(currency.countryFlag)

        if (holder.adapterPosition != topPosition) {
            val index : Double = currencyResponse.rates[currency.title] ?: 1.0
            val amount = index * currentAmount
            val stringAmount = decimalFormatter.format(amount)
            holder.view.currency_amount.text.replace(0, holder.view.currency_amount.text.length, stringAmount)
        } else {
            val stringAmount = decimalFormatter.format(currentAmount)
            holder.view.currency_amount.text.replace(0, holder.view.currency_amount.text.length, stringAmount)
        }

        holder.view.currency_amount.doAfterTextChanged {
            val stringAmount = it.toString()
            if (holder.adapterPosition == topPosition && currentAmount.toString() != stringAmount) {
                currentAmount = if (stringAmount.isNotEmpty()) stringAmount.toDouble() else 0.0
            }
        }

        holder.view.setOnClickListener {
            val stringAmount = holder.view.currency_amount.text.toString()
            holder.view.currency_amount.requestFocus()
            holder.view.currency_amount.setSelection(stringAmount.length)
            moveItem(holder.adapterPosition, stringAmount)
        }
    }

    private fun moveItem(fromPosition: Int, stringAmount: String) {
        if (fromPosition == topPosition || fromPosition < topPosition) return

        val movingItem = currencyList.removeAt(fromPosition)
        currencyList.add(topPosition, movingItem)
        currencyBase = movingItem.title
        currentAmount = if (stringAmount.isNotEmpty()) stringAmount.toDouble() else 0.0
        currencyResponse = getCurrencyResponse(currencyBase)
        notifyItemMoved(fromPosition,topPosition)
    }

    fun updateEachSecond() {
        currencyResponse = getCurrencyResponse(currencyBase)
        notifyDataSetChanged()
    }

    private fun getCurrencyResponse(baseCurrency: String?) : CurrencyResponse {
        val currencyListWithoutBaseCurrency =  currencyList.filter { it.title != baseCurrency }.map { it.title to Random.nextDouble(0.1, 1.9) }.toMap()
        return CurrencyResponse("2018-09-06", baseCurrency, currencyListWithoutBaseCurrency)
    }

    override fun getItemCount() = currencyList.size
}
