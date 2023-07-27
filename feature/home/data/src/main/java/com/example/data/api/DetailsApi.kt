package com.example.data.api

import com.example.core.network.Response
import com.example.core.network.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsApi @Inject constructor() {

    suspend fun getProductById(productId: String) : Response<Product> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        val colRef = db.collection("products")
        val response = Response(value = Product())

        colRef.document(productId).get()
            .addOnSuccessListener {
                response.value = it.toObject(Product::class.java) ?: Product()
                response.message = "Success!"
                response.status = true
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }

}