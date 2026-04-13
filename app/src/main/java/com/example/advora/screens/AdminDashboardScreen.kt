package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import com.example.advora.components.admin.AdminDrawer
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight

// ================= MAIN SCREEN =================
@Composable
fun AdminDashboardScreen(onNavigate: (String) -> Unit) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black
            ) {
                AdminDrawer(
                    current = "admin_dashboard",
                    onNavigate = {
                        scope.launch { drawerState.close() }
                        onNavigate(it)
                    }
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },

        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFF9CA3AF))
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Dashboard", style = MaterialTheme.typography.titleLarge)

                    Spacer(Modifier.height(20.dp))

                    // STATS
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            StatCard("1,234", "Total Ads", Icons.Default.Description, Color(0xFFB96B4C))
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            StatCard("856", "Total Users", Icons.Default.People, Color(0xFF10B981))
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            StatCard("23", "Reports", Icons.Default.Warning, Color(0xFFEF4444))
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            StatCard("892", "Active Ads", Icons.Default.TrendingUp, Color(0xFF3B82F6))
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // QUICK ACTIONS
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Quick Actions",
                                color = Color(0xFFB96B4C),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                                ActionCard(
                                    text = "Manage Ads",
                                    icon = Icons.Default.Description
                                ) { onNavigate("manage_ads") }

                                ActionCard(
                                    text = "Users",
                                    icon = Icons.Default.People
                                ) { onNavigate("user_management") }
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                                ActionCard(
                                    text = "Reports",
                                    icon = Icons.Default.Warning
                                ) { onNavigate("reports") }

                                ActionCard(
                                    text = "Map",
                                    icon = Icons.Default.Place
                                ) { onNavigate("admin_map") }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // ACTIVITIES
                    Card(shape = RoundedCornerShape(20.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text("Recent Activities", color = Color(0xFFB96B4C))

                            Spacer(Modifier.height(10.dp))

                            ActivityItem("New ad posted: iPhone 14 Pro Max", "5 min ago", Color.Gray)
                            ActivityItem("User reported ad: Honda City 2020", "15 min ago", Color.Gray)
                            ActivityItem("Ad approved: MacBook Pro M2", "1 hour ago", Color(0xFF22C55E))
                            ActivityItem("User blocked: John Doe", "2 hours ago", Color(0xFFEF4444))
                        }
                    }
                }
            }
        }
    }
}

// ================= TOP BAR =================
@Composable
fun TopBar(onMenuClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                Icons.Default.Menu,
                null,
                tint = Color(0xFFB96B4C),
                modifier = Modifier.clickable { onMenuClick() }
            )

            Spacer(Modifier.width(10.dp))

            Text("Advora", color = Color(0xFFB96B4C)) // ✅ FIXED TEXT
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                "EN",
                modifier = Modifier
                    .background(Color.DarkGray, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                color = Color.White
            )

            Spacer(Modifier.width(10.dp))

            Icon(Icons.Outlined.Notifications, null, tint = Color(0xFFB96B4C))
            Spacer(Modifier.width(10.dp))
            Icon(Icons.Default.AccountCircle, null, tint = Color(0xFFB96B4C))
        }
    }
}

// ================= BOTTOM BAR =================


// ================= COMMON COMPONENTS =================
@Composable
fun BottomItem(text: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFFB96B4C) else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(icon, null, tint = color)
        Text(text, color = color, fontSize = 12.sp)
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), // 🔥 INCREASE SIZE
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp) // 🔥 SHADOW
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // 🔥 MORE PADDING
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ICON
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )

            Column {
                Text(
                    value,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    label,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = Color(0xFFB96B4C))
            Spacer(Modifier.height(6.dp))
            Text(text)
        }
    }
}

@Composable
fun ActivityItem(title: String, time: String, color: Color) {
    Row(modifier = Modifier.padding(10.dp)) {
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title)
            Text(time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

// ================= DRAWER =================
