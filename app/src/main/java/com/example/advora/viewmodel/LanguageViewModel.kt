package com.example.advora.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            _language.value = lang
        }
    }
}