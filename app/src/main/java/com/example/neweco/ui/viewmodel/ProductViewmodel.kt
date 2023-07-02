package com.example.neweco.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweco.network.Resource
import com.example.neweco.network.data.Product
import com.example.neweco.network.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository): ViewModel() {

//    val status : MutableLiveData<Boolean?> = MutableLiveData(null)
    private val _products = MutableLiveData<List<Product>>()
    val products : LiveData<List<Product>> get() = _products
    fun getProducts() {
        flow {
            when(val res = repository.getProducts()) {
                is Resource.SUCCESS -> {
                    emit(res.value.value)
                }
                is Resource.FAILURE -> {
                    throw Exception(res.message)
                }
            }
        }
            .onEach {
                _products.postValue(it)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private val _productByCategory = MutableLiveData<List<Product>>()
    val productByCategory : LiveData<List<Product>> get() = _productByCategory

    fun getProductByCategory(category : String) {
        repository.getProductByCategory(category)
            .onEach {
                _productByCategory.postValue(it)
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

}