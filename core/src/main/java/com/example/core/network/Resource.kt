package com.example.core.network

sealed class Resource<out T> {
    data class SUCCESS<T>(
        val value : T
    ): Resource<T>()

    data class FAILURE(
        val message : String = ""
    ) : Resource<Nothing>()
}