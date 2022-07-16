package com.tugasakhir.onandcafe.ui.stock

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tugasakhir.onandcafe.base.BaseViewModel
import com.tugasakhir.onandcafe.db.daos.StockDao
import com.tugasakhir.onandcafe.model.Stock
import kotlinx.coroutines.launch

class StockViewModel(
    private val stockDao: StockDao?
) : BaseViewModel<Long>() {

    fun getStocks() = stockDao?.getStocks()

    fun insertStock(stock: Stock) {
        viewModelScope.launch {
            val existingStock = stockDao?.getStockById(stock.id)

            if (existingStock != null) {
                onError.postValue("ID already used!")
            } else {
                Log.d("TAG", "Insert")
                isSuccess.postValue(stockDao?.insertStock(stock))
            }
        }
    }

    fun updateStock(stock: Stock) {
        Log.d("TAG", "Update")
        viewModelScope.launch {
            stockDao?.updateStock(stock)
        }
    }

    fun deleteStock(stock: Stock) {
        viewModelScope.launch {
            stockDao?.deleteStock(stock)
        }
    }
}