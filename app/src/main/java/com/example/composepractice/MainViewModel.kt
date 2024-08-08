package com.example.composepractice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel: ViewModel() {
    private val _isReady = MutableLiveData(false)

    val  isReady: LiveData<Boolean> get() = _isReady

    fun setReady(ready: Boolean){
        _isReady.value = ready
    }
}
