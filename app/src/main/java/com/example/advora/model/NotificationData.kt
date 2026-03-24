package com.example.advora.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.AccessTime

fun getNotifications(): List<NotificationModel> {
    return listOf(
        NotificationModel(
            title = "New Update Available",
            description = "A new version of the app is ready to download.",
            time = "2h ago",
            buttonText = "Update Now",
            icon = Icons.Filled.Notifications
        ),
        NotificationModel(
            title = "Ad expiring soon",
            description = "Honda Activa",
            time = "8d",
            buttonText = "Renew",
            icon = Icons.Filled.AccessTime
        )
    )
}