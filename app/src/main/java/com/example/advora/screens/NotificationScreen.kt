package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.advora.viewmodel.AdViewModel

// 👤 USER ONLY LIST - Updated with Ad-related prelisted items
val userNotificationsList = mutableStateListOf<NotifyItem>(
    NotifyItem(
        "Ad Posted Successfully!", "विज्ञापन सफलतापूर्वक पोस्ट किया गया!",
        "Your ad for 'iPhone 13' is now live for everyone to see.",
        "आपका 'iPhone 13' का विज्ञापन अब सभी के देखने के लिए लाइव है।",
        "5m ago", "5 मिनट पहले", false
    ),
    NotifyItem(
        "Ad Under Review", "विज्ञापन समीक्षा के अधीन है",
        "Your new ad for '2BHK Apartment' is being verified by our team.",
        "आपके '2BHK अपार्टमेंट' के नए विज्ञापन की हमारी टीम द्वारा पुष्टि की जा रही है।",
        "1h ago", "1 घंटे पहले", false
    ),
    NotifyItem(
        "New Message Received", "नया संदेश प्राप्त हुआ",
        "A buyer is interested in your 'Royal Enfield' listing.",
        "एक खरीदार आपकी 'Royal Enfield' लिस्टिंग में रुचि रखता है।",
        "3h ago", "3 घंटे पहले", true
    ),
    NotifyItem(
        "Trending Near You", "आपके पास ट्रेंडिंग",
        "Check out the latest electronics ads posted in your area.",
        "अपने क्षेत्र में पोस्ट किए गए नवीनतम इलेक्ट्रॉनिक्स विज्ञापन देखें।",
        "Yesterday", "कल", false
    ),
    NotifyItem(
        "Price Drop Alert!", "कीमत में गिरावट की चेतावनी!",
        "An item in your wishlist has a new lower price.",
        "आपकी विशलिस्ट की एक वस्तु की नई कम कीमत है।",
        "2 days ago", "2 दिन पहले", false
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    adViewModel: AdViewModel,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    var selectedNotification by remember { mutableStateOf<NotifyItem?>(null) }
    val notificationList = userNotificationsList

    LaunchedEffect(Unit) {
        adViewModel.setNewNotification(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedNotification == null) {
                            if (isHindi) "सूचनाएं" else "Notifications"
                        } else {
                            if (isHindi) "विवरण" else "Details"
                        },
                        color = Color(0xFFD08C60),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedNotification != null) selectedNotification = null else onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFFD08C60))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (selectedNotification == null) {
                if (notificationList.isEmpty()) {
                    EmptyNotificationsView(isHindi)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(notificationList.reversed()) { notification ->
                            NotificationItem(
                                notification = notification,
                                isHindi = isHindi,
                                onDismiss = { userNotificationsList.remove(notification) },
                                onClick = { selectedNotification = notification }
                            )
                        }
                    }
                }
            } else {
                NotificationDetailView(selectedNotification!!, isHindi)
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotifyItem,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(10.dp).background(
                    if (notification.isUrgent) Color.Red else Color(0xFFD08C60), CircleShape
                ).align(Alignment.Top)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isHindi) notification.titleHi else notification.titleEn,
                    fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black
                )
                Text(
                    text = if (isHindi) notification.descHi else notification.descEn,
                    fontSize = 13.sp, color = Color.Gray, maxLines = 1
                )
                Text(
                    text = if (isHindi) notification.timeHi else notification.timeEn,
                    fontSize = 11.sp, color = Color(0xFFD08C60), modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun NotificationDetailView(data: NotifyItem, isHindi: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = "https://via.placeholder.com/400x250?text=Advora+Ad+Update",
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
        Surface(
            color = if (data.isUrgent) Color.Red else Color(0xFFD08C60),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = if (isHindi) "अपडेट" else "Update",
                color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                fontSize = 12.sp, fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = if (isHindi) data.titleHi else data.titleEn,
            fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)
        )
        Text(text = if (isHindi) data.timeHi else data.timeEn, fontSize = 14.sp, color = Color.Gray)
        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
        Text(if(isHindi) "विवरण" else "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (isHindi) data.descHi else data.descEn,
            color = Color.DarkGray, fontSize = 14.sp, lineHeight = 20.sp
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { /* Detail Action */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(if (isHindi) "अभी देखें" else "View Now", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmptyNotificationsView(isHindi: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.NotificationsNone, null, tint = Color.LightGray, modifier = Modifier.size(80.dp))
        Spacer(Modifier.height(12.dp))
        Text(
            text = if(isHindi) "कोई नई सूचना नहीं" else "No new notifications",
            color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium
        )
    }
}