package com.example.presentation.ui.screen.details

import androidx.lifecycle.viewModelScope
import com.example.core.network.repository.ProductRepository
import com.example.core.viewmodels.ProductViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    product: ProductRepository
) : ProductViewModel(product) {
    fun getProductById(productId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = async {

            }
        }
    }
}