package com.example.advora.utils

import androidx.compose.runtime.Composable

@Composable
fun getText(english: String, hindi: String): String {
    val isHindi = LocalLanguage.current
    return if (isHindi) hindi else english
}