package com.example.advora.utils // or your package

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.advora.screens.UserItem

object SessionManager {
    // This will hold the user who just registered or logged in
    var currentUser by mutableStateOf<UserItem?>(null)
}