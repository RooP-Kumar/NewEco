package com.example.neweco.network.repository

import com.example.neweco.network.Resource
import com.example.neweco.network.Response
import com.example.neweco.network.api.AuthApi
import com.example.neweco.network.utils.ApiUtils
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) {
    suspend fun register(mail: String, pass: String) : Resource<Response<FirebaseUser?>> {
        return ApiUtils.safeCall {
            api.registerWithEmail(mail, pass)
        }
    }

    suspend fun login(mail: String, pass: String) : Resource<Response<Unit>> {
        return ApiUtils.safeCall {
            api.loginWithEmail(mail, pass)
        }
    }
}