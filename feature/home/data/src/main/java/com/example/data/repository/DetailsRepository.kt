package com.example.data.repository

import com.example.core.network.data.Product
import com.example.data.api.DetailsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val api: DetailsApi
) {
    suspend fun getProductById(productId: String): Product {
        val data = CoroutineScope(Dispatchers.IO).async {
            api.getProductById(productId)
        }

        val response = data.await()
        return if (response.status) response.value
        else throw Exception(response.message)
    }
}