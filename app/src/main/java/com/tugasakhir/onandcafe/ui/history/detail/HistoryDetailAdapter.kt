package com.tugasakhir.onandcafe.ui.history.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.base.visible
import com.tugasakhir.onandcafe.databinding.ItemListOrderBinding
import com.tugasakhir.onandcafe.model.Menu

class HistoryDetailAdapter(
    private val context: Context
) : RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailHolder>() {
    private val menus = arrayListOf<Menu>()

    inner class HistoryDetailHolder(
        private val binding: ItemListOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.apply {
                tvName.text = menu.name

                if (menu.name.length > 1) {
                    tvStockInitial.text = menu.name.slice(0..1)
                } else {
                    tvStockInitial.text = menu.name[0].toString()
                }

                tvPrice.text = context.getString(R.string.rp, menu.price.toString())
                tvQty.text = menu.qty.toString()
                ibDelete.visible(false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListOrderBinding.inflate(inflater, parent, false)

        return HistoryDetailHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryDetailHolder, position: Int) {
        holder.bind(menus[position])
    }

    override fun getItemCount(): Int = menus.size

    fun setData(menus: List<Menu>) {
        this.menus.addAll(menus)
    }
}