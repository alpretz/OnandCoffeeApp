package com.tugasakhir.onandcafe.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tugasakhir.onandcafe.base.PrefUtils
import com.tugasakhir.onandcafe.db.MainDb
import com.tugasakhir.onandcafe.ui.history.HistoryViewModel
import com.tugasakhir.onandcafe.ui.home.HomeViewModel
import com.tugasakhir.onandcafe.ui.login.LoginViewModel
import com.tugasakhir.onandcafe.ui.menu.MenuViewModel
import com.tugasakhir.onandcafe.ui.order.OrderViewModel
import com.tugasakhir.onandcafe.ui.stock.StockViewModel
import com.tugasakhir.onandcafe.ui.table.TableViewModel

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = MainDb.getDb(application)
        val userDao = db?.userDao
        val tableDao = db?.tableDao
        val stockDao = db?.stockDao
        val menuDao = db?.menuDao
        val orderWithMenuDao = db?.orderWithMenuDao

        val prefUtil = PrefUtils(application, PrefUtils.AUTH_PREF)

        when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                return LoginViewModel(userDao, prefUtil) as T

            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                return HomeViewModel(userDao, prefUtil) as T

            modelClass.isAssignableFrom(TableViewModel::class.java) ->
                return TableViewModel(tableDao, prefUtil) as T

            modelClass.isAssignableFrom(StockViewModel::class.java) ->
                return StockViewModel(stockDao) as T

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                return MenuViewModel(menuDao, prefUtil) as T

            modelClass.isAssignableFrom(OrderViewModel::class.java) ->
                return OrderViewModel(orderWithMenuDao, tableDao) as T

            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
                return HistoryViewModel(orderWithMenuDao, tableDao) as T
        }

        return (userDao) as T
    }
}