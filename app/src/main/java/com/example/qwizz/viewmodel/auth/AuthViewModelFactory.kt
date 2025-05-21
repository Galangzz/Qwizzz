package com.example.qwizz.viewmodel.auth

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val prefs = context.applicationContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return AuthViewModel(prefs) as T
    }
}