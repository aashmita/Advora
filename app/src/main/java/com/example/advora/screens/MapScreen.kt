package com.example.advora.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen(
    onNavigate: (String) -> Unit,
    isAdmin: Boolean = false
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔶 TOP BAR
        TopBar()

        // 🗺️ MAP + ADMIN UI
        Box(modifier = Modifier.weight(1f)) {

            AndroidView(
                modifier = Modifier.fillMaxSize(), // ✅ FIXED
                factory = { context: Context ->

                    Configuration.getInstance().load(
                        context,
                        context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
                    )

                    val map = MapView(context)

                    map.setTileSource(TileSourceFactory.MAPNIK)
                    map.setMultiTouchControls(true)

                    val controller = map.controller
                    controller.setZoom(14.0)

                    val startPoint = GeoPoint(23.1765, 75.7885)
                    controller.setCenter(startPoint)

                    val marker = Marker(map)
                    marker.position = startPoint
                    marker.title = "Sample Ad"

                    map.overlays.add(marker)

                    map
                }
            )

            // 🔐 ADMIN LABEL
            if (isAdmin) {
                Text(
                    "Admin Mode",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
            }
        }

        // 🔻 BOTTOM BAR

    }
}
@Composable
fun TopBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("Advora", color = Color(0xFFB96B4C))

        Row {
            Text("EN", color = Color.White)
        }
    }
}

