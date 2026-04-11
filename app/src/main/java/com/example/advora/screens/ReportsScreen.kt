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
    var isResolved: Boolean = false
)

@Composable
fun ReportsScreen(
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit
) {
    val accentColor = Color(0xFFD08C60)
    val isHindi = languageViewModel.isHindi

    var selectedTab by remember { mutableStateOf("Pending") }
    var selectedReport by remember { mutableStateOf<ReportItem?>(null) }
    var reportToResolve by remember { mutableStateOf<ReportItem?>(null) }

    val reportsList = remember { mutableStateListOf(
        ReportItem("1", "iPhone 14 Pro Max 256GB", "John Doe", "Suspicious pricing", "2026-03-22", "₹85,000", "Indore, MP", "The price is significantly lower than market value for a 14 Pro Max in this condition. Seller refuses to meet in person.", "https://images.unsplash.com/photo-1663499482523-1c0c1bae4ce1?q=80&w=1000&auto=format&fit=crop"),
        ReportItem("2", "Honda City 2020 VX Model", "Jane Smith", "Fake images", "2026-03-21", "₹7,20,000", "Ujjain, MP", "The images provided are stock photos from a dealership website and do not represent the actual vehicle being sold.", "https://images.unsplash.com/photo-1590362891991-f776e747a588?q=80&w=1000&auto=format&fit=crop"),
        ReportItem("3", "Samsung S23 Ultra", "Rahul Sharma", "Duplicate listing", "2026-03-20", "₹65,000", "Dewas, MP", "This exact listing has been posted multiple times by the same user across different categories to spam the feed.", "https://images.unsplash.com/photo-1678911820864-e2c567c655d7?q=80&w=1000&auto=format&fit=crop")
    )}

    // ENHANCED CONFIRM ACTION DIALOG (Image 2 style)
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
                                val index = reportsList.indexOf(reportToResolve)
                                if (index != -1) reportsList[index] = reportToResolve!!.copy(isResolved = true)
                                reportToResolve = null
                                selectedReport = null // Navigate back to list if in detail view
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
            containerColor = Color(0xFFF5F5F7) // Match app theme background
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                // Tabs
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE0E0E0).copy(0.6f)).padding(4.dp)) {
                    TabButton(if (isHindi) "लंबित" else "Pending", selectedTab == "Pending", accentColor, Modifier.weight(1f)) { selectedTab = "Pending" }
                    TabButton(if (isHindi) "हल किया गया" else "Resolved", selectedTab == "Resolved", accentColor, Modifier.weight(1f)) { selectedTab = "Resolved" }
                }

                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val filteredList = reportsList.filter { if (selectedTab == "Pending") !it.isResolved else it.isResolved }
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
                                putExtra(android.content.Intent.EXTRA_TEXT, "Check out this ad: ${report.adTitle} for ${report.price}")
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
                            // Logic for Contact Seller (Opening Dialer)
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
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F7)) // App Theme Background
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            // --- Image Section ---
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
                    var isFav by remember { mutableStateOf(false) }
                    FloatingActionButton(
                        onClick = { isFav = !isFav },
                        containerColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp),
                        elevation = FloatingActionButtonDefaults.elevation(2.dp)
                    ) {
                        Icon(
                            if(isFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            null,
                            tint = if(isFav) Color.Red else Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // --- Title & Price Section ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(report.price, fontSize = 22.sp, fontWeight = FontWeight.Black, color = accentColor)
                    Spacer(Modifier.weight(1f))
                    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                        Text("ACTIVE", color = Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Text(report.adTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(top = 2.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(" ${report.location} • Posted ${report.date}", color = Color.Gray, fontSize = 13.sp)
                }

                Spacer(Modifier.height(16.dp))

                // --- Description Card ---
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

                // --- Report Info Card ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE).copy(alpha = 0.7f)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2).copy(alpha = 0.5f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Report, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "रिपोर्ट जानकारी" else "Report Info", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 15.sp)
                        }
                        Text("${if(isHindi) "कारण" else "Reason"}: ${report.reason}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black, modifier = Modifier.padding(top = 6.dp))

                        // Smaller Suspend Button at the bottom of the section
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

                // --- Reporter Profile Card (Working) ---
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { /* Action for viewing reporter profile */ },
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
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(14.dp), tint = Color.LightGray)
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