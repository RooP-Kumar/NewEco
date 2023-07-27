package com.example.core.network

data class Response<T>(
    var value :  T,
    var status : Boolean = false,
    var message : String = ""
)