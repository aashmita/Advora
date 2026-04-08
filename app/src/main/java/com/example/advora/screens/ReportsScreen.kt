package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // FIXED: Resolved 'clip' error
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.LanguageViewModel

// Data model for reports
data class ReportItem(
    val id: String,
    val adTitle: String,
    val reportedBy: String,
    val reason: String,
    val date: String,
    val price: String,
    val location: String,
    val description: String,
    var isResolved: Boolean = false
)

@Composable
fun ReportsScreen(
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit
) {
    val accentColor = Color(0xFFD08C60) // App Accent Color
    val isHindi = languageViewModel.isHindi

    var selectedTab by remember { mutableStateOf("Pending") }
    var selectedReport by remember { mutableStateOf<ReportItem?>(null) }

    // Mock Data List based on your screenshot
    val reportsList = remember { mutableStateListOf(
        ReportItem("1", "iPhone 14 Pro Max 256GB", "John Doe", "Suspicious pricing", "2026-03-22", "₹85,000", "Indore, MP", "The price is too low for a 14 Pro Max, likely a scam."),
        ReportItem("2", "Honda City 2020 VX Model", "Jane Smith", "Fake images", "2026-03-21", "₹7,20,000", "Ujjain, MP", "Images are taken from the internet, not the actual car."),
        ReportItem("3", "Samsung S23 Ultra", "Rahul Sharma", "Duplicate listing", "2026-03-20", "₹65,000", "Dewas, MP", "This user has posted the same ad three times already.")
    )}

    val filteredReports = reportsList.filter {
        if (selectedTab == "Pending") !it.isResolved else it.isResolved
    }

    if (selectedReport == null) {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(Color.Black).statusBarsPadding()) { // Dark TopBar
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor)
                        }
                        Text(
                            text = if (isHindi) "रिपोर्ट्स" else "Reports",
                            color = accentColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Tab Switcher matching your Reports screenshot
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF333333))
                            .padding(4.dp)
                    ) {
                        TabButton(
                            title = if (isHindi) "लंबित" else "Pending",
                            isSelected = selectedTab == "Pending",
                            accentColor = accentColor,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = "Pending" }
                        )
                        TabButton(
                            title = if (isHindi) "हल किया गया" else "Resolved",
                            isSelected = selectedTab == "Resolved",
                            accentColor = accentColor,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedTab = "Resolved" }
                        )
                    }
                }
            },
            containerColor = Color(0xFFF8F9FA) // App Background
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredReports) { report ->
                    ReportCard(
                        report = report,
                        isHindi = isHindi,
                        onAction = {
                            val index = reportsList.indexOf(report)
                            if (index != -1) reportsList[index] = report.copy(isResolved = true)
                        },
                        onDismiss = { reportsList.remove(report) },
                        onClick = { selectedReport = report }
                    )
                }
            }
        }
    } else {
        // Detailed Report View (Matching Image 11 & 5 theme)
        ReportDetailView(
            report = selectedReport!!,
            isHindi = isHindi,
            accentColor = accentColor,
            onBack = { selectedReport = null }
        )
    }
}

@Composable
fun TabButton(title: String, isSelected: Boolean, accentColor: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) accentColor else Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                color = if (isSelected) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ReportCard(report: ReportItem, isHindi: Boolean, onAction: () -> Unit, onDismiss: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFFFFEBEE), shape = CircleShape, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.padding(6.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(report.adTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))

                // Small Cross for Dismiss
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, tint = Color.Gray)
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("${if(isHindi) "द्वारा रिपोर्ट किया गया" else "Reported by"}: ${report.reportedBy}", color = Color.Gray, fontSize = 14.sp)
            Text("${if(isHindi) "कारण" else "Reason"}: ${report.reason}", color = Color.Gray, fontSize = 14.sp)
            Text(report.date, color = Color.LightGray, fontSize = 12.sp)

            if (!report.isResolved) {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)) // Action Green
                ) {
                    Text(if(isHindi) "कार्रवाई करें" else "Take Action", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailView(report: ReportItem, isHindi: Boolean, accentColor: Color, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(isHindi) "रिपोर्ट विवरण" else "Report Details", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) {
            // Ad Image Area matching your app's detail page
            Box(Modifier.fillMaxWidth().height(250.dp).background(Color(0xFFEEEEEE)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Image, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            }

            Column(Modifier.padding(16.dp)) {
                Text(report.price, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
                Text(report.adTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(" ${report.location}", color = Color.Gray, fontSize = 14.sp)
                }

                HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp) // Fixed deprecated Divider

                Text(if(isHindi) "रिपोर्ट विवरण" else "Report Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${if(isHindi) "कारण" else "Reason"}: ${report.reason}", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                Text(report.description, color = Color.Gray, modifier = Modifier.padding(top = 8.dp), lineHeight = 20.sp)

                Spacer(Modifier.height(24.dp))

                // Reporter Card styled like your app's seller card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = accentColor.copy(0.2f), modifier = Modifier.size(40.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(report.reportedBy.take(1), color = accentColor, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(report.reportedBy, fontWeight = FontWeight.Bold)
                            Text(if(isHindi) "रिपोर्टर प्रोफाइल" else "Reporter Profile", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}