package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage

import com.example.advora.model.Ad

@Composable
fun SavedScreen(
    onBack: () -> Unit
) {

    // 🔥 STATIC SAVED ADS (NO VIEWMODEL)
    val savedAds = listOf(

        Ad(
            title = "Laptop for Sale",
            price = "₹30,000",
            location = "Ujjain",
            imageUrl = "https://images.unsplash.com/photo-1517336714731-489689fd1ca8",
            category = "Buy/Sell"
        ),

        Ad(
            title = "1BHK Flat",
            price = "₹8,000/month",
            location = "Indore",
            imageUrl = "https://images.unsplash.com/photo-1507089947368-19c1da9775ae",
            category = "Rentals"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
    ) {

        // 🔹 HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = Color(0xFFB86B4B),
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                "Saved Ads",
                color = Color(0xFFB86B4B),
                fontSize = 18.sp
            )
        }

        // 🔹 LIST
        LazyColumn {

            items(savedAds) { ad ->

                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Row {

                        AsyncImage(
                            model = ad.imageUrl,
                            contentDescription = "",
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .weight(1f)
                        ) {

                            Text(ad.title, fontSize = 14.sp)

                            Spacer(Modifier.height(4.dp))

                            Text(ad.location, color = Color.Gray, fontSize = 12.sp)

                            Spacer(Modifier.height(6.dp))

                            Text(
                                ad.price,
                                color = Color(0xFFB86B4B),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}