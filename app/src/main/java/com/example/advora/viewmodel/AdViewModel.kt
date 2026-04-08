package com.example.advora.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.UUID

data class AdItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val price: String,
    val location: String,
    val category: String,
    val description: String = "",
    val imageUri: String = "",
    val status: String = "Active",
    val postedDate: String = "Just now",
    val ownerName: String = "Advora Official",
    val ownerPhone: String = "+91 98765 XXXX",
    val ownerEmail: String = "seller@advora.com",
    val ownerAddress: String = "Ujjain, MP"
)

class AdViewModel : ViewModel() {

    var ads = mutableStateListOf<AdItem>()

    init {
        // --- 6 ACTIVE/EXPIRING ADS FOR HOME PAGE ---
        ads.add(AdItem(
            title = "Sales Executive Needed",
            price = "₹25,000",
            location = "Ujjain, MP",
            category = "Jobs",
            description = "We are seeking a motivated Sales Executive to join our team. Responsibilities include lead generation, meeting sales targets, and maintaining customer relationships.",
            imageUri = "https://images.unsplash.com/photo-1492724441997-5dc865305da7",
            status = "Active"
        ))

        ads.add(AdItem(
            title = "2BHK Flat near Mahakal",
            price = "₹10,000",
            location = "Ujjain, MP",
            category = "Rentals",
            description = "Spacious 2BHK flat available for rent near Mahakal Temple. Features include 24-hour water supply, secure parking, and a peaceful neighborhood.",
            imageUri = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2",
            status = "Active"
        ))

        ads.add(AdItem(
            title = "Royal Enfield Classic 350",
            price = "₹1,20,000",
            location = "Ujjain, MP",
            category = "Vehicles",
            description = "Well-maintained Royal Enfield Classic 350 in excellent condition. Black color, low mileage, and single owner. All documents are up to date.",
            imageUri = "https://images.unsplash.com/photo-1558981806-ec527fa84c39",
            status = "Active"
        ))

        ads.add(AdItem(
            title = "iPhone 13 (Like New)",
            price = "₹42,000",
            location = "Ujjain, MP",
            category = "Buy/Sell",
            description = "iPhone 13 available in pristine condition. No scratches, 128GB storage, includes original box and accessories. Battery health is at 95%.",
            imageUri = "https://images.unsplash.com/photo-1632661674596-df8be070a5c5",
            status = "Active"
        ))

        // POSITION 5: MACBOOK AIR M1 (Last-Second for Home Screen)
        ads.add(AdItem(
            title = "MacBook Air M1",
            price = "₹65,000",
            location = "Indore, MP",
            category = "Electronics",
            description = "Apple MacBook Air with M1 chip. 8GB RAM, 256GB SSD. Lightweight, powerful, and in perfect working condition. Great for students and creative professionals.",
            imageUri = "https://images.unsplash.com/photo-1517336714460-4c50422b3f14",
            status = "Expiring"
        ))

        ads.add(AdItem(
            title = "Study Table & Chair",
            price = "₹4,500",
            location = "Jaipur",
            category = "Furniture",
            description = "Ergonomic study table and chair set. Sturdy wooden table with drawer and a comfortable adjustable chair. Perfect for home office or students.",
            imageUri = "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd",
            status = "Active"
        ))

        // --- 3 SOLD ADS ---
        ads.add(AdItem(
            title = "Sony WH-1000XM5",
            price = "₹22,000",
            location = "Pune",
            category = "Electronics",
            description = "Industry-leading noise cancelling headphones. Gently used, original packaging included.",
            imageUri = "https://images.unsplash.com/photo-1618366712277-72202c6313a0",
            status = "Sold"
        ))

        ads.add(AdItem(
            title = "Mountain Bike",
            price = "₹12,000",
            location = "Bhopal",
            category = "Vehicles",
            description = "Durable mountain bike with 21 gears and front suspension. Sold to a local buyer.",
            imageUri = "https://images.unsplash.com/photo-1485965120184-e220f721d03e",
            status = "Sold"
        ))

        ads.add(AdItem(
            title = "Gaming Laptop RTX 4060",
            price = "₹95,000",
            location = "Delhi",
            category = "Electronics",
            description = "High-performance gaming laptop with RTX 4060. Sold after 6 months of use.",
            imageUri = "https://images.unsplash.com/photo-1603302576837-37561b2e2302",
            status = "Sold"
        ))

        // --- 3 INACTIVE ADS ---
        ads.add(AdItem(
            title = "Luxury 2BHK Flat",
            price = "₹65,00,000",
            location = "Bangalore",
            category = "Real Estate",
            description = "Premium luxury flat in the heart of Bangalore. Currently off-market.",
            imageUri = "https://images.unsplash.com/photo-1568605114967-8130f3a36994",
            status = "Inactive"
        ))

        ads.add(AdItem(
            title = "Smart Watch Series 9",
            price = "₹32,000",
            location = "Chennai",
            category = "Fashion",
            description = "Latest series smart watch. Listing paused by seller.",
            imageUri = "https://images.unsplash.com/photo-1544117518-30dd44b99d08",
            status = "Inactive"
        ))

        ads.add(AdItem(
            title = "Graphic Designer",
            price = "₹30,000",
            location = "Indore, MP",
            category = "Jobs",
            description = "Hiring paused for the graphic designer position.",
            imageUri = "https://images.unsplash.com/photo-1572044162444-ad60f128bdea",
            status = "Inactive"
        ))
    }

    // Home Dashboard Helper
    fun getHomeAds() = ads.filter { it.status == "Active" || it.status == "Expiring" }

    // Notification State
    var hasNewNotifications by mutableStateOf(false)
        private set

    fun setNewNotification(value: Boolean) { hasNewNotifications = value }

    // Saved Ads Logic
    var savedAds = mutableStateListOf<AdItem>()
        private set

    fun toggleSave(ad: AdItem) {
        if (savedAds.any { it.id == ad.id }) {
            savedAds.removeAll { it.id == ad.id }
        } else {
            savedAds.add(ad)
        }
    }

    fun isSaved(ad: AdItem): Boolean {
        return savedAds.any { it.id == ad.id }
    }

    // Ad Management
    fun addAd(ad: AdItem) { ads.add(0, ad) }

    fun deleteAd(adId: String) {
        ads.removeAll { it.id == adId }
        savedAds.removeAll { it.id == adId }
    }

    fun updateAdStatus(adId: String, newStatus: String) {
        val index = ads.indexOfFirst { it.id == adId }
        if (index != -1) {
            ads[index] = ads[index].copy(status = newStatus)
        }
    }

    var isDarkMode by mutableStateOf(false)
        private set

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }
    fun updateAdDetails(updatedAd: AdItem) {
        val index = ads.indexOfFirst { it.id == updatedAd.id }
        if (index != -1) {
            ads[index] = updatedAd
        }
    }
}