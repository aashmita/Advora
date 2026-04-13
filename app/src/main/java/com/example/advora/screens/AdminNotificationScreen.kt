package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.LanguageViewModel

// ✅ SHARED DATA MODEL (Keep this in a top-level file or shared package)
data class NotifyItem(
    val titleEn: String, val titleHi: String,
    val descEn: String, val descHi: String,
    val timeEn: String, val timeHi: String,
    val isUrgent: Boolean = false
)

// 🛡️ ADMIN ONLY LIST - Strictly separate from userNotificationsList
val adminNotificationsList = mutableStateListOf<NotifyItem>(
    NotifyItem(
        "System Maintenance", "सिस्टम रखरखाव",
        "Scheduled server update at 12:00 AM.", "रात 12:00 बजे निर्धारित सर्वर अपडेट।",
        "1h ago", "1 घंटे पहले"
    ),
    NotifyItem(
        "Urgent Reports", "तत्काल रिपोर्ट",
        "5 ads in 'Real Estate' reported for fraud.", "धोखाधड़ी के लिए 'रियल एस्टेट' में 5 विज्ञापनों की रिपोर्ट की गई।",
        "3h ago", "3 घंटे पहले", true
    ),
    NotifyItem(
        "New KYC Request", "नया केवाईसी अनुरोध",
        "Verify 10 new business account applications.", "10 नए बिजनेस अकाउंट आवेदनों को सत्यापित करें।",
        "Yesterday", "कल"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNotificationScreen(
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = if (isHindi) "एडमिन सूचनाएं" else "Admin Notifications",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = Color(0xFFF5F5F7) // Light grey background for contrast
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(Modifier.height(16.dp)) }

            // 🔥 Displays ONLY Admin items, newest first
            items(adminNotificationsList.reversed()) { item ->
                NotificationCard(item, isHindi, accentColor)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun NotificationCard(item: NotifyItem, isHindi: Boolean, accentColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Priority/Status Dot
            Box(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .size(8.dp)
                    .background(if (item.isUrgent) Color.Red else accentColor, CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isHindi) item.titleHi else item.titleEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (isHindi) item.timeHi else item.timeEn,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (isHindi) item.descHi else item.descEn,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}