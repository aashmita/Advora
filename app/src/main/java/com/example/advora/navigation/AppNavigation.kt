package com.example.advora.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.advora.screens.*
import com.example.advora.model.Ad
import com.example.advora.viewmodel.DashboardViewModel
import com.example.advora.screens.MapScreen
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavigation() {
    val context = LocalContext.current

    // 🔁 CURRENT SCREEN
    var screen by remember { mutableStateOf("login") }





    // 📦 SELECTED AD
    var selectedItem by remember { mutableStateOf<Ad?>(null) }

    // ✅ SINGLE SHARED VIEWMODEL
    val viewModel: DashboardViewModel = viewModel()

    when (screen) {
        "user_management" -> UserManagementScreen(
            onBack = { screen = "admin_dashboard" },
            onNavigate = { screen = it }
        )
        "user_detail" -> PlaceholderScreen("User Details") {
            screen = "user_management"
        }

        // 🔐 LOGIN
        "login" -> LoginScreen(
            onLogin = { email, password ->

                if (email == "admin@gmail" && password == "admin123") {
                    Toast.makeText(context, "Admin Login", Toast.LENGTH_SHORT).show()
                    screen = "admin_dashboard"
                } else {
                    screen = "home"
                }
            },
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
        "admin_dashboard" -> AdminDashboardScreen(
            onNavigate = { screen = it }
        )


        "map" -> MapScreen(
            onNavigate = { screen = it },
            isAdmin = false
        )
        "admin_map" -> MapScreen(
            onNavigate = { screen = it },
            isAdmin = true
        )
        "reports" -> ReportsScreen(
            onNavigate = { screen = it }
        )
        "manage_ads" -> ManageAdsScreen(
            onNavigate = { screen = it }
        )

        "profile" -> ProfileScreen(
            onNavigate = { screen = it }
        )


        else -> screen = "login"
    }
}