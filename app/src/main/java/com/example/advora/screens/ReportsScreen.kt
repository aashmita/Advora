package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdItem
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel

data class ReportItem(
    val id: String,
    val adTitle: String,
    val reportedBy: String,
    val reason: String,
    val date: String,
    val price: String,
    val location: String,
    val description: String,
    val adImageUrl: String,
    var isResolved: Boolean = false,
    val linkedAd: AdItem? = null
)

@Composable
fun ReportsScreen(
    adViewModel: AdViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit
) {
    val accentColor = Color(0xFFD08C60)
    val isHindi = languageViewModel.isHindi

    var selectedTab by remember { mutableStateOf("Pending") }
    var selectedReport by remember { mutableStateOf<ReportItem?>(null) }
    var reportToResolve by remember { mutableStateOf<ReportItem?>(null) }

    // Logic to track resolved dynamic IDs locally in the screen state
    val resolvedDynamicIds = remember { mutableStateListOf<String>() }

    val staticReports = remember { mutableStateListOf(
        ReportItem("1", "iPhone 14 Pro Max 256GB", "John Doe", "Suspicious pricing", "2026-03-22", "85,000", "Indore, MP", "The price is significantly lower than market value for a 14 Pro Max in this condition. Seller refuses to meet in person.", "https://images.unsplash.com/photo-1663499482523-1c0c1bae4ce1?q=80&w=1000&auto=format&fit=crop"),
        ReportItem("2", "Honda City 2020 VX Model", "Jane Smith", "Fake images", "2026-03-21", "7,20,000", "Ujjain, MP", "The images provided are stock photos from a dealership website and do not represent the actual vehicle being sold.", "https://images.unsplash.com/photo-1590362891991-f776e747a588?q=80&w=1000&auto=format&fit=crop"),
        ReportItem("3", "Samsung S23 Ultra", "Rahul Sharma", "Duplicate listing", "2026-03-20", "65,000", "Dewas, MP", "This exact listing has been posted multiple times by the same user across different categories to spam the feed.", "https://images.unsplash.com/photo-1678911820864-e2c567c655d7?q=80&w=1000&auto=format&fit=crop")
    )}

    // Mapping dynamic reports and checking if they exist in the resolved list
    val dynamicReports = adViewModel.reportedAds.map { (ad, reason) ->
        ReportItem(
            id = ad.id,
            adTitle = ad.title,
            reportedBy = "App User",
            reason = reason,
            date = "Just Now",
            price = ad.price,
            location = ad.location,
            description = ad.description,
            adImageUrl = ad.imageUri,
            linkedAd = ad,
            isResolved = resolvedDynamicIds.contains(ad.id)
        )
    }.reversed() // Recent reported at the top

    val allReports = dynamicReports + staticReports

    if (reportToResolve != null) {
        Dialog(onDismissRequest = { reportToResolve = null }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if(isHindi) "कार्रवाई की पुष्टि करें" else "Confirm Action",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = if(isHindi) "क्या आप इस विज्ञापनों को निलंबित करना चाहते हैं?" else "Are you sure you want to suspend this reported advertisement?",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(32.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { reportToResolve = null }) {
                            Text(if(isHindi) "रद्द करें" else "Cancel", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val report = reportToResolve!!

                                if (report.linkedAd != null) {
                                    // Handle Real Ad: Update status and move to resolved
                                    adViewModel.updateAdDetails(report.linkedAd.copy(status = "Suspended"))
                                    resolvedDynamicIds.add(report.id)

                                    // Send Notification to User
                                    adViewModel.addUserNotification(
                                        title = "Ad Suspended",
                                        message = "Admin suspended your ad '${report.adTitle}' due to reports.",
                                        userId = report.linkedAd.userId
                                    )
                                } else {
                                    // Handle Static Data
                                    val index = staticReports.indexOfFirst { it.id == report.id }
                                    if (index != -1) staticReports[index] = staticReports[index].copy(isResolved = true)
                                }

                                reportToResolve = null
                                selectedReport = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(if(isHindi) "निलंबित करें" else "Suspend", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (selectedReport == null) {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(Color.Black).statusBarsPadding()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor) }
                        Text(if (isHindi) "रिपोर्ट्स" else "Reports", color = accentColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            containerColor = Color(0xFFF5F5F7)
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE0E0E0).copy(0.6f)).padding(4.dp)) {
                    TabButton(if (isHindi) "लंबित" else "Pending", selectedTab == "Pending", accentColor, Modifier.weight(1f)) { selectedTab = "Pending" }
                    TabButton(if (isHindi) "हल किया गया" else "Resolved", selectedTab == "Resolved", accentColor, Modifier.weight(1f)) { selectedTab = "Resolved" }
                }

                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val filteredList = allReports.filter { if (selectedTab == "Pending") !it.isResolved else it.isResolved }
                    items(filteredList) { report ->
                        CompactReportCard(report, isHindi, onAction = { reportToResolve = report }, onClick = { selectedReport = report })
                    }
                }
            }
        }
    } else {
        FullReportDetailView(
            report = selectedReport!!,
            isHindi = isHindi,
            accentColor = accentColor,
            onBack = { selectedReport = null },
            onSuspend = { reportToResolve = it }
        )
    }
}

@Composable
fun CompactReportCard(report: ReportItem, isHindi: Boolean, onAction: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Error, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(report.adTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                }
                Surface(color = Color(0xFFFFF3E0), shape = RoundedCornerShape(4.dp), modifier = Modifier.padding(vertical = 6.dp)) {
                    Text(
                        text = "Reason: ${report.reason}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFE65100)
                    )
                }
                Text("By: ${report.reportedBy} • ${report.date}", fontSize = 12.sp, color = Color.Gray)
            }

            if (!report.isResolved) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(if(isHindi) "कार्रवाई" else "Action", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullReportDetailView(
    report: ReportItem,
    isHindi: Boolean,
    accentColor: Color,
    onBack: () -> Unit,
    onSuspend: (ReportItem) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 12.dp, color = Color.White) {
                Row(
                    modifier = Modifier.padding(16.dp).navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedIconButton(
                        onClick = {
                            val sendIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, "Reviewing reported ad: ${report.adTitle}")
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(sendIntent, null))
                        },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray.copy(0.5f))
                    ) {
                        Icon(Icons.Outlined.Share, null, tint = accentColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val intent = android.content.Intent(android.content.Intent.ACTION_DIAL)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Phone, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(if(isHindi) "विक्रेता से संपर्क करें" else "Contact Seller", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F7)).verticalScroll(rememberScrollState()).padding(padding)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = report.adImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FloatingActionButton(onClick = onBack, containerColor = Color.White, shape = CircleShape, modifier = Modifier.size(40.dp), elevation = FloatingActionButtonDefaults.elevation(2.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(report.price, fontSize = 22.sp, fontWeight = FontWeight.Black, color = accentColor)
                    Spacer(Modifier.weight(1f))
                    Surface(color = if(report.isResolved) Color.LightGray else Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                        Text(if(report.isResolved) "SUSPENDED" else "ACTIVE", color = if(report.isResolved) Color.DarkGray else Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Text(report.adTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(top = 2.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(" ${report.location} • Posted ${report.date}", color = Color.Gray, fontSize = 13.sp)
                }

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.5.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(if(isHindi) "विवरण" else "Description", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        Spacer(Modifier.height(8.dp))
                        Text(report.description, color = Color.DarkGray, lineHeight = 20.sp, fontSize = 14.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if(report.isResolved) Color(0xFFE8F5E9).copy(0.5f) else Color(0xFFFFEBEE).copy(alpha = 0.7f)),
                    border = BorderStroke(1.dp, if(report.isResolved) Color(0xFF2E7D32).copy(0.3f) else Color(0xFFFFCDD2).copy(alpha = 0.5f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(if(report.isResolved) Icons.Default.CheckCircle else Icons.Default.Report, contentDescription = null, tint = if(report.isResolved) Color(0xFF2E7D32) else Color.Red, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(report.isResolved) (if(isHindi) "समाधान हो गया" else "Resolved") else (if(isHindi) "रिपोर्ट जानकारी" else "Report Info"), fontWeight = FontWeight.Bold, color = if(report.isResolved) Color(0xFF2E7D32) else Color.Red, fontSize = 15.sp)
                        }
                        Text("${if(isHindi) "कारण" else "Reason"}: ${report.reason}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black, modifier = Modifier.padding(top = 6.dp))

                        if (!report.isResolved) {
                            Button(
                                onClick = { onSuspend(report) },
                                modifier = Modifier.padding(top = 12.dp).fillMaxWidth().height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if(isHindi) "निलंबित करें" else "Suspend Advertisement", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = accentColor.copy(0.15f), modifier = Modifier.size(44.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(report.reportedBy.take(1), color = accentColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(report.reportedBy, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                            Text(if(isHindi) "सत्यापित रिपोर्टर" else "Verified Reporter", color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Spacer(Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = if (isSelected) accentColor else Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}