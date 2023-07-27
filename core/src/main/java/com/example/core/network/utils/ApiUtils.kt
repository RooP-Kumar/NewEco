package com.example.core.network.utils

import com.example.core.network.Resource
import com.example.core.network.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiUtils {
    suspend fun <T> safeCall(
        call: suspend () -> Response<T>
    ): Resource<Response<T>> {
        return withContext(Dispatchers.IO){
            val response = call()
            if(response.status) Resource.SUCCESS(value = response)
            else Resource.FAILURE(message = response.message)
        }
    }
}