package com.example.advora.components.admin

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background

@Composable
fun AdminTopBar(onMenuClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text("Reports", color = Color(0xFFB96B4C))
        }

        Row {
            Text("EN", color = Color.White)

            Spacer(modifier = Modifier.width(10.dp))

            Icon(Icons.Outlined.Notifications, null, tint = Color(0xFFB96B4C))

            Spacer(modifier = Modifier.width(10.dp))

            Icon(Icons.Default.AccountCircle, null, tint = Color(0xFFB96B4C))
        }
    }
}