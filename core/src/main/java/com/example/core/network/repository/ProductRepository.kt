package com.example.core.network.repository

import com.example.core.network.Resource
import com.example.core.network.Response
import com.example.core.network.api.ProductApi
import com.example.core.network.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: ProductApi) {
    suspend fun getProducts() : Resource<Response<List<Product>>> {
        return withContext(Dispatchers.IO) {
            val response = api.getProducts()
            if (response.status) Resource.SUCCESS(value = response)
            else Resource.FAILURE(message = response.message)
        }
    }

    fun getProductByCategory(category: String): Flow<List<Product>> = flow {
        val response = api.getProductByCategory(category)
        if (response.status) {
            emit(response.value)
        } else {
            throw Exception(response.message)
        }
    }
}