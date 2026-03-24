package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.advora.viewmodel.DashboardViewModel
import com.example.advora.model.Ad

// 🎨 COLORS
val PrimaryBlue = Color(0xFF1F3C88)
val LightBlue = Color(0xFF3A5BA0)
val AccentGreen = Color(0xFF16A34A)
val Background = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    current: String,
    onNavigate: (String) -> Unit,
    onItemClick: (Ad) -> Unit
) {



    val ads = viewModel.filteredAds

    Scaffold(
        containerColor = Background,

        // 🔥 BOTTOM BAR WITH CENTER FLOATING BUTTON (LIKE YOUR IMAGE)
        bottomBar = {
            Box {

                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {

                    NavigationBarItem(
                        selected = current == "home",
                        onClick = { onNavigate("home") },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Home") }
                    )

                    NavigationBarItem(
                        selected = current == "ads",
                        onClick = { onNavigate("ads") },
                        icon = { Icon(Icons.Default.List, null) },
                        label = { Text("My Ads") }
                    )

                    // 👇 SPACE FOR CENTER BUTTON
                    Spacer(modifier = Modifier.width(60.dp))

                    NavigationBarItem(
                        selected = current == "map",
                        onClick = { onNavigate("map") },
                        icon = { Icon(Icons.Default.LocationOn, null) },
                        label = { Text("Map") }
                    )

                    NavigationBarItem(
                        selected = current == "saved",
                        onClick = { onNavigate("saved") },
                        icon = { Icon(Icons.Default.FavoriteBorder, null) },
                        label = { Text("Saved") }
                    )
                }

                // 🔥 CENTER ORANGE BUTTON (PIXEL PERFECT)
                FloatingActionButton(
                    onClick = { onNavigate("post") },
                    containerColor = Color(0xFFFF6A00),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-6).dp), // 👈 perfect position
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // 🔷 HEADER
            Column(
                modifier = Modifier
                    .background(PrimaryBlue)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Menu, "", tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Advora", color = Color.White, fontSize = 20.sp)
                    }

                    Row {
                        Icon(Icons.Default.Language, "", tint = Color.White)
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.clickable { onNavigate("notification") }
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(Icons.Default.Person, "", tint = Color.White)
                    }
                }

                Spacer(Modifier.height(12.dp))

                // 🔍 SEARCH
                Row {
                    TextField(
                        value = viewModel.searchQuery,
                        onValueChange = { viewModel.updateSearch(it) },
                        placeholder = { Text("Search ads...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFF22C55E), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Tune, "", tint = Color.White)
                    }
                }

                Spacer(Modifier.height(10.dp))

                // 🔘 CATEGORY
                Row {
                    listOf("All", "Jobs", "Rentals", "Buy/Sell").forEach { cat ->
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { viewModel.updateCategory(cat) }
                                .background(
                                    if (viewModel.selectedCategory == cat)
                                        Color.White else LightBlue,
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                cat,
                                color = if (viewModel.selectedCategory == cat)
                                    Color.Black else Color.White
                            )
                        }
                    }
                }
            }

            // 🔥 ADS LIST
            LazyColumn {

                items(ads) { ad ->

                    val isSaved = viewModel.isSaved(ad)

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable { onItemClick(ad) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // ✅ FIXED
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {

                        Column {

                            Box {

                                AsyncImage(
                                    model = ad.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )

                                // ✅ ACTIVE BADGE (FIXED LIKE YOUR IMAGE)
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .background(
                                            AccentGreen,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Active", color = Color.White)
                                }
                            }

                            Column(Modifier.padding(12.dp)) {

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Text(ad.title, fontSize = 16.sp)

                                    Icon(
                                        imageVector = if (isSaved)
                                            Icons.Default.Favorite
                                        else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (isSaved) Color.Red else Color.Gray,
                                        modifier = Modifier.clickable {
                                            viewModel.toggleSave(ad)
                                        }
                                    )
                                }

                                Spacer(Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, "", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text(ad.location, color = Color.Gray, fontSize = 12.sp)
                                }

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    ad.price,
                                    color = PrimaryBlue,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}