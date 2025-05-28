package com.example.qwizz.data.control

import android.nfc.Tag
import android.util.Log
import com.example.qwizz.data.model.Leaderboard
import com.example.qwizz.data.model.QuizQuestion
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.data.model.User
import com.example.qwizz.data.model.UserStats
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
    private val leaderboardCollection = firestore.collection("leaderboard")


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
            if (!makeLeaderboard(id, title, topic)){
                Log.e(TAG, "Error making leaderboard")
                false
            }else{
                true
            }
        }catch ( e: Exception){
            Log.e(TAG, "Error adding qwizzz", e)
            false
        }

    }

    suspend fun makeLeaderboard(id: String, title: String, topic: String): Boolean{
        return try {
            val qwizId = qwizzzColection
                .whereEqualTo("id", id)
                .whereEqualTo("title", title)
                .whereEqualTo("topic", topic)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.id ?: return false
            val leaderboard = Leaderboard(
                qwizzzId = qwizId,
                leaderboard = emptyList()
            )
            leaderboardCollection.add(leaderboard).await()
            Log.d(TAG, "Leaderboard initial added successfully")
            true
        } catch (e: Exception){
            Log.e(TAG, "Error making leaderboard", e)
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

    suspend fun getTitleQwizzz(title: String, topic: String): String {
        return try {
            val userId = auth.currentUser?.uid ?: return ""
            val userDocRef = qwizzzColection
                .whereEqualTo("id", userId)
                .whereEqualTo("title", title)
                .whereEqualTo("topic", topic)
                .limit(1)
            val querySnapshot = userDocRef.get().await()
            // Ambil dokumen pertama (jika ada)

            val documentSnapshot = querySnapshot.documents.firstOrNull()

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val title = documentSnapshot.getString("title") ?: ""
                Log.d(TAG, "Fetched title: $title")
                title
            } else {
                Log.d(TAG, "No quiz found for user $userId")
                ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching title: ${e.localizedMessage}", e)
            ""
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

    suspend fun updateLeaderboard(qwizzz: Qwizzz, score: Double): Boolean{
        try{

            val userId = auth.currentUser?.uid ?: return false
            val userDocRef = userCollection.whereEqualTo("id", userId).get().await()
            val documentSnapshot = userDocRef.documents.firstOrNull()

            var username = ""
            if (documentSnapshot != null && documentSnapshot.exists()) {
                username = documentSnapshot.getString("name") ?: ""
            }

            val qwizId = qwizzzColection
                .whereEqualTo("id", qwizzz.id)
                .whereEqualTo("title", qwizzz.title)
                .whereEqualTo("topic", qwizzz.topic)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.id ?: return false

            val leaderboardDocRef = leaderboardCollection.whereEqualTo("qwizzzId", qwizId).get().await()
            val leaderboardDoc = leaderboardDocRef.documents.firstOrNull()
            if (leaderboardDoc != null && leaderboardDoc.exists()){
                val leaderboard = leaderboardDoc.toObject(Leaderboard::class.java)
                val updateList = leaderboard?.leaderboard?.toMutableList() ?: mutableListOf()
                val exitingIndex = updateList.indexOfFirst { it.username == username }
                if (exitingIndex != -1) {
                    val exitingScore = updateList[exitingIndex].score
                    if (score > exitingScore){
                        updateList[exitingIndex] = UserStats(
                            username = username,
                            score = score
                        )

                    }
                }else{
                    updateList.add(UserStats(
                        username = username,
                        score = score
                    ))

                }
                leaderboardCollection.document(leaderboardDoc.id).update("leaderboard", updateList).await()
            }
            Log.d(TAG, "Leaderboard updated successfully")
            return true
        } catch (e: Exception){
            Log.e(TAG, "Error fetching title: ${e.localizedMessage}", e)
            return false
        }
    }

    suspend fun getLeaderboard(qwizzzId: String): List<UserStats> {
        return try {
            val querySnapshot = leaderboardCollection.whereEqualTo("qwizzzId", qwizzzId).get().await()
            val leaderboardDoc = querySnapshot.documents.firstOrNull()
            if (leaderboardDoc != null && leaderboardDoc.exists()){
                val leaderboard = leaderboardDoc.toObject(Leaderboard::class.java)

                leaderboard?.leaderboard?.sortedByDescending { it.score } ?: emptyList()
            }else {
                emptyList()
            }

            } catch (e: Exception){
            Log.e(TAG, "Error fetching leaderboard: ${e.localizedMessage}", e)
            emptyList()
        }
    }


}