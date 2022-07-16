package com.tugasakhir.onandcafe.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tugasakhir.onandcafe.base.BaseViewModel
import com.tugasakhir.onandcafe.base.PrefUtils
import com.tugasakhir.onandcafe.db.daos.UserDao
import com.tugasakhir.onandcafe.model.User
import kotlinx.coroutines.launch

class   LoginViewModel(
    private val userDao: UserDao?,
    private val prefUtils: PrefUtils
) : BaseViewModel<User>() {

    init {
        viewModelScope.launch {
            userDao?.insertUser(
                User(1, "admin", "admin@admin.com", "admin123", true)
            )
        }
    }

    fun isLoggedIn(): Boolean {
        Log.d("TAG", "LoggedIn ${prefUtils.getFromPrefInt(PrefUtils.AUTH_ID_PREF) > 0}")
        return prefUtils.getFromPrefInt(PrefUtils.AUTH_ID_PREF) > 0
    }

    fun authenticateUser(value: String, password: String) {
        isLoading.postValue(true)
        viewModelScope.launch {
            val user = userDao?.getUserByUsernameEmailAndPassword(value, password)

            if (user != null) {
                isLoading.postValue(false)
                isSuccess.postValue(user)
                prefUtils.saveToPref(PrefUtils.AUTH_ID_PREF, user.id)
                prefUtils.saveToPref(PrefUtils.AUTH_ADMIN_PREF, user.isAdmin)
                Log.d("TAG", "ID ${user.id} ADMIN ${user.isAdmin} $user")
            } else {
                isLoading.postValue(false)
                onError.postValue("Invalid credentials!")
            }
        }
    }
}