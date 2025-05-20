package com.example.qwizz.data.control

import android.util.Log
import com.example.qwizz.data.model.Qwizzz
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

private const val TAG = "QwizzControl"
class QwizzControl {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val qwizzzColection = firestore.collection("qwizzz")

    suspend fun addQwizzz( qwizzz: Qwizzz): Boolean{
        return try{
            qwizzzColection.add(qwizzz).await()
            Log.d(TAG, "Qwizzz added successfully")
            true
        }catch ( e: Exception){
            Log.e(TAG, "Error adding qwizzz", e)
            false
        }

    }


}