@file:Suppress("DEPRECATION")

package com.example.qwizz.data.control

import android.content.Context
import android.system.Os
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class CredentialsManager(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs_name",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_REMEMBER_ME = "remember_me"
    }

    fun saveCredentials(username: String, accessToken: String, refreshToken: String?, rememberMe: Boolean) {
        with(sharedPreferences.edit()) {
            putString(KEY_USERNAME, username)
            putString(KEY_ACCESS_TOKEN, accessToken)
            refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
            putBoolean(KEY_REMEMBER_ME, rememberMe)
            apply()
        }
    }

    fun getUsername(): String? = sharedPreferences.getString(KEY_USERNAME, null)
    fun getAccessToken(): String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    fun isRememberMeEnabled(): Boolean = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)

    fun clearCredentials() {
        with(sharedPreferences.edit()) {
            Os.remove(KEY_USERNAME)
            Os.remove(KEY_ACCESS_TOKEN)
            Os.remove(KEY_REFRESH_TOKEN)
            Os.remove(KEY_REMEMBER_ME) // Atau biarkan jika Anda hanya ingin menghapus token
            apply()
        }
    }
}