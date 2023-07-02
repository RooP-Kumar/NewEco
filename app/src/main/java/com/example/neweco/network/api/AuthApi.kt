package com.example.neweco.network.api

import com.example.neweco.network.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthApi @Inject constructor() {
    suspend fun registerWithEmail(mail : String, pass : String) : Response<FirebaseUser?> = suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        val response = Response(value = auth.currentUser)

        auth.createUserWithEmailAndPassword(mail, pass)
            .addOnSuccessListener {
                response.value = it.user
                it.user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        response.status = true
                        response.message = "verification email has been sent to your email."
                        continuation.resume(response)
                    }
                    ?.addOnFailureListener {e->
                        response.status = false
                        response.message = e.message.toString()
                        continuation.resume(response)
                    }
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }

    suspend fun loginWithEmail(mail: String, pass: String) : Response<Unit> = suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val response = Response(value = Unit)
        if (user?.isEmailVerified!!) {
            auth.signInWithEmailAndPassword(mail, pass)
                .addOnSuccessListener {
                    response.status = true
                    response.message = it.user?.uid.toString()
                    continuation.resume(response)
                }
                .addOnFailureListener {
                    response.status = false
                    response.message = it.message.toString()
                    continuation.resume(response)
                }
        } else {
            response.status = false
            response.message = "Please verify your email."
            continuation.resume(response)
        }
    }

}