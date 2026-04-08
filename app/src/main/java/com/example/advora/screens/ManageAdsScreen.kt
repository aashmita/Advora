package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

    // Track local approvals to change UI state to grey
    var localApprovedIds by remember { mutableStateOf(setOf<String>()) }

    var adToConfirm by remember { mutableStateOf<AdItem?>(null) }
    var isApproveAction by remember { mutableStateOf(true) }

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
                AdminTopBar(
                    isHindi = isHindi,
                    onLogout = onLogout,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onToggleLang = { languageViewModel.toggleLanguage() }
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

                    val statusTabs = listOf("Active", "Sold", "Inactive")
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

                    val filteredAds = adViewModel.ads.filter { ad ->
                        val statusTarget = when (selectedStatusTab) {
                            0 -> "Active"
                            1 -> "Sold"
                            else -> "Inactive"
                        }
                        ad.status == statusTarget && (selectedCategory == "All" || ad.category == selectedCategory)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredAds, key = { it.id }) { ad ->
                            AdminAdCard(
                                ad = ad,
                                accentColor = accentColor,
                                isApproved = localApprovedIds.contains(ad.id),
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
                                // Mark as approved locally to update UI to grey
                                localApprovedIds = localApprovedIds + ad.id
                                // You can call your backend update here if necessary
                            } else {
                                adViewModel.deleteAd(ad.id)
                            }
                            adToConfirm = null
                        }
                    )
                }
            }
        }
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ad.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(ad.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Text(ad.price, color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isApproved) {
                        // Greyed out "Approved" block
                        Surface(
                            modifier = Modifier.height(36.dp).weight(1f),
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("Approved", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                        enabled = !isApproved, // Disable reject if already approved
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
        Triple("Jobs", Icons.Default.Work, "नौकरी"),
        Triple("Rentals", Icons.Default.Home, "किराया"),
        Triple("Buy/Sell", Icons.Default.ShoppingCart, "खरीदें")
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { (eng, icon, hindi) ->
            val isSelected = selectedCategory == eng
            Surface(
                onClick = { onCategorySelected(eng) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) accentColor else bgColor.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        null,
                        tint = if (isSelected) Color.White else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (isHindi) hindi else eng,
                        color = if (isSelected) Color.White else Color.Black,
                        fontSize = 13.sp
                    )
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
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isApproveAction) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (isApproveAction) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    modifier = Modifier.size(54.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (isApproveAction) "Approve Ad?" else "Reject Ad?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Action for: $adTitle",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
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