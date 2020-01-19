package com.example.revolut.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.model.Currency
import com.example.revolut.model.CurrencyResponse
import com.example.revolut.R
import kotlinx.android.synthetic.main.currency_item.view.*
import java.text.DecimalFormat

class CurrencyAdapter(private val currencyList: MutableList<Currency>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    companion object {
        const val topPosition = 0
        const val defaultCurrency = "EUR"
        const val defaultAmount = 100.0
    }

    private val decimalFormatter = DecimalFormat("#.##")
    private var currencyResponse: CurrencyResponse? = null
    private var base: String? = defaultCurrency
    private var amount = defaultAmount

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
            val index: Double = currencyResponse?.rates?.get(currency.title) ?: 1.0
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
        notifyItemMoved(fromPosition, topPosition)
    }

    private fun updateCurrency(newBase: String?, newAmount: String) {
        base = newBase
        amount = if (newAmount.isNotEmpty()) newAmount.toDouble() else 0.0
    }

    private fun canMoveItem(fromPosition: Int): Boolean =
        !(fromPosition == topPosition || fromPosition < topPosition)

    fun updateEachSecond(newCurrencyResponse: CurrencyResponse?) {
        if (newCurrencyResponse != null) {
            currencyResponse = newCurrencyResponse
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
        }
    }

    fun getBase(): String? = base

    override fun getItemCount() = currencyList.size
}
