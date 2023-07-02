package com.example.neweco.network.api

import com.example.neweco.network.Response
import com.example.neweco.network.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProductApi @Inject constructor() {
    suspend fun getProducts() : Response<List<Product>> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        val response = Response(value = listOf<Product>())
        val colRef = db.collection("products")
        colRef.get()
            .addOnSuccessListener {
                it?.let {
                    response.value = it.toObjects(Product::class.java)
                    response.status = true
                    response.message = "Success!"
                }
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }

    suspend fun getProductByCategory(category: String): Response<List<Product>> = suspendCoroutine {continuation->
        val db = FirebaseFirestore.getInstance()
        val response = Response(value = listOf<Product>())
        val colRef = db.collection("products")
        colRef
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener {
                it?.let {
                    response.value = it.toObjects(Product::class.java)
                    response.status = true
                    response.message = "Success!"
                }
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }
}