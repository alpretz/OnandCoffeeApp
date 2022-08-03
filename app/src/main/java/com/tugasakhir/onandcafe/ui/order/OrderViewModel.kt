package com.tugasakhir.onandcafe.ui.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugasakhir.onandcafe.base.BaseViewModel
import com.tugasakhir.onandcafe.base.DateUtil
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.db.daos.OrderWithMenuDao
import com.tugasakhir.onandcafe.db.daos.TableDao
import com.tugasakhir.onandcafe.model.Order
import com.tugasakhir.onandcafe.model.OrderMenuCrossRef
import com.tugasakhir.onandcafe.ui.home.HomeActivity
import kotlinx.coroutines.launch

class OrderViewModel(
    private val dao: OrderWithMenuDao?,
    private val tableDao: TableDao?
) : BaseViewModel<Long>() {

    fun insertOrder(value: Order): LiveData<Long> {
        val orderId = MutableLiveData<Long>()

        viewModelScope.launch {
            val table = tableDao?.getTableById(value.tableNo.toInt())

            when {
                table == null -> {
                    onError.postValue("Table number not found!")
                }
                table.isOccupied -> {
                    onError.postValue("Table are already occupied!")
                }
                else -> {
                    val response = dao?.insert(value)

                    if (response != null) {
                        orderId.postValue(response)

                        val idToShow = "$response/OC/${DateUtil.formatDate(value.orderDate)}"
                        dao?.setId(idToShow, response)
                    }
                }
            }
        }

        return orderId
    }

    fun setIdToShow(orderId: Long) {

    }

    fun insertOrderWithMenu(orderId: Long, menuId: Long, tableNo: Int) {
        viewModelScope.launch {
            val orderMenuRef = OrderMenuCrossRef(orderId, menuId)
            val id = dao?.insert(orderMenuRef)

            if (id == null) {
                onError.postValue("Cannot save data!")
            } else {
                isSuccess.postValue(id)
                tableDao?.setOccupied(tableNo)
            }
        }
    }

    /// method update order
    fun updateOrder(value: Order, context: Context): LiveData<Long> {
        val orderId = MutableLiveData<Long>()

        viewModelScope.launch {
            /// first, we must know what table user pick
            val table = tableDao?.getTableById(value.tableNo.toInt())

            when {
                /// jika table tidak ada yang dipilih, alias kosong kolomnya, maka muncul toast dibawah
                table == null -> {
                    onError.postValue("No Meja tidak ditemukan!")
                    showToast(context, "No Meja tidak ditemukan!", Toast.LENGTH_SHORT)
                }
                /// jika user mengupdate order, namun tidak merubah posisi table, maka tetap bisa update order di table yang sama
                table.isOccupied -> {
                    orderId.postValue(value.orderId)
                    dao?.updateOrder(
                        value.orderId,
                        value.buyerName,
                        value.tableNo,
                        value.totalPrice
                    )
                }
                /// jika user mengupdate order,  namun merubah posisi pilihan table
                else -> {
                    orderId.postValue(value.orderId)
                    dao?.updateOrder(
                        value.orderId,
                        value.buyerName,
                        value.tableNo,
                        value.totalPrice
                    )
                }
            }
        }

        return orderId
    }

    /// method untuk update menu
    fun updateOrderWithMenu(
        orderId: Long,
        menuId: Long,
        tableNo: Int,
        lastTable: String,
        context: Context
    ) {
        viewModelScope.launch {
            val orderMenuRef = OrderMenuCrossRef(orderId, menuId)
            /// query untuk update menu
            val id = dao?.insert(orderMenuRef)

            if (id == null) {
                onError.postValue("Cannot save data!")
            } else {
                /// hapus table sebelumnya jika user memilih table lain
                if (tableNo != lastTable.toInt()) {
                    tableDao?.setUnOccupied(lastTable.toInt())
                }
                isSuccess.postValue(id)
                tableDao?.setOccupied(tableNo)

                /// sukses update order historu, kembali ke homepage
                val intent = Intent(context, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

            }
        }
    }
}