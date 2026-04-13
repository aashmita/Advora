package com.example.advora.model

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val totalAds: Int,
    val joined: String,
    val isActive: Boolean = true
)