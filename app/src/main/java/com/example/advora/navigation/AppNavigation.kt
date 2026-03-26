package com.example.advora.navigation

import androidx.compose.runtime.*
import com.example.advora.screens.*
import com.example.advora.viewmodel.LanguageViewModel

@Composable
fun AppNavigation(
    languageViewModel: LanguageViewModel
) {

    var screen by remember { mutableStateOf("login") }

    when (screen) {

        "login" -> LoginScreen(
            onLogin = { screen = "otp" },
            onRegister = { screen = "register" },
            languageViewModel = languageViewModel
        )

        "register" -> RegisterScreen(
            onSuccess = { screen = "otp" },
            onBack = { screen = "login" },
            languageViewModel = languageViewModel
        )

        "otp" -> OtpScreen(
            onVerify = { screen = "home" },
            onBack = { screen = "register" },
            languageViewModel = languageViewModel
        )

        "home" -> DashboardScreen(
            languageViewModel = languageViewModel,
            onNavigate = { screen = it }
        )


        "ads" -> MyAdsScreen(
            onBack = { screen = "home" }
        )

        "saved" -> SavedScreen(
            onBack = { screen = "home" }
        )

        "post" -> PostAdScreen(
            onBack = { screen = "home" }
        )
    }
}