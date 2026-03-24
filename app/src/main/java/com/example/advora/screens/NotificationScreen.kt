package com.example.advora.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import com.example.advora.viewmodel.LanguageViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.advora.R
import com.example.advora.model.NotificationModel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.content.res.Configuration
import java.util.Locale
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun NotificationScreen(viewModel: LanguageViewModel) {
    val lang by viewModel.language.collectAsState(initial = "en")

    ApplyLanguage(lang)
    val notifications = listOf(
        NotificationModel(
            title = stringResource(R.string.ad_still_active),
            description = stringResource(R.string.flat_location),
            time = "4d",
            buttonText = stringResource(R.string.update_now),
            icon = Icons.Filled.Notifications
        ),
        NotificationModel(
            title = stringResource(R.string.ad_expiring),
            description = stringResource(R.string.bike_name),
            time = "8d",
            buttonText = stringResource(R.string.renew),
            icon = Icons.Filled.AccessTime
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        // 🔝 Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.notifications),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // 🌐 Language Toggle
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text("हिं")

                Switch(
                    checked = lang == "en",
                    onCheckedChange = {
                        if (it) viewModel.changeLanguage("en")
                        else viewModel.changeLanguage("hi")
                    }
                )

                Text("EN")
            }
        }
        // Notification List
        LazyColumn {
            items(notifications) { item ->
                NotificationItem(item)
            }
        }
    }
}
@Composable
fun NotificationItem(item: NotificationModel) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color(0xFFFF6D00)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(item.title, fontWeight = FontWeight.Bold)

                Text(
                    item.description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                if (item.buttonText != null) {
                    Button(onClick = {}) {
                        Text(item.buttonText)
                    }
                }
            }

            Text(item.time, fontSize = 10.sp)
        }
    }
}
@Composable
fun ApplyLanguage(lang: String) {
    val context = LocalContext.current

    LaunchedEffect(lang) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
