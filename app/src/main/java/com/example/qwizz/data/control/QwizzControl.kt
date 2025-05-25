package com.example.qwizz.data.control

import android.nfc.Tag
import android.util.Log
import com.example.qwizz.data.model.QuizQuestion
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

private const val TAG = "QwizzControl"
class QwizzControl {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
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

    fun updateUserScore(score: Double, topic: String) {
        val userId = auth.currentUser?.uid ?: return
        val userDocRef = userCollection.document(userId)

        // 1. Ambil dokumen pengguna dari Firestore
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    if (user != null) {
                        when (topic) {
                            "mathscore" -> user.mathscore.add(score)
                            "bahasascore" -> user.bahasascore.add(score)
                            else -> {
                                Log.e(TAG, "Unknown topic: $topic. Score not added.")
                                return@addOnSuccessListener
                            }
                        }

                        userDocRef.update(mapOf(topic to when(topic) {
                            "mathscore" -> user.mathscore
                            "bahasascore" -> user.bahasascore
                            else -> emptyList<Double>()
                        }))
                            .addOnSuccessListener {
                                Log.d(TAG, "Score $score added to $topic successfully (including duplicates).")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error updating $topic after local modification for user $userId", e)
                            }
                    } else {
                        Log.d(TAG, "Failed to parse user document to User object for user $userId.")
                    }
                } else {
                    Log.d(TAG, "User document does not exist for user $userId.")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching user document for user $userId", e)
            }
    }


}