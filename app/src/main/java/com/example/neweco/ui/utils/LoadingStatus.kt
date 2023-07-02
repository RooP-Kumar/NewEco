package com.example.neweco.ui.utils

data class LoadingStatus(
    val showLoading: Boolean = false,
    val result : Boolean? = null, // positive or negative
    val message : String = ""
)