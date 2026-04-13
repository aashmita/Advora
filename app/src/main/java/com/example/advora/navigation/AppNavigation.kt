package com.example.advora.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.advora.screens.*
import com.example.advora.viewmodel.LanguageViewModel
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.AdItem

@Composable
fun AppNavigation(
    languageViewModel: LanguageViewModel
) {
    var selectedAdForEdit by remember { mutableStateOf<AdItem?>(null) }
    var selectedAd by remember { mutableStateOf<AdItem?>(null) }
    var screen by remember { mutableStateOf("login") }

    // Track if the current session is an Admin session
    var isAdminSession by remember { mutableStateOf(false) }

    val adViewModel: AdViewModel = viewModel<AdViewModel>()

    when (screen) {
        "login" -> LoginScreen(
            onLogin = { isAdmin ->
                isAdminSession = isAdmin
                if (isAdmin) {
                    screen = "admin_dashboard"
                } else {
                    screen = "home"
                }
            },
            onRegister = { screen = "register" },
            onForgotPassword = { screen = "forgot" },
            languageViewModel = languageViewModel
        )

        "admin_dashboard" -> AdminDashboardScreen(
            adViewModel = adViewModel,
            languageViewModel = languageViewModel,
            onNavigate = { screen = it },
            onLogout = {
                isAdminSession = false
                screen = "login"
            }
        )

        // ✅ Admin Specific Notification Route
        "admin_notifications" -> AdminNotificationScreen(
            adViewModel = adViewModel,
            languageViewModel = languageViewModel,
            onBack = { screen = "admin_dashboard" }
        )

        "manage_ads" -> ManageAdsScreen(
            adViewModel = adViewModel,
            languageViewModel = languageViewModel,
            onNavigate = { screen = it },
            onAdClick = { ad ->
                selectedAd = ad
                screen = "admin_detail"
            },
            onLogout = { screen = "login" }
        )

        "admin_detail" -> {
            selectedAd?.let { ad ->
                AdminAdDetailScreen(
                    adId = ad.id,
                    adViewModel = adViewModel,
                    onBack = { screen = "manage_ads" }
                )
            } ?: run { screen = "manage_ads" }
        }

        "user_management" -> UserManagementScreen(
            languageViewModel = languageViewModel,
            adViewModel = adViewModel,
            onBack = { screen = "admin_dashboard" }
        )

        "reports" -> ReportsScreen(
            adViewModel = adViewModel,
            languageViewModel = languageViewModel,
            onBack = { screen = "admin_dashboard" }
        )

        "admin_profile" -> AdminProfileScreen(
            languageViewModel = languageViewModel,
            onBack = { screen = "admin_dashboard" },
            onLogout = {
                isAdminSession = false
                screen = "login"
            }
        )

        "map" -> MapScreen(
            adViewModel = adViewModel,
            onBack = { screen = if (isAdminSession) "admin_dashboard" else "home" }
        )

        "home" -> DashboardScreen(
            languageViewModel = languageViewModel,
            adViewModel = adViewModel,
            onNavigate = { screen = it },
            onAdClick = { ad ->
                selectedAd = ad
                screen = "detail"
            }
        )

        "profile" -> {
            if (isAdminSession) {
                AdminProfileScreen(
                    languageViewModel = languageViewModel,
                    onBack = { screen = "admin_dashboard" },
                    onLogout = {
                        isAdminSession = false
                        screen = "login"
                    }
                )
            } else {
                ProfileScreen(
                    adViewModel = adViewModel,
                    languageViewModel = languageViewModel,
                    onBack = { screen = "home" },
                    onNavigate = { screen = it },
                    onLogout = { screen = "login" }
                )
            }
        }

        "forgot" -> ForgotPasswordScreen(
            onBackToLogin = { screen = "login" },
            onResetSuccess = { screen = "home" },
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

        "ads" -> MyAdsScreen(
            adViewModel = adViewModel,
            onBack = { screen = "home" },
            onEditAd = { ad ->
                selectedAdForEdit = ad
                screen = "post"
            },
            onViewDetails = { ad ->
                selectedAd = ad
                screen = "detail"
            }
        )

        "saved" -> SavedScreen(
            onBack = { screen = "home" },
            adViewModel = adViewModel,
            onNavigate = { screen = it },
            isHindi = languageViewModel.isHindi,
            onAdClick = { ad ->
                selectedAd = ad
                screen = "detail"
            }
        )

        "detail" -> {
            selectedAd?.let { ad ->
                AdDetailScreen(
                    ad = ad,
                    onBack = { screen = "home" },
                    adViewModel = adViewModel
                )
            } ?: run { screen = "home" }
        }

        "post" -> PostAdScreen(
            adViewModel = adViewModel,
            languageViewModel = languageViewModel,
            adToEdit = selectedAdForEdit,
            onBack = {
                selectedAdForEdit = null
                screen = "home"
            },
            onNavigate = {
                selectedAdForEdit = null
                screen = it
            }
        )

        // ✅ User Specific Notification Route
        "notifications" -> NotificationScreen(
            adViewModel = adViewModel,
            isHindi = languageViewModel.isHindi,
            onBack = { screen = "home" }
        )
    }
}