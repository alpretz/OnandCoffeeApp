package com.tugasakhir.onandcafe.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.DateUtil
import com.tugasakhir.onandcafe.databinding.ItemListDateBinding
import com.tugasakhir.onandcafe.databinding.ItemListHistoryBinding
import com.tugasakhir.onandcafe.model.Order
import java.util.*

class HistoryAdapter(
    private val context: Context,
    private val onCLick: (Long) -> Unit
) : ListAdapter<Order, RecyclerView.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
                oldItem.orderId == newItem.orderId

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
                oldItem == newItem

        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) != null) {
            return getItem(position).type
        }

        return 0
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding: ViewBinding

        if (p1 == 0) {
            binding = ItemListHistoryBinding.inflate(inflater, p0, false)
            return MainHolder(binding)
        }

        binding = ItemListDateBinding.inflate(inflater, p0, false)
        return DateHolder(binding)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val data = getItem(p1)

        if (data != null) {
            if (p0.itemViewType == 0) {
                p0 as MainHolder
                p0.bind(data)
            } else {
                p0 as DateHolder
                p0.bind(data.orderDate)
            }
        }
    }

    inner class MainHolder(private val binding: ItemListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.apply {
                tvDate.text = DateUtil.formatDateTime(order.orderDate)
                tvId.text = order.id
                tvPayment.text = order.paymentType
                tvPriceTotal.text = context.getString(R.string.rp, order.totalPrice.toString())
                tvTable.text = context.getString(R.string.table_, order.tableNo)

                if (order.isPaid == 1) {
                    vStatus.setBackgroundResource(R.drawable.circle_green)
                }
            }

            itemView.setOnClickListener { onCLick(order.orderId) }
        }
    }

    inner class DateHolder(private val binding: ItemListDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: Date) {
            binding.itemDate.text = DateUtil.formatDayDate(date)
        }
    }
}