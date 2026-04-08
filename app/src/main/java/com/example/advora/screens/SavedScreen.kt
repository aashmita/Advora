package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.AdItem

@Composable
fun SavedScreen(
    onBack: () -> Unit,
    adViewModel: AdViewModel,
    onNavigate: (String) -> Unit,
    isHindi: Boolean,
    onAdClick: (AdItem) -> Unit
) {

    val savedAds = adViewModel.savedAds

    Scaffold(
        bottomBar = {
            BottomNavBar(onNavigate, isHindi)
        }
    ) { paddingValues ->   // ✅ FIXED HERE

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F3F3))
                .padding(paddingValues) // ✅ FIXED HERE (REMOVES GAP + ERROR)
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
                    tint = Color(0xFFD08C60),
                    modifier = Modifier.clickable { onBack() }
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    "Saved Ads",
                    color = Color(0xFFD08C60),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // 🔹 LIST
            LazyColumn {

                items(savedAds) { ad ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .clickable { onAdClick(ad) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {

                        Column {

                            AsyncImage(
                                model = ad.imageUri,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.padding(12.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        ad.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black // ✅ FIXED (WAS GREY ISSUE)
                                    )

                                    Icon(
                                        Icons.Default.Favorite,
                                        contentDescription = "",
                                        tint = Color.Red,
                                        modifier = Modifier.clickable {
                                            adViewModel.toggleSave(ad)
                                        }
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(ad.location, color = Color.Gray)

                                Spacer(Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        ad.price,
                                        color = Color(0xFFD08C60)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .border(
                                                1.dp,
                                                Color.LightGray,
                                                RoundedCornerShape(20.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            ad.category,
                                            color = Color.Black // ✅ FIXED JOBS GREY ISSUE
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}