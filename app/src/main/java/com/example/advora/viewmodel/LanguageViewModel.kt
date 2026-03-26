package com.example.advora.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LanguageViewModel : ViewModel() {

    var isHindi by mutableStateOf(false)
        private set

    fun changeLanguage(value: Boolean) {
        isHindi = value
    }

    fun toggleLanguage() {
        isHindi = !isHindi
    }
}