package com.example.revolut

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyAdapter(private val currencyList: MutableList<Currency>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val topPosition = 0

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

        holder.view.setOnClickListener {
            moveItem(holder.adapterPosition)
        }
    }

    private fun moveItem(fromPosition: Int) {
        if (fromPosition == topPosition) return

        val movingItem = currencyList.removeAt(fromPosition)
        currencyList.add(topPosition, movingItem)
        notifyItemMoved(fromPosition,topPosition)
    }

    override fun getItemCount() = currencyList.size
}
