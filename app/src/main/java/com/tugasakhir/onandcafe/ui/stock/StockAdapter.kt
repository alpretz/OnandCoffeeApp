package com.tugasakhir.onandcafe.ui.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugasakhir.onandcafe.databinding.ItemListStockBinding
import com.tugasakhir.onandcafe.model.Stock

class StockAdapter(
    private val onClick: (Stock) -> Unit
) : RecyclerView.Adapter<StockAdapter.StockHolder>() {

    private val stocks = arrayListOf<Stock>()

    inner class StockHolder(
        private val binding: ItemListStockBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: Stock) {
            binding.apply {
                tvStockName.text = stock.stockName

                if (stock.stockName.length > 1) {
                    tvStockInitial.text = stock.stockName.slice(0..1)
                } else {
                    tvStockInitial.text = stock.stockName[0].toString()
                }

                itemView.setOnClickListener { onClick(stock) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListStockBinding.inflate(inflater, parent, false)

        return StockHolder(binding)
    }

    override fun onBindViewHolder(holder: StockHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int = stocks.size

    fun setData(stocks: List<Stock>) {
        this.stocks.clear()
        this.stocks.addAll(stocks)
        notifyDataSetChanged()
    }
}