package com.example.advora.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.advora.viewmodel.AdItem

data class UserItem(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val totalAds: Int,
    val joinDate: String,
    var status: MutableState<String> = mutableStateOf("ACTIVE"),
    val postedAds: SnapshotStateList<AdItem> = mutableStateListOf()
)

val registeredUsers = mutableStateListOf<UserItem>().apply {
    if (isEmpty()) {
        // Helper to create UNIQUE ads for each user
        fun createUserAds(userName: String, userEmail: String, userPhone: String, adCount: Int): SnapshotStateList<AdItem> {
            val ads = mutableStateListOf<AdItem>()
            val items = listOf("iPhone 15", "Gaming Laptop", "Mountain Bike", "DSLR Camera", "Designer Sofa", "Electric Guitar")
            val prices = listOf("₹70,000", "₹1,20,000", "₹15,000", "₹45,000", "₹22,000", "₹35,000")
            val images = listOf(
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9",
                "https://images.unsplash.com/photo-1603302576837-37561b2e2302",
                "https://images.unsplash.com/photo-1485965120184-e220f721d03e",
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32",
                "https://images.unsplash.com/photo-1555041469-a586c61ea9bc",
                "https://images.unsplash.com/photo-1550291652-6ea9114a47b1"
            )

            for (i in 0 until adCount) {
                val index = (0 until items.size).random()
                ads.add(
                    AdItem(
                        id = "ad_${userName}_$i",
                        title = items[index],
                        price = prices[index],
                        category = "Classifieds",
                        location = "Indore",
                        imageUri = images[index],
                        //date = "Apr 12",
                        description = "This is a premium ${items[index]} listed by $userName. It is in excellent condition and comes with all original accessories.",
                        ownerName = userName,
                        ownerEmail = userEmail,
                        ownerPhone = userPhone,
                        status = "Active"
                    )
                )
            }
            return ads
        }

        // Add 5 users, each getting their own generated ad list
        add(UserItem("1", "Rajesh Kumar", "rajesh@example.com", "+91 98765 43210", "1234", 3, "Jan 15, 2024", mutableStateOf("ACTIVE"), createUserAds("Rajesh Kumar", "rajesh@example.com", "+91 98765 43210", 3)))
        add(UserItem("2", "Priya Sharma", "priya@example.com", "+91 98123 45678", "1234", 2, "Feb 20, 2024", mutableStateOf("ACTIVE"), createUserAds("Priya Sharma", "priya@example.com", "+91 98123 45678", 2)))
        add(UserItem("3", "Amit Patel", "amit@example.com", "+91 97654 32109", "1234", 4, "Dec 10, 2023", mutableStateOf("BLOCKED"), createUserAds("Amit Patel", "amit@example.com", "+91 97654 32109", 4)))
        add(UserItem("4", "Suresh Raina", "suresh@example.com", "+91 91234 56789", "1234", 1, "Mar 05, 2024", mutableStateOf("ACTIVE"), createUserAds("Suresh Raina", "suresh@example.com", "+91 91234 56789", 1)))
        add(UserItem("5", "Megha Gupta", "megha@example.com", "+91 99887 76655", "1234", 2, "Jan 22, 2024", mutableStateOf("ACTIVE"), createUserAds("Megha Gupta", "megha@example.com", "+91 99887 76655", 2)))
    }
}