package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState // Added for scrolling
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // Added for scrolling
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Added for image shaping
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdViewModel

// ✅ Data Model
data class NotificationData(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    var isRead: Boolean,
    val dotColor: Color,
    val detailTitle: String,
    val detailPrice: String,
    val detailLocation: String,
    val detailDesc: String,
    val status: String,
    val imageUrl: Any? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    adViewModel: AdViewModel,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    var selectedNotification by remember { mutableStateOf<NotificationData?>(null) }
    val latestAd = adViewModel.ads.lastOrNull()

    // ✅ Clear red dot badge on Home screen
    LaunchedEffect(Unit) {
        adViewModel.setNewNotification(false)
    }

    // ✅ Notification Data with Full Details and Images
    val notificationList = remember {
        mutableStateListOf(
            NotificationData(
                1, if(isHindi) "आपका विज्ञापन लाइव है" else "Your ad is now live",
                "${latestAd?.title ?: "Item"} is now visible", "Just now", false, Color.Red,
                latestAd?.title ?: "Ad Posted", latestAd?.price ?: "---",
                latestAd?.location ?: "Ujjain, MP", latestAd?.description ?: "Your ad is successfully posted.", "ACTIVE",
                latestAd?.imageUri
            ),
            NotificationData(
                2, if(isHindi) "पास में नया विज्ञापन" else "New ad in your area",
                "iPhone 15 Pro posted in Freeganj", "15 min ago", false, Color.Green,
                "iPhone 15 Pro", "₹95,000", "Freeganj, Ujjain",
                "128GB variant. Excellent condition.", "NEW",
                "https://images.unsplash.com/photo-1695048133142-1a20484d2569"
            ),
            NotificationData(
                3, if(isHindi) "विज्ञापन समाप्त होने वाला है" else "Ad expiring soon",
                "Honda City ad expires in 2 days", "1 hour ago", false, Color.Red,
                "Honda City 2020", "₹8,50,000", "Nanakheda, Ujjain",
                "Your ad is about to expire. Renew now!", "EXPIRING",
                "https://images.unsplash.com/photo-1590362891991-f776e747a588"
            ),
            NotificationData(
                4, if(isHindi) "कीमत में गिरावट" else "Price drop alert",
                "MacBook Pro price reduced", "1 day ago", true, Color.Gray,
                "MacBook Pro M2", "₹1,10,000", "Indore, MP",
                "Price dropped by ₹5,000. Don't miss out!", "OFFER",
                "https://images.unsplash.com/photo-1517336714460-457926510180"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedNotification == null) (if (isHindi) "सूचनाएं" else "Notifications")
                        else (if (isHindi) "विवरण" else "Details"),
                        color = Color(0xFFD08C60), fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedNotification != null) selectedNotification = null else onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFD08C60))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (selectedNotification == null) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notificationList) { notification ->
                        NotificationItem(
                            notification = notification,
                            onDismiss = { notificationList.remove(notification) },
                            onClick = {
                                notification.isRead = true
                                selectedNotification = notification
                            }
                        )
                    }
                }
            } else {
                NotificationDetailView(selectedNotification!!, isHindi)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationData, onDismiss: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(if (notification.isRead) Color.LightGray else notification.dotColor, CircleShape)
                    .align(Alignment.Top)
            )
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(notification.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(notification.message, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
                Text(notification.time, fontSize = 11.sp, color = Color(0xFFD08C60), modifier = Modifier.padding(top = 4.dp))
            }

            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.LightGray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun NotificationDetailView(data: NotificationData, isHindi: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Fixed Unresolved reference
    ) {
        AsyncImage(
            model = data.imageUrl ?: "https://via.placeholder.com/400x250?text=No+Image",
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp)), // Fixed Unresolved reference
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        Surface(
            color = when(data.status) {
                "ACTIVE", "NEW" -> Color(0xFF4CAF50)
                "EXPIRING" -> Color(0xFFFF9800)
                else -> Color(0xFFD08C60)
            },
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(data.status, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Text(data.detailTitle, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Text(data.detailLocation, fontSize = 14.sp, color = Color.Gray)
        Text(data.detailPrice, fontSize = 20.sp, color = Color(0xFFD08C60), fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 8.dp))

        HorizontalDivider(Modifier.padding(vertical = 12.dp))

        Text(if(isHindi) "विवरण" else "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))
        Text(data.detailDesc, color = Color.DarkGray, fontSize = 14.sp, lineHeight = 20.sp)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* Action logic */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(if (isHindi) "अभी देखें" else "View Now", color = Color.White)
        }
    }
}