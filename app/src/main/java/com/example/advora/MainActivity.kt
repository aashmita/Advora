package com.example.advora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider

import com.example.advora.navigation.AppNavigation
import com.example.advora.ui.theme.AdvoraTheme
import com.example.advora.viewmodel.LanguageViewModel
import com.example.advora.utils.LocalLanguage

class MainActivity : ComponentActivity() {

    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val isHindi = languageViewModel.isHindi

            CompositionLocalProvider(
                LocalLanguage provides isHindi
            ) {
                AdvoraTheme {
                    AppNavigation(languageViewModel)
                }
            }
        }
    }
}