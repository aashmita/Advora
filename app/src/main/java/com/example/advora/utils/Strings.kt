package com.example.advora.utils

import androidx.compose.runtime.Composable

object Strings {

    fun get(key: String, isHindi: Boolean): String {
        return if (isHindi) {
            hindi[key] ?: key
        } else {
            english[key] ?: key
        }
    }

    private val english = mapOf(
        "search" to "Search ads...",
        "post_ad" to "Post New Ad",
        "title" to "Title",
        "description" to "Description",
        "price" to "Price",
        "location" to "Location",
        "city" to "City",
        "area" to "Area",
        "contact" to "Contact",
        "upload_image" to "Tap to upload image",
        "submit" to "Submit",
        "jobs" to "Jobs",
        "rentals" to "Rentals",
        "buy_sell" to "Buy/Sell",
        "my_ads" to "My Ads",
        "profile" to "Profile",
        "home" to "Home"
    )

    private val hindi = mapOf(
        "search" to "विज्ञापन खोजें...",
        "post_ad" to "विज्ञापन डालें",
        "title" to "शीर्षक",
        "description" to "विवरण",
        "price" to "मूल्य",
        "location" to "स्थान",
        "city" to "शहर",
        "area" to "क्षेत्र",
        "contact" to "संपर्क",
        "upload_image" to "तस्वीर जोड़ने के लिए टैप करें",
        "submit" to "सबमिट करें",
        "jobs" to "नौकरियां",
        "rentals" to "किराये",
        "buy_sell" to "खरीदें/बेचें",
        "my_ads" to "मेरे विज्ञापन",
        "profile" to "प्रोफाइल",
        "home" to "होम"
    )
}