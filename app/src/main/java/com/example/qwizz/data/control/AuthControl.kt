package com.example.qwizz.data.control

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import com.example.qwizz.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date


private const val TAG = "AuthControl"

class AuthControl {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    suspend fun registerUser(username: String, email: String, password: String): Boolean{
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return false

            val user = User(
                id = userId,
                name = username,
                email = email,
                lastLogin = Date().time,
                mathscore = mutableListOf(),
                bahasascore = mutableListOf()
            )
            usersCollection.document(userId).set(user).await()
            Log.d(TAG, "User registered successfully: $userId")
            true
        } catch (e: Exception){
            Log.e("AuthController", "Error registering user", e)

            false

        }
    }
    suspend fun checkUserName(username: String): Boolean {
        return try {
            val querySnapshot = usersCollection.whereEqualTo("name", username).get().await()
            querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("AuthController", "Error checking username", e)
            false
        }
    }

    suspend fun getCurrentUser(): User? {
        val currentUser = auth.currentUser ?: return null

        return try {

            usersCollection.document(currentUser.uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("AuthController", "Error getting user", e)
            null
        }
    }
    fun logoutUser() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}