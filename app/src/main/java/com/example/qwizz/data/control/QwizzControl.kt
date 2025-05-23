package com.example.qwizz.data.control

import android.nfc.Tag
import android.util.Log
import com.example.qwizz.data.model.QuizQuestion
import com.example.qwizz.data.model.Qwizzz
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

private const val TAG = "QwizzControl"
class QwizzControl {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val qwizzzColection = firestore.collection("qwizzz")
    private val userCollection = firestore.collection("users")


    suspend fun addQwizzz(id: String, topic: String, title: String, totalDetik: Int, question: List<QuizQuestion>): Boolean{
        return try{
            val name = userCollection.document(id).get().await().getString("name") ?: ""
            val qwizzz = Qwizzz(
                id = id,
                name = name,
                topic = topic,
                title = title,
                createdAt = Timestamp.now(),
                timeQuiz = totalDetik,
                question = question
            )
            qwizzzColection.add(qwizzz).await()
            Log.d(TAG, "Qwizzz added successfully")
            true
        }catch ( e: Exception){
            Log.e(TAG, "Error adding qwizzz", e)
            false
        }

    }

    suspend fun getQwizzz(): List<Qwizzz> {
        return try {
            val querySnapshot = qwizzzColection.get().await()
            querySnapshot.documents.forEach { doc ->
                Log.d(TAG, "Qwizzz doc data: ${doc.data}")
            }

            Log.d(TAG, "Fetched ${querySnapshot.size()} qwizzz documents")
            querySnapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Qwizzz::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error mapping document to Qwizzz: ${doc.id}", e)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching qwizzz: ${e.localizedMessage}", e)
            emptyList()
        } finally {
            Log.d(TAG, "Fetching qwizzz completed")
        }
    }


}