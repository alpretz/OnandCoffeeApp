package com.tugasakhir.onandcafe.ui.home

import androidx.lifecycle.viewModelScope
import com.tugasakhir.onandcafe.base.BaseViewModel
import com.tugasakhir.onandcafe.base.PrefUtils
import com.tugasakhir.onandcafe.db.daos.UserDao
import com.tugasakhir.onandcafe.model.User
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userDao: UserDao?,
    private val prefUtils: PrefUtils
) : BaseViewModel<User>() {

    fun getUserById() {
        val id = prefUtils.getFromPrefInt(PrefUtils.AUTH_ID_PREF)
        viewModelScope.launch { isSuccess.postValue(userDao?.getUserById(id.toString())) }
    }

    fun logout() {
        prefUtils.removeFromPref(PrefUtils.AUTH_ID_PREF)
        prefUtils.removeFromPref(PrefUtils.AUTH_ADMIN_PREF)
    }
}