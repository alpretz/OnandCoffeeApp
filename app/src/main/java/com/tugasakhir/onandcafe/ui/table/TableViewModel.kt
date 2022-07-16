package com.tugasakhir.onandcafe.ui.table

import androidx.lifecycle.viewModelScope
import com.tugasakhir.onandcafe.base.BaseViewModel
import com.tugasakhir.onandcafe.base.PrefUtils
import com.tugasakhir.onandcafe.db.daos.TableDao
import com.tugasakhir.onandcafe.model.Table
import kotlinx.coroutines.launch

class TableViewModel(
    private val tableDao: TableDao?,
    private val prefUtils: PrefUtils
) : BaseViewModel<List<Table>>() {

    fun getTables() = tableDao?.getTables()

    fun generateTable() {
        val tables = arrayListOf<Table>()

        for (i: Int in 1 until 8) {
            val table = Table(i, "Meja $i", false)
            tables.add(table)
        }

        viewModelScope.launch {
            tableDao?.insertTables(tables)
        }
    }

    fun setUnOccupied(id: Int) {
        viewModelScope.launch {
            tableDao?.setUnOccupied(id)
        }
    }

    fun getIsAdmin(): Boolean = prefUtils.getFromPrefBoolean(PrefUtils.AUTH_ADMIN_PREF)
}