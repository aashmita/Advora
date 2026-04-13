package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdItem
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAdsScreen(
    adViewModel: AdViewModel,
    languageViewModel: LanguageViewModel,
    onNavigate: (String) -> Unit,
    onAdClick: (AdItem) -> Unit,
    onLogout: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)
    val bgColor = Color(0xFFF5F5F7)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedStatusTab by remember { mutableIntStateOf(0) }

    var adToConfirm by remember { mutableStateOf<AdItem?>(null) }
    var isApproveAction by remember { mutableStateOf(true) }
    var showRejectionReasonDialog by remember { mutableStateOf(false) }
    var rejectionReasonText by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminDrawerContent(
                isHindi = isHindi,
                accentColor = accentColor,
                currentRoute = "manage_ads",
                onLogout = onLogout,
                onItemClick = { route ->
                    scope.launch { drawerState.close() }
                    onNavigate(route)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                ManageAdsTopBar(
                    isHindi = isHindi,
                    accentColor = accentColor,
                    onBack = { onNavigate("admin_dashboard") }
                )
            },
            containerColor = bgColor
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    CategoryFilterRow(
                        isHindi = isHindi,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it },
                        accentColor = accentColor,
                        bgColor = bgColor
                    )

                    val statusTabs = listOf(
                        if(isHindi) "लंबित" else "Pending",
                        if(isHindi) "सक्रिय" else "Active",
                        if(isHindi) "अस्वीकृत" else "Rejected"
                    )

                    TabRow(
                        selectedTabIndex = selectedStatusTab,
                        containerColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedStatusTab]),
                                color = accentColor
                            )
                        }
                    ) {
                        statusTabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedStatusTab == index,
                                onClick = { selectedStatusTab = index },
                                text = { Text(title, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }

                    // ✅ Dynamic filtering that correctly displays pre-existing ads by status
                    val filteredAds = adViewModel.ads.filter { ad ->
                        val statusTarget = when (selectedStatusTab) {
                            0 -> "Pending"
                            1 -> "Active"
                            else -> "Rejected"
                        }
                        ad.status.equals(statusTarget, ignoreCase = true) &&
                                (selectedCategory == "All" || ad.category == selectedCategory)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (filteredAds.isEmpty()) {
                            item {
                                Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(if(isHindi) "कोई विज्ञापन नहीं मिला" else "No ads found", color = Color.Gray)
                                }
                            }
                        }

                        items(filteredAds, key = { it.id }) { ad ->
                            AdminAdCard(
                                ad = ad,
                                accentColor = accentColor,
                                isApproved = ad.status.equals("Active", ignoreCase = true),
                                onClick = { onAdClick(ad) },
                                onApprove = {
                                    adToConfirm = ad
                                    isApproveAction = true
                                },
                                onReject = {
                                    adToConfirm = ad
                                    isApproveAction = false
                                }
                            )
                        }
                    }
                }

                adToConfirm?.let { ad ->
                    ConfirmActionDialog(
                        isHindi = isHindi,
                        adTitle = ad.title,
                        isApproveAction = isApproveAction,
                        onDismiss = { adToConfirm = null },
                        onConfirm = {
                            if (isApproveAction) {
                                adViewModel.updateAdDetails(ad.copy(status = "Active", rejectionReason = null))
                                adToConfirm = null
                            } else {
                                showRejectionReasonDialog = true
                            }
                        }
                    )
                }

                if (showRejectionReasonDialog) {
                    Dialog(onDismissRequest = { showRejectionReasonDialog = false }) {
                        Surface(shape = RoundedCornerShape(24.dp), color = Color.White) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text(
                                    if(isHindi) "अस्वीकृति का कारण" else "Reason for Rejection",
                                    fontWeight = FontWeight.Bold, fontSize = 18.sp
                                )
                                Spacer(Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = rejectionReasonText,
                                    onValueChange = { rejectionReasonText = it },
                                    placeholder = { Text(if(isHindi) "यहां कारण लिखें..." else "Enter reason here...") },
                                    modifier = Modifier.fillMaxWidth().height(120.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = accentColor,
                                        cursorColor = accentColor
                                    )
                                )
                                Spacer(Modifier.height(20.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    TextButton(onClick = { showRejectionReasonDialog = false }, modifier = Modifier.weight(1f)) {
                                        Text("Cancel", color = Color.Gray)
                                    }
                                    Button(
                                        onClick = {
                                            adToConfirm?.let { ad ->
                                                adViewModel.updateAdDetails(ad.copy(status = "Rejected", rejectionReason = rejectionReasonText))
                                            }
                                            showRejectionReasonDialog = false
                                            adToConfirm = null
                                            rejectionReasonText = ""
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Reject", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageAdsTopBar(isHindi: Boolean, accentColor: Color, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = accentColor, modifier = Modifier.size(24.dp))
        }
        Text(
            text = if (isHindi) "विज्ञापनों का प्रबंधन" else "Manage Ads",
            color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AdminAdCard(
    ad: AdItem,
    accentColor: Color,
    isApproved: Boolean,
    onClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ad.imageUri, contentDescription = null,
                modifier = Modifier.size(85.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(ad.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Text(ad.price, color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)

                if (ad.status.equals("Rejected", ignoreCase = true) && !ad.rejectionReason.isNullOrBlank()) {
                    Text("Reason: ${ad.rejectionReason}", color = Color.Red, fontSize = 11.sp, maxLines = 1)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isApproved) {
                        Surface(
                            modifier = Modifier.height(36.dp).weight(1f),
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("Approved", color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Button(
                            onClick = onApprove,
                            modifier = Modifier.height(36.dp).weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onReject,
                        modifier = Modifier.height(36.dp).weight(1f),
                        enabled = !ad.status.equals("Rejected", ignoreCase = true),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Reject", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    isHindi: Boolean,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    accentColor: Color,
    bgColor: Color
) {
    val categories = listOf(
        Triple("All", Icons.AutoMirrored.Filled.List, "सभी"),
        Triple("Electronics", Icons.Default.Devices, "इलेक्ट्रॉनिक्स"),
        Triple("Vehicles", Icons.Default.DirectionsCar, "वाहन"),
        Triple("Real Estate", Icons.Default.HomeWork, "प्रॉपर्टी"),
        Triple("Other", Icons.Default.MoreHoriz, "अन्य")
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { (eng, icon, hindi) ->
            val isSelected = selectedCategory == eng
            Surface(
                onClick = { onCategorySelected(eng) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) accentColor else bgColor.copy(alpha = 0.5f)
            ) {
                Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = if (isSelected) Color.White else Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(if (isHindi) hindi else eng, color = if (isSelected) Color.White else Color.Black, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ConfirmActionDialog(
    isHindi: Boolean,
    adTitle: String,
    isApproveAction: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White, tonalElevation = 8.dp) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (isApproveAction) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isApproveAction) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    modifier = Modifier.size(54.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (isApproveAction) (if(isHindi) "अनुमोदित करें?" else "Approve Ad?") else (if(isHindi) "अस्वीकार करें?" else "Reject Ad?"),
                    fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(text = "Action for: $adTitle", color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isApproveAction) Color(0xFF388E3C) else Color(0xFFD32F2F)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}