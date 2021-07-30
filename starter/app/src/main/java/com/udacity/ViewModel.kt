package com.udacity

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

    private val _currentUrl = MutableLiveData<String>()
    val currentUrl : LiveData<String>
    get() = _currentUrl

    fun setUrl(myUrl : String)
    {
        _currentUrl.value = myUrl
    }
}
