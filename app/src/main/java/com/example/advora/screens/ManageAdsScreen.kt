package com.example.advora.screens

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// 🔥 SIMPLE DATA MODEL (SAFE)
data class AdminAd(
    val title: String,
    val user: String,
    val price: String,
    val imageUrl: String,
    val category: String,
    val isApproved: Boolean = false
)

@Composable
fun ManageAdsScreen(onNavigate: (String) -> Unit) {

    var selectedTab by remember { mutableStateOf("all") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var actionType by remember { mutableStateOf("") } // "approve" or "reject"

    var ads by remember {
        mutableStateOf(
            listOf(
                AdminAd("iPhone 14 Pro Max 256GB", "Rajesh Kumar", "₹85,000", "", "buy"),
                AdminAd("Honda City 2020 VX Model", "Priya Sharma", "₹12,50,000", "", "buy"),
                AdminAd("3BHK Flat in Prime Location", "Amit Patel", "₹85,00,000", "", "rent"),
                AdminAd("MacBook Pro M2 16-inch", "Sneha Desai", "₹1,95,000", "", "buy"),
                AdminAd("Royal Enfield Classic 350", "Vikram Singh", "₹1,65,000", "", "buy"),
                AdminAd("L-Shape Sofa Set", "Lakshmi Iyer", "₹45,000", "", "rent")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9CA3AF))
    ) {

        // 🔝 HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowBack, null, tint = Color(0xFFB96B4C),
                modifier = Modifier.clickable { onNavigate("admin_dashboard") })

            Spacer(Modifier.width(10.dp))

            Text("Manage Ads", color = Color(0xFFB96B4C))
        }

        // 🔥 FILTER TABS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip("All", selectedTab == "all") { selectedTab = "all" }
            FilterChip("Jobs", selectedTab == "jobs") { selectedTab = "jobs" }
            FilterChip("Rentals", selectedTab == "rent") { selectedTab = "rent" }
            FilterChip("Buy/Sell", selectedTab == "buy") { selectedTab = "buy" }
        }

        // 🔥 LIST
        LazyColumn(
            contentPadding = PaddingValues(12.dp)
        ) {

            itemsIndexed(ads) { index, ad ->

                if (selectedTab == "all" || ad.category == selectedTab) {

                    ManageAdCard(
                        ad = ad,
                        onApprove = {
                            selectedIndex = index
                            actionType = "approve"
                            showDialog = true
                        },
                        onReject = {
                            selectedIndex = index
                            actionType = "reject"
                            showDialog = true
                        }
                    )
                }
            }
        }
        if (showDialog && selectedIndex != -1) {

            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        if (actionType == "approve") "Approve Ad"
                        else "Reject Ad"
                    )
                },
                text = {
                    Text(
                        if (actionType == "approve")
                            "Are you sure you want to approve this ad?"
                        else
                            "Are you sure you want to reject this ad?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (actionType == "approve") {
                                ads = ads.toMutableList().also {
                                    it[selectedIndex] =
                                        it[selectedIndex].copy(isApproved = true)
                                }
                            } else {
                                ads = ads.toMutableList().also {
                                    it.removeAt(selectedIndex)
                                }
                            }

                            showDialog = false
                        }
                    ) {
                        Text("Yes", color = Color(0xFF22C55E))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .background(
                if (selected) Color(0xFFB96B4C) else Color.Black,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

@Composable
fun ManageAdCard(
    ad: AdminAd,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row {

            // IMAGE PLACEHOLDER
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray)
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            ) {

                Text(ad.title)
                Text(ad.user, color = Color.Gray)

                Spacer(Modifier.height(6.dp))

                Text(ad.price, color = Color(0xFFB96B4C))

                Spacer(Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(Color(0xFF22C55E))
                    ) {
                        Text("Approve", color = Color.White)
                    }

                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(Color(0xFFEF4444))
                    ) {
                        Text("Reject", color = Color.White)
                    }
                }
            }
        }
    }
}