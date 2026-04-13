package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {

    var isDark by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("EN") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9CA3AF))
    ) {

        // 🔝 TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Menu, null, tint = Color(0xFFB96B4C))

            Spacer(Modifier.weight(1f))

            Text("Profile", color = Color(0xFFB96B4C))

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .background(Color.DarkGray, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(language, color = Color.White)
            }

            Spacer(Modifier.width(10.dp))
            Icon(Icons.Outlined.Notifications, null, tint = Color(0xFFB96B4C))

            Spacer(Modifier.width(10.dp))

            AsyncImage(
                model = "https://i.pravatar.cc/150?u=admin",
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // 👤 PROFILE CARD
            Card(shape = RoundedCornerShape(20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = "https://i.pravatar.cc/150?u=admin",
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text("Admin User", fontSize = 16.sp)
                        Text("admin@advora.com", color = Color.Gray)

                        Spacer(Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFB96B4C),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Administrator", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ⚙️ SETTINGS CARD
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Settings", fontSize = 16.sp)

                    Spacer(Modifier.height(12.dp))

                    // THEME
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3A3A3A), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DarkMode, null, tint = Color.White)

                        Spacer(Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Theme", color = Color.White)
                            Text(
                                if (isDark) "Dark" else "Light",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }

                        Switch(
                            checked = isDark,
                            onCheckedChange = { isDark = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFFB96B4C)
                            )
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // LANGUAGE
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3A3A3A), RoundedCornerShape(12.dp))
                            .clickable {
                                language = if (language == "EN") "HI" else "EN"
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Language, null, tint = Color.White)

                        Spacer(Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Language", color = Color.White)
                            Text(
                                if (language == "EN") "English" else "Hindi",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFB96B4C),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(language, color = Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 📊 STATS
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Admin Stats")

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                        StatBox("156", "Actions Today")
                        StatBox("23", "Pending Tasks")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🔻 LOGOUT BUTTON
            Button(
                onClick = { onNavigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFEF4444)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.ExitToApp, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Logout", color = Color.White)
            }
        }
    }
}

@Composable
fun StatBox(value: String, label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3A3A3A), RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Color(0xFFB96B4C))
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}