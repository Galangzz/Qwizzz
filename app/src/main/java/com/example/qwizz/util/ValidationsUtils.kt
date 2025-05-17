package com.example.qwizz.util

import android.util.Patterns
import java.util.regex.Pattern

object ValidationsUtils {
    private val PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    )

    private val USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{3,20}$")

    fun validateEmail(email: String): Pair<Boolean, String?> {
        if (email.isBlank()) {
            return Pair(false, "Email tidak boleh kosong")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Pair(false, "Format email tidak valid")
        }

        return Pair(true, null)
    }

    fun validatePassword(password: String): Pair<Boolean, String?> {
        if (password.isBlank()) {
            return Pair(false, "Password tidak boleh kosong")
        }

        if (password.length < 8) {
            return Pair(false, "Password minimal 8 karakter")
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return Pair(
                false,
                "Password harus berisi minimal 1 huruf besar, 1 huruf kecil, " +
                        "1 angka, dan 1 karakter khusus (@#$%^&+=!)"
            )
        }

        return Pair(true, null)
    }

    fun validateUsername(username: String): Pair<Boolean, String?> {
        if (username.isBlank()) {
            return Pair(false, "Username tidak boleh kosong")
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return Pair(
                false,
                "Username hanya boleh berisi huruf, angka, dan karakter . _ -" +
                        " dengan panjang 3-20 karakter"
            )
        }

        return Pair(true, null)
    }

    fun sanitizeUsername(username: String): String {
        var sanitized = username.trim().lowercase().replace(" ", "_")
        return sanitized
    }
}