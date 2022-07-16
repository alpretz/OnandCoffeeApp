package com.tugasakhir.onandcafe.ui.order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.databinding.ItemListOrderBinding
import com.tugasakhir.onandcafe.model.Menu

class OrderAdapter(
    private val context: Context,
    private val onClick: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {
    private val menus = arrayListOf<Menu>()
    private val menuIds = arrayListOf<Long>()

    inner class OrderHolder(
        private val binding: ItemListOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu, position: Int) {
            binding.apply {
                tvName.text = menu.name

                if (menu.name.length > 1) {
                    tvStockInitial.text = menu.name.slice(0..1)
                } else {
                    tvStockInitial.text = menu.name[0].toString()
                }

                tvPrice.text = context.getString(R.string.rp, menu.price.toString())
                tvQty.text = menu.qty.toString()

                ibAdd.setOnClickListener { changeQty(1, menu.price) }
                ibReduce.setOnClickListener { changeQty(-1, menu.price) }
                if(context.toString() == "android.app.Application@8b92452"){
                    Log.e("tag", "")
                    ibDelete.visibility = View.GONE
                }
                ibDelete.setOnClickListener { removeData(adapterPosition) }

                menuIds.add(menu.menuId)
            }
        }

        private fun changeQty(value: Int, price: Long) {
            var qty = binding.tvQty.text.toString().toLong()
            qty += value

            val totalPrice = price * qty
            binding.tvQty.text = qty.toString()
            binding.tvPrice.text = context.getString(R.string.rp, totalPrice.toString())

            onClick(price, value > 0)
        }

        private fun removeData(position: Int) {
            menus.removeAt(position)
            menuIds.removeAt(position)
            notifyItemRemoved(position)

            var currentPrice = binding.tvPrice.text.toString()
            if (currentPrice.contains("Rp."))
                currentPrice = currentPrice.removeRange(0..2)

            onClick(currentPrice.toLong(), false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListOrderBinding.inflate(inflater, parent, false)

        return OrderHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(menus[position], position)
    }

    override fun getItemCount(): Int = menus.size

    fun addData(menu: Menu) {
        menus.add(menu)
        notifyItemChanged(menus.size - 1)
    }

    fun getMenus(): List<Menu> = menus
    fun getIds(): List<Long> = menuIds
}