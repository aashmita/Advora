package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdItem
import com.example.advora.viewmodel.AdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(
    adViewModel: AdViewModel,
    onBack: () -> Unit,
    onEditAd: (AdItem) -> Unit,
    onViewDetails: (AdItem) -> Unit
) {
    // Strictly three tabs for My Ads
    var selectedTab by remember { mutableStateOf("Active") }
    var adToDelete by remember { mutableStateOf<AdItem?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val filteredAds = adViewModel.ads.filter { it.status == selectedTab }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Ads", color = Color(0xFFD08C60), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFFD08C60))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFFF1F1F1)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Strictly Active, Sold, Inactive
                listOf("Active", "Sold", "Inactive").forEach { tab ->
                    val isSelected = selectedTab == tab

                    // Specific Color Logic
                    val tabColor = when (tab) {
                        "Active" -> Color(0xFF4CAF50)   // Requested Green
                        "Sold" -> Color(0xFFFFA500)     // Orange
                        "Inactive" -> Color(0xFFE53935) // Red
                        else -> Color(0xFFD08C60)
                    }

                    Button(
                        onClick = { selectedTab = tab },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) tabColor else Color.White,
                            contentColor = if (isSelected) Color.White else Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(if (isSelected) 4.dp else 2.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(tab, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (filteredAds.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No $selectedTab Ads Found", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredAds, key = { it.id }) { ad ->
                        EnhancedAdCard(
                            ad = ad,
                            onDelete = { adToDelete = ad; showDeleteDialog = true },
                            onEdit = { onEditAd(ad) },
                            onStatusChange = { newStatus -> adViewModel.updateAdStatus(ad.id, newStatus) },
                            onClick = { onViewDetails(ad) }
                        )
                    }
                }
            }
        }
    }

    // --- Delete Confirmation Popup ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Ad", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this ad? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        adToDelete?.let { adViewModel.deleteAd(it.id) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Delete", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun EnhancedAdCard(
    ad: AdItem,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onStatusChange: (String) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ad.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(95.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(ad.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, color = Color.Black)
                Text(ad.location, color = Color.Gray, fontSize = 12.sp)
                Text(ad.price, color = Color(0xFFD08C60), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)

                // Contextual Status Toggle
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val label = if (ad.status == "Active" || ad.status == "Expiring") "Mark Sold" else "Re-activate"
                    val nextStatus = if (ad.status == "Active" || ad.status == "Expiring") "Sold" else "Active"

                    Surface(
                        onClick = { onStatusChange(nextStatus) },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFD08C60).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFFD08C60))
                    ) {
                        Text(
                            label,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            color = Color(0xFFD08C60),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Action Buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit", tint = Color.DarkGray, modifier = Modifier.size(22.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFE53935), modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}