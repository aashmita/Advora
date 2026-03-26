package com.example.advora.model

data class Ad(

    // OLD FIELDS (for existing code compatibility)
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val category: String = "",

    // NEW FIELDS (for multilingual support)
    val titleEn: String = "",
    val titleHi: String = "",
    val priceEn: String = "",
    val priceHi: String = "",
    val locationEn: String = "",
    val locationHi: String = "",
    val image: String = "",
    val categoryEn: String = "",
    val categoryHi: String = "",

    var isSaved: Boolean = false
)