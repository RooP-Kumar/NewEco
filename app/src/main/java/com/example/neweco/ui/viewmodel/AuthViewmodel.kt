package com.example.neweco.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweco.network.Resource
import com.example.neweco.network.repository.AuthRepository
import com.example.neweco.ui.utils.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _status : MutableLiveData<LoadingStatus> = MutableLiveData()
    val status : LiveData<LoadingStatus> get() = _status

    fun registerUser(email: String, pass: String) {
        _status.postValue(LoadingStatus(showLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            when(val res = repository.register(email, pass)){
                is Resource.SUCCESS -> {
                    _status.postValue(LoadingStatus(showLoading = false, true, message = res.value.message))
                }
                is Resource.FAILURE -> {
                    _status.postValue(LoadingStatus(showLoading = false, false, message = res.message))
                }
            }
        }
    }

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