package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun MyAdsScreen(
    onBack: () -> Unit
) {

    // 🔥 TEMP STATIC DATA (NO VIEWMODEL)
    val ads = listOf(

        Ad(
            title = "Old Bike for Sale",
            price = "₹20,000",
            location = "Ujjain",
            imageUrl = "https://images.unsplash.com/photo-1558981806-ec527fa84c39",
            category = "Buy/Sell"
        ),

        Ad(
            title = "Room for Rent",
            price = "₹5,000/month",
            location = "Freeganj",
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
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color(0xFFB86B4B),
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                "My Ads",
                color = Color(0xFFB86B4B),
                fontSize = 18.sp
            )
        }

        // 🔹 LIST
        LazyColumn {

            items(ads) { ad ->

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
                            modifier = Modifier
                                .size(100.dp),
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