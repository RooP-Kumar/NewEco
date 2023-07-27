package com.example.core.network.data

data class User(
    val id : String = "",
    val cart : List<String> = emptyList(), // This is basically the user cart so this will be not of String type but some custom data class type of user cart details (change later)
    val email : String = "",
    val name : String = "",
    val phone : String = ""
)
