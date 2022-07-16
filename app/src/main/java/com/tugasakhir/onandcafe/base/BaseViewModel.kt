package com.tugasakhir.onandcafe.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {
    internal val isLoading = MutableLiveData(false)
    internal val isSuccess = MutableLiveData<T>()
    internal val onError = MutableLiveData("")

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getIsSuccess(): LiveData<T> = isSuccess
    fun getOnError(): LiveData<String> = onError
}