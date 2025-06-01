package com.example.qwizz.data.control

import android.util.Log
import com.example.qwizz.data.model.LeaderboardUser
import com.example.qwizz.data.model.QuizQuestion
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private const val TAG = "QwizzControl"
class QwizzControl {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private val qwizzzColection = firestore.collection("qwizzz")
    private val userCollection = firestore.collection("users")
//    private val leaderboardCollection = firestore.collection("leaderboard")


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
                question = question,
                leaderboard = emptyList()
            )
            qwizzzColection.add(qwizzz).await()
            Log.d(TAG, "Qwizzz added successfully")
            true
        }catch ( e: Exception){
            Log.e(TAG, "Error adding qwizzz", e)
            false
        }

    }

    suspend fun getQwizzzAvailable(): List<Qwizzz> {
        return try {
            val userId = auth.currentUser?.uid ?: return emptyList()
            val userDocRef = qwizzzColection
                .whereEqualTo("id", userId)
            val querySnapshot = userDocRef.get().await()
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

    suspend fun getIdQwizzz(qwizzz: Qwizzz?): String {
        return try {
            val qwizzz = qwizzzColection
                .whereEqualTo("id", qwizzz?.id)
                .whereEqualTo("title", qwizzz?.title)
                .whereEqualTo("topic", qwizzz?.topic)
                .limit(1)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.id ?: ""
            Log.d(TAG, "Fetched qwizzz ID: $qwizzz")
            qwizzz
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching qwizzz ID: ${e.localizedMessage}", e)
            ""
        }
    }


    suspend fun updateLeaderboard(idQwizzz: String, score: Double): Boolean {
        return try {
            val userId = auth.currentUser?.uid ?: ""
            val name = userCollection.document(userId).get().await().getString("name") ?: ""
            val qwizzzDocRef = qwizzzColection.document(idQwizzz)
            val snapshot = qwizzzDocRef.get().await()
            if (snapshot.exists()){
               val qwizzz = snapshot.toObject(Qwizzz::class.java)
                val leaderboard = qwizzz?.leaderboard?.toMutableList()?: mutableListOf()

                val updateUser = LeaderboardUser(name, score)
                val existingIndex = leaderboard.indexOfFirst { it.username == name }

                if (existingIndex != -1){
                    leaderboard[existingIndex] = updateUser
                }else{
                    leaderboard.add(updateUser)
                }

                qwizzzDocRef.update("leaderboard", leaderboard).await()
            }

            Log.d(TAG, "Leaderboard updated successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating leaderboard: ${e.localizedMessage}", e)
            false
            }
    }

    suspend fun getLeaderboard(qwizzzId: String): List<LeaderboardUser> {
        return try {
            val querySnapshot = qwizzzColection.document(qwizzzId).get().await()
            val qwizzz = querySnapshot.toObject(Qwizzz::class.java)?.leaderboard?.sortedByDescending { it.score } ?: emptyList()
            Log.d(TAG, "Fetched ${qwizzz.size} leaderboard documents")
            qwizzz
            } catch (e: Exception){
            Log.e(TAG, "Error fetching leaderboard: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    suspend fun deleteQwizzzById(qwizzzId: String): Boolean {
        return try {
            qwizzzColection.document(qwizzzId).delete().await()
            Log.d(TAG, "Qwizzz deleted successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting qwizzz: ${e.localizedMessage}", e)
            false
        }
    }

     fun observeQwizzz(): Flow<List<Qwizzz>> = callbackFlow {
        val listener = qwizzzColection.whereEqualTo("id", auth.currentUser?.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val qwizzzList = snapshot?.toObjects(Qwizzz::class.java) ?: emptyList()
                trySend(qwizzzList)
            }

        awaitClose {
            listener.remove()
        }
    }

    fun observeCurrentQwizzz(qwizzzId: String): Flow<Qwizzz> = callbackFlow {
        val listener = qwizzzColection
            .document(qwizzzId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val qwizzz = snapshot?.toObject(Qwizzz::class.java)
                if (qwizzz != null) {
                    trySend(qwizzz)
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun updateQwizzz(id: String, qwizzz: Qwizzz): Boolean {
        return try {
            qwizzzColection.document(id).set(qwizzz).await()
            Log.d(TAG, "Qwizzz updated successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating qwizzz: ${e.localizedMessage}", e)
            false
        }
    }


}