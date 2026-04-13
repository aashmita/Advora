package com.example.advora.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import java.util.UUID

// Notification Data Model
data class NotificationData(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val time: String = "",
    var isRead: Boolean = false,
    val dotColor: Color = Color.Gray,
    val detailTitle: String = "",
    val detailPrice: String = "",
    val detailLocation: String = "",
    val detailDesc: String = "",
    val status: String = "",
    val imageUrl: String? = null
)

data class AdItem(
    var id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val price: String = "",
    val location: String = "",
    val category: String = "",
    val description: String = "",
    val imageUri: String = "",
    var status: String = "Pending", // "Pending", "Active", "Rejected", "Sold", "Inactive"
    val postedDate: String = "",
    val rejectionReason: String? = null,
    val ownerName: String = "Advora Official",
    val ownerPhone: String = "+91 98765 XXXX",
    val ownerEmail: String = "seller@advora.com",
    val ownerAddress: String = "Ujjain, MP",
    val userId: String = "user_123"
)

class AdViewModel : ViewModel() {

    var ads = mutableStateListOf<AdItem>()

    // Global Notification State
    var notifications = mutableStateListOf<NotificationData>()
        private set

    init {
        // --- EXISTING STATIC DATA ---
        ads.add(AdItem(
            title = "Sales Executive Needed",
            price = "₹25,000",
            location = "Ujjain, MP",
            category = "Jobs",
            description = "We are seeking a motivated Sales Executive to join our team...",
            imageUri = "https://images.unsplash.com/photo-1492724441997-5dc865305da7",
            status = "Active",
            postedDate = "2 days ago"
        ))

        ads.add(AdItem(
            title = "2BHK Flat near Mahakal",
            price = "₹10,000",
            location = "Ujjain, MP",
            category = "Real Estate",
            description = "Spacious 2BHK flat available for rent near Mahakal Temple...",
            imageUri = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2",
            status = "Active",
            postedDate = "1 day ago"
        ))

        ads.add(AdItem(
            title = "Royal Enfield Classic 350",
            price = "₹1,20,000",
            location = "Ujjain, MP",
            category = "Vehicles",
            description = "Well-maintained Royal Enfield Classic 350...",
            imageUri = "https://images.unsplash.com/photo-1558981806-ec527fa84c39",
            status = "Active",
            postedDate = "3 hours ago"
        ))

        ads.add(AdItem(
            title = "iPhone 13 (Like New)",
            price = "₹42,000",
            location = "Ujjain, MP",
            category = "Electronics",
            description = "iPhone 13 available in pristine condition...",
            imageUri = "https://images.unsplash.com/photo-1632661674596-df8be070a5c5",
            status = "Active",
            postedDate = "Just now"
        ))

        ads.add(AdItem(
            title = "MacBook Air M1",
            price = "₹65,000",
            location = "Indore, MP",
            category = "Electronics",
            description = "Apple MacBook Air with M1 chip...",
            imageUri = "https://images.unsplash.com/photo-1517336714460-4c50422b3f14",
            status = "Active",
            postedDate = "5 days ago"
        ))

        ads.add(AdItem(
            title = "Study Table & Chair",
            price = "₹4,500",
            location = "Jaipur, RJ",
            category = "Furniture",
            description = "Ergonomic study table and chair set...",
            imageUri = "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd",
            status = "Active",
            postedDate = "1 week ago"
        ))

        // --- PRE-EXISTING PENDING ADS ---
        ads.add(AdItem(
            title = "Samsung Galaxy S23 Ultra",
            price = "₹85,000",
            location = "Indore, MP",
            category = "Electronics",
            description = "Brand new condition with bill and box.",
            status = "Pending",
            postedDate = "5 mins ago",
            imageUri = "https://images.unsplash.com/photo-1678911820864-e2c567c655d7"
        ))

        ads.add(AdItem(
            title = "Office Receptionist",
            price = "₹15,000",
            location = "Ujjain, MP",
            category = "Jobs",
            description = "Need a front desk receptionist for a local clinic.",
            status = "Pending",
            postedDate = "1 hour ago",
            imageUri = "https://images.unsplash.com/photo-1521791136366-398b7cc80d0d"
        ))

        // --- PRE-EXISTING REJECTED ADS ---
        ads.add(AdItem(
            title = "Second Hand Tires",
            price = "₹2,000",
            location = "Ratlam, MP",
            category = "Vehicles",
            status = "Rejected",
            rejectionReason = "Items violate safety guidelines.",
            postedDate = "Yesterday",
            imageUri = "https://images.unsplash.com/photo-1584281722572-870026600c8b"
        ))

        ads.add(AdItem(
            title = "Used Sofa Set",
            price = "₹8,000",
            location = "Dewas, MP",
            category = "Furniture",
            status = "Rejected",
            rejectionReason = "Images are not clear / Low quality.",
            postedDate = "2 days ago",
            imageUri = "https://images.unsplash.com/photo-1555041469-a586c61ea9bc"
        ))

        // --- 3 SOLD ADS ---
        ads.add(AdItem(title = "Sony WH-1000XM5", price = "₹22,000", location = "Pune, MH", category = "Electronics", status = "Sold", postedDate = "1 month ago", imageUri = "https://images.unsplash.com/photo-1618366712277-72202c6313a0"))
        ads.add(AdItem(title = "Mountain Bike", price = "₹12,000", location = "Bhopal, MP", category = "Vehicles", status = "Sold", postedDate = "2 weeks ago", imageUri = "https://images.unsplash.com/photo-1485965120184-e220f721d03e"))
        ads.add(AdItem(title = "Gaming Laptop RTX 4060", price = "₹95,000", location = "Delhi", category = "Electronics", status = "Sold", postedDate = "3 weeks ago", imageUri = "https://images.unsplash.com/photo-1603302576837-37561b2e2302"))

        // --- 3 INACTIVE ADS ---
        ads.add(AdItem(title = "Luxury 2BHK Flat", price = "₹65,00,000", location = "Bangalore, KA", category = "Real Estate", status = "Inactive", postedDate = "2 months ago", imageUri = "https://images.unsplash.com/photo-1568605114967-8130f3a36994"))
        ads.add(AdItem(title = "Smart Watch Series 9", price = "₹32,000", location = "Chennai, TN", category = "Fashion", status = "Inactive", postedDate = "4 days ago", imageUri = "https://images.unsplash.com/photo-1544117518-30dd44b99d08"))
        ads.add(AdItem(title = "Graphic Designer", price = "₹30,000", location = "Indore, MP", category = "Jobs", status = "Inactive", postedDate = "10 days ago", imageUri = "https://images.unsplash.com/photo-1572044162444-ad60f128bdea"))
    }

    // Home Dashboard Helper
    fun getHomeAds() = ads.filter { it.status == "Active" }

    // Notification State
    var hasNewNotifications by mutableStateOf(false)
        private set

    fun setNewNotification(value: Boolean) { hasNewNotifications = value }

    private fun addNotificationInternal(notification: NotificationData) {
        notifications.add(0, notification)
        setNewNotification(true)
    }

    // Admin Notification Logic
    fun addAdminNotification(title: String, message: String) {
        val notif = NotificationData(
            id = System.currentTimeMillis().toInt(),
            title = title,
            message = message,
            time = "Just now",
            isRead = false,
            dotColor = Color.Red,
            detailTitle = title,
            detailPrice = "",
            detailLocation = "Admin Panel",
            detailDesc = message,
            status = "REPORT",
            imageUrl = null
        )
        addNotificationInternal(notif)
    }

    // User Notification Logic
    fun addUserNotification(title: String, message: String, userId: String) {
        val notif = NotificationData(
            id = System.currentTimeMillis().toInt(),
            title = title,
            message = message,
            time = "Just now",
            isRead = false,
            dotColor = Color.Blue,
            detailTitle = title,
            detailPrice = "",
            detailLocation = "System",
            detailDesc = message,
            status = "INFO",
            imageUrl = null
        )
        addNotificationInternal(notif)
    }

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

    // Local Add Ad logic
    fun addAd(ad: AdItem) {
        ads.add(0, ad)
        addUserNotification("Ad Posted", "${ad.title} is pending review.", ad.userId)
    }

    // Local Delete logic
    fun deleteAd(adId: String) {
        ads.removeAll { it.id == adId }
        savedAds.removeAll { it.id == adId }
    }

    // Local Update status logic
    fun updateAdStatus(adId: String, newStatus: String) {
        val index = ads.indexOfFirst { it.id == adId }
        if (index != -1) {
            ads[index] = ads[index].copy(status = newStatus)
        }
    }

    fun updateAdDetails(updatedAd: AdItem) {
        val index = ads.indexOfFirst { it.id == updatedAd.id }
        if (index != -1) {
            ads[index] = updatedAd
        }
    }

    var isDarkMode by mutableStateOf(false)
        private set

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    // --- Reports Management ---
    var reportedAds = mutableStateListOf<Pair<AdItem, String>>()
        private set

    var resolvedReportIds = mutableStateListOf<String>()
        private set

    fun reportAd(ad: AdItem, reason: String) {
        reportedAds.add(0, ad to reason)
        resolvedReportIds.remove(ad.id)
        addAdminNotification("New Ad Report", "Ad '${ad.title}' has been reported for: $reason")
    }

    fun resolveReport(adId: String) {
        if (!resolvedReportIds.contains(adId)) {
            resolvedReportIds.add(adId)
        }
    }
}