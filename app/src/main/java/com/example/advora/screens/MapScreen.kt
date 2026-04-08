package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun MapScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Map Background (Replace URL with your API/Provider if needed)
        AsyncImage(
            model = "https://maps.googleapis.com/maps/api/staticmap?center=23.1765,75.7885&zoom=15&size=800x1600&sensor=false",
            contentDescription = "Map View",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Floating Back Button (Top Left)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(44.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // 3. Center Location Marker
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Current Location",
            tint = Color(0xFFD08C60), // Your App Accent Color
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .offset(y = (-24).dp) // Adjust to point exactly at center
        )

        // 4. Bottom Location Details Card
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Ujjain, Madhya Pradesh, India",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = { /* Add Logic to save location */ onBack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08C60)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirm Location", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}