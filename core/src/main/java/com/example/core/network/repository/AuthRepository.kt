package com.example.core.network.repository

import com.example.core.network.Resource
import com.example.core.network.Response
import com.example.core.network.api.AuthApi
import com.example.core.network.utils.ApiUtils
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) {
    suspend fun register(mail: String, pass: String, name : String, phone : String) : Resource<Response<FirebaseUser?>> {
        return ApiUtils.safeCall {
            api.registerWithEmail(mail, pass, name, phone)
        }
    }

    suspend fun login(mail: String, pass: String) : Resource<Response<Unit>> {
        return ApiUtils.safeCall {
            api.loginWithEmail(mail, pass)
        }
    }
}