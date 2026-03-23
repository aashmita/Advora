package com.example.advora.model

data class Ad(
    val title: String,
    val description: String,
    val price: String,
    val location: String,
    val category: String,
    val imageUrl: String? = null
)