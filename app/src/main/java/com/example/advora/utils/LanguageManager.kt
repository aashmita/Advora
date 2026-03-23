package com.example.advora.utils

import androidx.compose.runtime.*

object LanguageManager {
    var isHindi by mutableStateOf(false)

    fun t(en: String, hi: String): String {
        return if (isHindi) hi else en
    }
}