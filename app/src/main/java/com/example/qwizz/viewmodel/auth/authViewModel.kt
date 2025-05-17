package com.example.qwizz.viewmodel.auth

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwizz.data.control.AuthControl
import com.example.qwizz.data.model.User
import com.example.qwizz.util.ValidationsUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val TAG = "AuthViewModel"

class AuthViewModel: ViewModel() {

    private val authControl: AuthControl = AuthControl()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    val isLoading = mutableStateOf(false)

    init {
        auth.currentUser?.let { firebaseUser -> refreshCurrentUser() }
    }


    fun registerUser(username: String, email: String, password: String, onResult: (succes: Boolean, message: String?) -> Unit) {
        val usernameValidation = ValidationsUtils.validateUsername(username)
        val emailValidation = ValidationsUtils.validateEmail(email)
        val passwordValidation = ValidationsUtils.validatePassword(password)

        if (!usernameValidation.first || !emailValidation.first || !passwordValidation.first) {
            _authState.value = AuthState.Error(
                usernameValidation.second ?: emailValidation.second ?: passwordValidation.second
                ?: "Username, email, or password is invalid"
            )

            Log.d(ContentValues.TAG, "Register failed: ${usernameValidation.second ?: emailValidation.second ?: passwordValidation.second}")
            onResult(false, usernameValidation.second ?: emailValidation.second ?: passwordValidation.second)
            return
        }
        Log.d(ContentValues.TAG, "Registering user with username: $username, email: $email")

        val sanitizedUsername = ValidationsUtils.sanitizeUsername(username)

        viewModelScope.launch {
            isLoading.value = true
            _authState.value = AuthState.Loading
            try {
                val result = authControl.registerUser(sanitizedUsername, email, password)
                if (result) {
                    Log.d(ContentValues.TAG, "Register success email : $email")
                    _authState.value = AuthState.Authenticated

                    onResult(true, "Register success")
                } else {
                    _authState.value = AuthState.Error("Register failed")
                    onResult(false, "Register failed")
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Register failed: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
                onResult(false, e.message)
            } finally {
                isLoading.value = false
            }
        }
    }


     fun loginUser(email: String, password: String, onResult: (success: Boolean, message: String?) -> Unit) {
        try{
            val emailValidation = ValidationsUtils.validateEmail(email)
            if (!emailValidation.first) {
                _authState.value = AuthState.Error(emailValidation.second ?: "Email tidak valid")
                onResult(false, emailValidation.second)
                return
            }
            isLoading.value = true
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isLoading.value = false
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "Login success")
                        _authState.value = AuthState.Authenticated
                        _userEmail.value = email
                        onResult(true, "Login success")

                    } else {
                        Log.e(ContentValues.TAG, "Login failed: ${task.exception?.message}")
                        _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                        onResult(false, "Incorrect email or password")
                    }
                }


        }
        catch (e: Exception){
            Log.e(ContentValues.TAG, "Login failed: ${e.message}")
            _authState.value = AuthState.Error(e.message ?: "Unknown error")
            onResult(false, e.message)
        }

    }

    fun logoutUser() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                authControl.logoutUser()
                _authState.value = AuthState.Unauthenticated
                _currentUser.value = null
                _userEmail.value = ""
                Log.d(ContentValues.TAG, "Logout success")
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Logout failed: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }




    private fun refreshCurrentUser() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val user = authControl.getCurrentUser()
                _currentUser.value = user
                _authState.value = if (user != null) AuthState.Authenticated else AuthState.Unauthenticated
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error refreshing user: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            } finally {
                isLoading.value = false
            }
        }
    }


}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}