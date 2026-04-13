package com.example.advora.components.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AdminDrawer(
    current: String, // 🔥 ACTIVE SCREEN
    onNavigate: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.Black)
            .padding(16.dp)
    ) {

        // 🔶 TITLE
        Text(
            text = "Advora",
            color = Color(0xFFB96B4C),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        DrawerItem("Dashboard", "admin_dashboard", current, Icons.Outlined.Home, onNavigate)
        DrawerItem("Manage Ads", "manage_ads", current, Icons.Outlined.List, onNavigate)
        DrawerItem("User Management", "user_management", current, Icons.Outlined.Person, onNavigate)
        DrawerItem("Reports", "reports", current, Icons.Outlined.Info, onNavigate)
        DrawerItem("Profile", "profile", current, Icons.Outlined.AccountCircle, onNavigate)

        // 🗺️ NEW MAP BUTTON
        DrawerItem("Map", "admin_map", current, Icons.Outlined.Place, onNavigate)

        Spacer(modifier = Modifier.weight(1f))

        // 🔻 LOGOUT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0x33FF0000),
                    RoundedCornerShape(12.dp)
                )
                .clickable { onNavigate("login") }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.ExitToApp, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Logout", color = Color.Red)
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    route: String,
    current: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onNavigate: (String) -> Unit
) {

    val isSelected = current == route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) Color(0x33B96B4C) else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .clickable {
                if (!isSelected) onNavigate(route)
            }
            .padding(vertical = 14.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color(0xFFB96B4C)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            title,
            color = if (isSelected) Color.White else Color(0xFFB96B4C)
        )
    }
}