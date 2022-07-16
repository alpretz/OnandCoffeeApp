package com.tugasakhir.onandcafe.ui.table

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tugasakhir.onandcafe.R
import com.tugasakhir.onandcafe.databinding.ItemListTableBinding
import com.tugasakhir.onandcafe.model.Table

class TableAdapter(
    private val context: Context,
    private val onClick: (Table) -> Unit
) : RecyclerView.Adapter<TableAdapter.TableHolder>() {

    private val tables = arrayListOf<Table>()

    inner class TableHolder(
        private val binding: ItemListTableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(table: Table) {
            binding.apply {
                tvTableName.text = table.tableName

                if (table.isOccupied) cvTableCard
                    .setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            android.R.color.holo_green_dark
                        )
                    )
                else cvTableCard
                    .setCardBackgroundColor(ContextCompat.getColor(context, R.color.ash))

                itemView.setOnClickListener { onClick(table) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListTableBinding.inflate(inflater, parent, false)

        return TableHolder(binding)
    }

    override fun onBindViewHolder(holder: TableHolder, position: Int) {
        holder.bind(tables[position])
    }

    override fun getItemCount(): Int = tables.size

    fun setData(tables: List<Table>) {
        this.tables.clear()
        this.tables.addAll(tables)
        notifyDataSetChanged()
    }
}