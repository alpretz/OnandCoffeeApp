package com.tugasakhir.onandcafe.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tugasakhir.onandcafe.model.Stock

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStock(stock: Stock): Long

    @Update
    suspend fun updateStock(stock: Stock)

    @Delete
    suspend fun deleteStock(stock: Stock)

    @Query("select * from stock")
    fun getStocks(): LiveData<List<Stock>>

    @Query("select * from stock where id=:id")
    suspend fun getStockById(id: Int): Stock
}