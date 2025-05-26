package com.example.qwizz.data.control

import com.example.qwizz.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class StatsControl {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val statsCollection = firestore.collection("users")

    suspend fun getDataUser(userId: String): User? {
        return try {
            val documentSnapshot = statsCollection.document(userId).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}