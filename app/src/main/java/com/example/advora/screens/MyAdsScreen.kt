package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage

import com.example.advora.viewmodel.DashboardViewModel
import com.example.advora.model.Ad

@Composable
fun MyAdsScreen(
    viewModel: DashboardViewModel,
    onBack: () -> Unit
) {

    val ads = viewModel.myAds

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {

        // 🔷 HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable { onBack() }
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    "My Ads / मेरे विज्ञापन",
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                "${ads.size} ads posted",
                color = Color.Gray,
                fontSize = 12.sp
            )

            // 🔍 DEBUG (REMOVE AFTER TEST)
            Text("Ads count: ${ads.size}")
        }

        // 🔴 EMPTY STATE (IMPORTANT)
        if (ads.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No ads posted yet 😢")
            }
        }

        // 🔽 LIST
        else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp)
            ) {

                items(ads) { ad ->

                    var isActive by remember { mutableStateOf(true) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(modifier = Modifier.padding(14.dp)) {

                            // 🔥 TOP SECTION
                            Row {

                                AsyncImage(
                                    model = ad.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Text(
                                            ad.title,
                                            fontSize = 14.sp,
                                            maxLines = 1
                                        )

                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color(0xFF16A34A),
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "Active",
                                                color = Color.White,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(4.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            ad.location,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        ad.price,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1F3C88)
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Divider(color = Color(0xFFE5E5E5))

                            Spacer(Modifier.height(10.dp))

                            // 🔹 STATUS ROW
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text("Status", color = Color.Gray)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Active", fontSize = 12.sp)
                                    Spacer(Modifier.width(6.dp))

                                    Switch(
                                        checked = isActive,
                                        onCheckedChange = { isActive = it }
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))

                            // 🔘 BUTTONS
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                OutlinedButton(
                                    onClick = { },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Edit, null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Edit")
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.deleteAd(ad) // ✅ FIXED
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.Red
                                    )
                                ) {
                                    Icon(Icons.Default.Delete, null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}