package com.example.advora.navigation

import androidx.compose.runtime.*
import com.example.advora.screens.*

@Composable
fun AppNavigation() {

    var screen by remember { mutableStateOf("login") }
    var phone by remember { mutableStateOf("") }

    when (screen) {

        "login" -> LoginScreen(
            onLogin = { screen = "dashboard" },
            onRegister = { screen = "register" }
        )

        "register" -> RegisterScreen(
            onBack = { screen = "login" },
            onOtp = {
                phone = it
                screen = "otp"
            }
        )

        "otp" -> OtpScreen(
            phone = phone,
            onBack = { screen = "register" },
            onSuccess = { screen = "dashboard" }
        )

        "dashboard" -> DashboardScreen(
            onLogout = { screen = "login" }
        )
    }
}