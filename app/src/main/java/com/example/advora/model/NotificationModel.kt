package com.example.advora.model
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.advora.model.NotificationModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
data class NotificationModel(
    val title: String,
    val description: String,
    val time: String,
    val buttonText: String?, // Reference to R.string.description
    val icon: ImageVector,
    )