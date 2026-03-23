package com.example.advora.screens

// ✅ REQUIRED IMPORTS (MOST IMPORTANT)
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ✅ IMPORT YOUR VIEWMODEL + MODEL (VERY IMPORTANT)
import com.example.advora.viewmodel.DashboardViewModel
import com.example.advora.model.Ad

@Composable
fun SavedScreen(
    viewModel: DashboardViewModel,
    onBack: () -> Unit
) {

    val ads: List<Ad> = viewModel.savedAds

    Column {

        // 🔷 HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text("Saved Ads / सहेजे गए विज्ञापन")
                Text("${ads.size} saved items", fontSize = 12.sp)
            }
        }

        // 🔴 EMPTY STATE
        if (ads.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No saved ads 😢")
            }
        }

        // 🟢 LIST
        else {
            LazyColumn {

                items(ads) { ad ->

                    Card(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Column {

                            Box {

                                AsyncImage(
                                    model = ad.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )

                                // 🟢 ACTIVE BADGE
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .background(
                                            Color(0xFF16A34A),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Active", color = Color.White)
                                }

                                // ❌ DELETE BUTTON
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .clickable {
                                            viewModel.toggleSave(ad)
                                        }
                                )
                            }

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(ad.title)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                    Text(ad.location, color = Color.Gray)
                                }

                                Text(
                                    ad.price,
                                    color = Color(0xFF1F3C88)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}