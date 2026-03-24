package com.example.advora.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.advora.screens.*
import com.example.advora.model.Ad
import com.example.advora.viewmodel.DashboardViewModel
import com.example.advora.viewmodel.LanguageViewModel

@Composable
fun AppNavigation() {
    val languageViewModel: LanguageViewModel = viewModel()

    // 🔁 CURRENT SCREEN
    var screen by remember { mutableStateOf("login") }

    // 📦 SELECTED AD
    var selectedItem by remember { mutableStateOf<Ad?>(null) }

    // ✅ SINGLE SHARED VIEWMODEL
    val viewModel: DashboardViewModel = viewModel()

    when (screen) {
        "notification" -> NotificationScreen(languageViewModel)
        // 🔐 LOGIN
        "login" -> LoginScreen(
            onLogin = { screen = "home" },
            onRegister = { screen = "register" }
        )

        // 📝 REGISTER
        "register" -> RegisterScreen(
            onBack = { screen = "login" },
            onOtp = { screen = "otp" }
        )

        // 🔢 OTP
        "otp" -> OtpScreen(
            mobileNumber = "",
            onBack = { screen = "register" },
            onVerify = { screen = "home" }
        )

        // 🏠 HOME
        "home" -> DashboardScreen(
            viewModel = viewModel,
            current = "home",
            onNavigate = { route -> screen = route },
            onItemClick = { ad ->
                selectedItem = ad
                screen = "detail"
            }
        )

        // ➕ POST
        "post" -> PostAdScreen(
            viewModel = viewModel,
            onBack = { screen = "home" }
        )

        // ❤️ SAVED
        "saved" -> SavedScreen(
            viewModel = viewModel,
            onBack = { screen = "home" }
        )

        // 📦 MY ADS (✅ FIXED)
        "ads" -> MyAdsScreen(
            viewModel = viewModel,
            onBack = { screen = "home" }
        )

        // 📄 DETAIL
        "detail" -> {
            selectedItem?.let { ad ->
                DetailScreen(
                    title = ad.title,
                    description = ad.description,
                    price = ad.price,
                    location = ad.location,
                    imageUrl = ad.imageUrl,
                    onBack = { screen = "home" }
                )
            } ?: run {
                screen = "home"
            }
        }
        // 📍 OTHER
        "map" -> PlaceholderScreen("Map") { screen = "home" }
        "profile" -> PlaceholderScreen("Profile") { screen = "home" }

        // 🔁 DEFAULT
        else -> screen = "login"
    }
}