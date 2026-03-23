package com.example.advora.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.advora.model.Ad

class DashboardViewModel : ViewModel() {

    // 🔐 PRIVATE LISTS
    private val _savedAds = mutableStateListOf<Ad>()
    private val _myAds = mutableStateListOf<Ad>()
    private val _allAds = mutableStateListOf(

        Ad(
            title = "Sales Executive Needed",
            description = "Office job",
            price = "₹25,000 - ₹35,000/month",
            location = "Freeganj, Ujjain",
            category = "Jobs",
            imageUrl = "https://images.unsplash.com/photo-1556745757-8d76bdb6984b?w=800"
        ),

        Ad(
            title = "2BHK Flat for Rent",
            description = "Near Mahakal Temple",
            price = "₹8,000/month",
            location = "Mahakal Area, Ujjain",
            category = "Rentals",
            imageUrl = "https://images.unsplash.com/photo-1560185127-6ed189bf02f4?w=800"
        ),

        Ad(
            title = "Bike for Sale",
            description = "Good condition",
            price = "₹30,000",
            location = "Vikram Nagar, Ujjain",
            category = "Buy/Sell",
            imageUrl = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=800"
        )
    )

    // 🔓 PUBLIC READ-ONLY ACCESS
    val savedAds: List<Ad> get() = _savedAds
    val myAds: List<Ad> get() = _myAds
    val allAds: List<Ad> get() = _allAds

    // 🔎 UI STATE
    var selectedCategory by mutableStateOf("All")
        private set

    var searchQuery by mutableStateOf("")
        private set

    // 🔍 FILTER
    val filteredAds: List<Ad>
        get() = _allAds.filter { ad ->
            (selectedCategory == "All" || ad.category == selectedCategory) &&
                    (
                            ad.title.contains(searchQuery, true) ||
                                    ad.location.contains(searchQuery, true)
                            )
        }

    // 🔄 UPDATE CATEGORY
    fun updateCategory(category: String) {
        selectedCategory = category
    }

    // 🔄 UPDATE SEARCH
    fun updateSearch(query: String) {
        searchQuery = query
    }

    // ❤️ SAVE / UNSAVE
    fun toggleSave(ad: Ad) {
        if (_savedAds.contains(ad)) {
            _savedAds.remove(ad)
        } else {
            _savedAds.add(ad)
        }
    }

    fun isSaved(ad: Ad): Boolean = _savedAds.contains(ad)

    // ➕ ADD NEW AD (IMPORTANT)
    fun addAd(
        title: String,
        description: String,
        price: String,
        location: String,
        category: String,
        imageUrl: String
    ) {
        val newAd = Ad(
            title = title,
            description = description,
            price = price,
            location = location,
            category = category,
            imageUrl = imageUrl
        )

        _allAds.add(0, newAd)
        _myAds.add(0, newAd)
    }

    // ❌ DELETE AD (FOR MY ADS SCREEN)
    fun deleteAd(ad: Ad) {
        _myAds.remove(ad)
        _allAds.remove(ad)
        _savedAds.remove(ad)
    }

    // ✏️ UPDATE AD (FUTURE EDIT FEATURE)
    fun updateAd(oldAd: Ad, updatedAd: Ad) {
        val indexAll = _allAds.indexOf(oldAd)
        if (indexAll != -1) _allAds[indexAll] = updatedAd

        val indexMy = _myAds.indexOf(oldAd)
        if (indexMy != -1) _myAds[indexMy] = updatedAd
    }
}