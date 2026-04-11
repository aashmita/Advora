package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.AdItem

import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.CustomZoomButtonsController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    adViewModel: AdViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = Color(0xFFD08C60)

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Cars", "Mobiles", "Property", "Bikes")

    // Mapping city names to fixed coordinates
    val cityCoords = remember {
        mapOf(
            "Ujjain" to GeoPoint(23.1765, 75.7885),
            "Indore" to GeoPoint(22.7196, 75.8577),
            "Dewas" to GeoPoint(22.9676, 76.0534),
            "Bhopal" to GeoPoint(23.2599, 77.4126)
        )
    }

    val mapView = remember {
        MapView(context).apply {
            // FIX LAG: Set User-Agent and tile cache settings
            Configuration.getInstance().userAgentValue = context.packageName
            setTileSource(TileSourceFactory.MAPNIK)

            setMultiTouchControls(true)
            isVerticalMapRepetitionEnabled = false
            isHorizontalMapRepetitionEnabled = true
            setScrollableAreaLimitLatitude(MapView.getTileSystem().maxLatitude, MapView.getTileSystem().minLatitude, 0)

            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(14.5)
            controller.setCenter(cityCoords["Ujjain"])
        }
    }

    // ✅ LOGIC: Group multiple ads into ONE pointer per city
    LaunchedEffect(selectedCategory, adViewModel.ads) {
        mapView.overlays.clear()

        val adsList: List<AdItem> = adViewModel.ads

        val filteredAds = adsList.filter { ad ->
            selectedCategory == "All" || ad.category.toString() == selectedCategory
        }

        // Grouping ads by city so we only show 1 pointer
        val groupedAds = filteredAds.groupBy { it.location ?: "Ujjain" }

        groupedAds.forEach { (location, ads) ->
            val point = cityCoords[location] ?: cityCoords["Ujjain"]
            val marker = Marker(mapView)
            marker.position = point

            // Show city name and total count in the pointer
            marker.title = "$location: ${ads.size} Ads Found"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        // 1. TOP OVERLAY (Search & Categories)
        Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
                    }
                }

                Spacer(Modifier.width(12.dp))

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search city...") },
                    modifier = Modifier.weight(1f).height(52.dp).shadow(10.dp, RoundedCornerShape(26.dp)),
                    shape = RoundedCornerShape(26.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            val target = cityCoords.keys.find { it.equals(searchQuery, true) }
                            target?.let {
                                mapView.controller.animateTo(cityCoords[it])
                                mapView.controller.setZoom(15.0)
                            }
                        }) {
                            Icon(Icons.Default.Search, null, tint = accentColor)
                        }
                    }
                )
            }

            Spacer(Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) accentColor else Color.White,
                        shadowElevation = 6.dp,
                        modifier = Modifier.height(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(category, color = if (isSelected) Color.White else Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. FLOATING ZOOM BUTTONS (RIGHT SIDE)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { mapView.controller.zoomIn() },
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.size(45.dp),
                shape = RoundedCornerShape(8.dp)
            ) { Icon(Icons.Default.Add, "Zoom In") }

            FloatingActionButton(
                onClick = { mapView.controller.zoomOut() },
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.size(45.dp),
                shape = RoundedCornerShape(8.dp)
            ) { Icon(Icons.Default.Remove, "Zoom Out") }
        }
    }
}