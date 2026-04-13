package com.example.advora.model

data class AdminAd(
    val title: String,
    val user: String,
    val price: String,
    val image: String,
    val category: String,
    val isApproved: Boolean = false
)