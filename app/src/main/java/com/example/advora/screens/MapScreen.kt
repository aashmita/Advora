package com.example.advora.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen() {

    AndroidView(factory = { context: Context ->

        // ✅ Important (OSM setup)
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
        )

        val map = MapView(context)

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val controller = map.controller
        controller.setZoom(14.0)

        // 📍 Ujjain location
        val startPoint = GeoPoint(23.1765, 75.7885)
        controller.setCenter(startPoint)


        val marker = Marker(map)
        marker.position = GeoPoint(23.1765, 75.7885)
        marker.title = "Sample Ad"
        map.overlays.add(marker)

        map
    })
}