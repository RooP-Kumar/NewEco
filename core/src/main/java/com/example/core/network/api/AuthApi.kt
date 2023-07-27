package com.example.core.network.api

import com.example.core.network.Response
import com.example.core.network.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthApi @Inject constructor() {
    suspend fun registerWithEmail(mail : String, pass : String, name : String, phone : String) : Response<FirebaseUser?> = suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val response = Response(value = auth.currentUser)

        auth.createUserWithEmailAndPassword(mail, pass)
            .addOnSuccessListener {result ->
                response.value = result.user
                result.user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        db.collection("users").document(result.user?.uid!!)
                            .set(User(result.user?.uid!!, emptyList(), mail, name, phone))
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
        val response = Response(value = Unit)
        auth.signInWithEmailAndPassword(mail, pass)
            .addOnSuccessListener {
                val user = it.user
                if(user != null && user.isEmailVerified) {
                    response.status = true
                    response.message = "Successfully! Login"
                    continuation.resume(response)
                } else {
                    response.status = false
                    response.message = "Please! verify your email."
                    continuation.resume(response)
                }
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }

    }

}