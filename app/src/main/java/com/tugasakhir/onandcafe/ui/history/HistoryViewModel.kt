package com.tugasakhir.onandcafe.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugasakhir.onandcafe.base.BaseViewModel
import com.tugasakhir.onandcafe.base.DateUtil
import com.tugasakhir.onandcafe.db.daos.OrderWithMenuDao
import com.tugasakhir.onandcafe.db.daos.TableDao
import com.tugasakhir.onandcafe.model.Order
import com.tugasakhir.onandcafe.model.OrderWithMenu
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val dao: OrderWithMenuDao?,
    val tableDao: TableDao?,
) : BaseViewModel<List<Order>>() {

    fun getAll(): LiveData<List<Order>>? = dao?.getAll()

    fun mapData(orders: ArrayList<Order>): List<Order> =
        if (!orders.isNullOrEmpty()) {
            var date = DateUtil.formatDayDate(orders[0].orderDate)
            val order = Order(orderDate = orders[0].orderDate)
            order.type = 1
            orders.add(0, order)

            var i = 0
            while (i <= orders.size - 1) {
                val formattedDate = DateUtil.formatDayDate(orders[i].orderDate)

                if (date != formattedDate) {
                    date = formattedDate

                    val itemOrder = Order(orderDate = orders[i].orderDate)
                    itemOrder.type = 1
                    orders.add(i, itemOrder)
                } else {
                    i++
                }
            }

            orders
        } else {
            listOf()
        }

    fun getOrderByOrderId(orderId: Long): LiveData<OrderWithMenu> {
        val orderDetail = MutableLiveData<OrderWithMenu>()
        viewModelScope.launch {
            orderDetail.postValue(dao?.getByOrderId(orderId))
        }

        return orderDetail
    }

    fun setPaymentStatus(status: Int, orderId: String) {
        viewModelScope.launch {
            dao?.setPaymentStatus(status, orderId)
        }
    }

    fun deleteHistory(orderId: String, tableNo: String) {
        viewModelScope.launch {
            dao?.deleteHistoryByOrderId(orderId)
            tableDao?.setUnOccupied(tableNo.toInt())
        }
    }

}