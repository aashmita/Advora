package com.example.advora.model

data class Report(
    val title: String,
    val user: String,
    val reason: String,
    val date: String,
    var isResolved: Boolean = false
)