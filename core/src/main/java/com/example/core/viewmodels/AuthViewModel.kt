package com.example.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.Resource
import com.example.core.network.repository.AuthRepository
import com.example.core.utils.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _status : MutableLiveData<LoadingStatus> = MutableLiveData()
    val status : LiveData<LoadingStatus> get() = _status

    fun registerUser(email: String, pass: String, name : String, phone : String?) {
        _status.postValue(LoadingStatus(showLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            when(val res = repository.register(email, pass, name, phone?:"")){
                is Resource.SUCCESS -> {
                    _status.postValue(LoadingStatus(showLoading = false, true, message = res.value.message))
                }
                is Resource.FAILURE -> {
                    _status.postValue(LoadingStatus(showLoading = false, false, message = res.message))
                }
            }
        }
    }

    // there is something

    fun loginUser(email: String, pass: String) {
        _status.postValue(LoadingStatus(true))
        viewModelScope.launch(Dispatchers.IO) {
            when(val res = repository.login(email, pass)){
                is Resource.SUCCESS -> {
                    _status.postValue(LoadingStatus(showLoading = false, true, message = res.value.message))
                }
                is Resource.FAILURE -> {
                    _status.postValue(LoadingStatus(showLoading = false, false, message = res.message))
                }
            }
        }
    }
}