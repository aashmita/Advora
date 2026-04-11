package com.example.advora.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel

// --- Data Models ---
data class UserItem(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val totalAds: Int,
    val joinDate: String,
    var status: MutableState<String>,
    val postedAds: List<UserSampleAd> = emptyList()
)

data class UserSampleAd(
    val id: String,
    val title: String,
    val price: String,
    val location: String,
    val category: String,
    val description: String,
    val imageUrl: String,
    val status: String = "ACTIVE"
)

// --- Helpers ---
fun getSoftColor(name: String): Color {
    val colors = listOf(Color(0xFFE3F2FD), Color(0xFFF1F8E9), Color(0xFFFFF3E0), Color(0xFFF3E5F5), Color(0xFFE0F2F1), Color(0xFFFFEBEE))
    return colors[name.length % colors.size]
}

fun getDarkerTone(name: String): Color {
    val colors = listOf(Color(0xFF1976D2), Color(0xFF388E3C), Color(0xFFF57C00), Color(0xFF7B1FA2), Color(0xFF00796B), Color(0xFFD32F2F))
    return colors[name.length % colors.size]
}

@Composable
fun UserManagementScreen(
    languageViewModel: LanguageViewModel,
    adViewModel: AdViewModel,
    onBack: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)

    // Shared state for the user list to ensure updates reflect everywhere
    val userList = remember {
        val commonAds = listOf(
            UserSampleAd("a1", if(isHindi) "प्रोफेशनल कैमरा लेंस" else "Professional Camera Lens", "₹25,000", "Ujjain, MP", "Electronics", "High quality lens for professional photography. Minimal usage.", "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=1000"),
            UserSampleAd("a2", if(isHindi) "आईफोन 13 (नया जैसा)" else "iPhone 13 (Like New)", "₹42,000", "Indore, MP", "Mobile", "Excellent condition, 128GB variant. Includes all accessories.", "https://images.unsplash.com/photo-1633113088452-979a6c31f08e?q=80&w=1000")
        )
        mutableStateListOf(
            UserItem("1", "Rajesh Kumar", "rajesh@example.com", "+91 98765 43210", 12, "Jan 15, 2024", mutableStateOf("ACTIVE"), commonAds),
            UserItem("2", "Priya Sharma", "priya@example.com", "+91 98123 45678", 8, "Feb 20, 2024", mutableStateOf("ACTIVE"), commonAds),
            UserItem("3", "Amit Patel", "amit@example.com", "+91 97654 32109", 5, "Dec 10, 2023", mutableStateOf("BLOCKED"), commonAds),
            UserItem("4", "Suresh Raina", "suresh@example.com", "+91 91234 56789", 3, "Mar 05, 2024", mutableStateOf("ACTIVE"), commonAds),
            UserItem("5", "Megha Gupta", "megha@example.com", "+91 99887 76655", 7, "Jan 22, 2024", mutableStateOf("ACTIVE"), commonAds)
        )
    }

    var selectedUser by remember { mutableStateOf<UserItem?>(null) }
    var selectedAd by remember { mutableStateOf<UserSampleAd?>(null) }

    if (selectedAd != null && selectedUser != null) {
        AdminUserAdDetailScreen(
            ad = selectedAd!!,
            user = selectedUser!!,
            isHindi = isHindi,
            onBack = { selectedAd = null }
        )
    } else if (selectedUser != null) {
        UserDetailPage(
            user = selectedUser!!,
            isHindi = isHindi,
            accentColor = accentColor,
            onBack = { selectedUser = null },
            onAdClick = { selectedAd = it }
        )
    } else {
        UserListContent(
            userList = userList,
            languageViewModel = languageViewModel,
            accentColor = accentColor,
            onBack = onBack,
            onSelectUser = { selectedUser = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListContent(
    userList: List<UserItem>,
    languageViewModel: LanguageViewModel,
    accentColor: Color,
    onBack: () -> Unit,
    onSelectUser: (UserItem) -> Unit
) {
    val isHindi = languageViewModel.isHindi
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor) }
                Text(if (isHindi) "उपयोगकर्ता प्रबंधन" else "User Management", modifier = Modifier.weight(1f), color = accentColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Surface(
                    onClick = { languageViewModel.toggleLanguage() },
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(0.2f),
                    border = BorderStroke(1.dp, accentColor.copy(0.5f))
                ) {
                    Text(if (isHindi) "हिं" else "EN", color = accentColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if(isHindi) "नाम से खोजें..." else "Search by name...", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Outlined.Search, null, tint = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            LazyColumn {
                items(userList.filter { it.name.contains(searchQuery, true) }) { user ->
                    UserListItem(user, onSelectUser, isHindi)
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: UserItem, onClick: (UserItem) -> Unit, isHindi: Boolean) {
    val iconBg = getSoftColor(user.name)
    val iconTint = getDarkerTone(user.name)

    Column(modifier = Modifier.fillMaxWidth().background(Color.White).clickable { onClick(user) }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(45.dp), shape = CircleShape, color = iconBg) {
                Box(contentAlignment = Alignment.Center) {
                    Text(user.name.take(1), color = iconTint, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(user.email, fontSize = 13.sp, color = Color.Gray)
            }

            val statusText = if (user.status.value == "ACTIVE") (if(isHindi) "सक्रिय" else "ACTIVE") else (if(isHindi) "ब्लॉक" else "BLOCKED")
            Surface(
                color = if(user.status.value == "ACTIVE") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = statusText,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if(user.status.value == "ACTIVE") Color(0xFF2E7D32) else Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(16.dp), tint = Color.LightGray)
        }
        HorizontalDivider(modifier = Modifier.padding(start = 72.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.4f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailPage(
    user: UserItem,
    isHindi: Boolean,
    accentColor: Color,
    onBack: () -> Unit,
    onAdClick: (UserSampleAd) -> Unit
) {
    val iconBg = getSoftColor(user.name)
    val iconTint = getDarkerTone(user.name)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(isHindi) "विवरण" else "User Details", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(modifier = Modifier.size(90.dp), shape = CircleShape, color = iconBg) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(user.name.take(1), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = iconTint)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(user.name, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(user.email, color = Color.Gray)

                    Spacer(Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        DetailStat(label = if(isHindi) "कुल विज्ञापन" else "Total Ads", value = "${user.totalAds}")
                        DetailStat(label = if(isHindi) "शामिल हुए" else "Joined", value = user.joinDate)

                        val statusVal = if(user.status.value == "ACTIVE") (if(isHindi) "सक्रिय" else "ACTIVE") else (if(isHindi) "ब्लॉक" else "BLOCKED")
                        DetailStat(label = if(isHindi) "स्थिति" else "Status", value = statusVal,
                            color = if(user.status.value == "ACTIVE") Color(0xFF2E7D32) else Color.Red)
                    }

                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { user.status.value = "ACTIVE" },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "सक्रिय करें" else "Activate")
                        }
                        Button(
                            onClick = { user.status.value = "BLOCKED" },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                        ) {
                            Icon(Icons.Default.Block, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "ब्लॉक करें" else "Block")
                        }
                    }
                }
                Text(if(isHindi) "उपयोगकर्ता की सूचियाँ" else "USER'S LISTINGS", modifier = Modifier.padding(16.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }

            items(user.postedAds) { ad ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onAdClick(ad) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ad.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(65.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(ad.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(ad.price, color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                            Text("${ad.location} • ${ad.category}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(14.dp), tint = Color.LightGray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserAdDetailScreen(ad: UserSampleAd, user: UserItem, isHindi: Boolean, onBack: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val accentColor = Color(0xFFD08C60)

    Scaffold(
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 10.dp) {
                Row(Modifier.padding(16.dp).navigationBarsPadding(), verticalAlignment = Alignment.CenterVertically) {
                    // Share icon - Border removed, plain button
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "${ad.title} - ${ad.price}")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share"))
                        },
                        modifier = Modifier.size(48.dp)
                    ) { Icon(Icons.Default.Share, null, tint = Color.Black) }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${user.phone}") }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Icon(Icons.Default.Phone, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(if(isHindi) "विक्रेता से संपर्क करें" else "Contact Seller", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).background(Color.White)) {
            Box(Modifier.fillMaxWidth().height(320.dp)) {
                AsyncImage(
                    model = ad.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.statusBarsPadding().padding(16.dp).background(Color.White.copy(0.7f), CircleShape)
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            }

            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(ad.price, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
                    Spacer(Modifier.weight(1f))
                    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                        Text(if(isHindi) "सक्रिय" else "ACTIVE", color = Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(ad.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(" ${ad.location} • ${ad.category}", color = Color.Gray, fontSize = 14.sp)
                }

                HorizontalDivider(Modifier.padding(vertical = 20.dp), thickness = 0.5.dp)

                Text(if(isHindi) "विवरण" else "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(ad.description, color = Color.DarkGray, modifier = Modifier.padding(top = 8.dp), lineHeight = 20.sp, fontSize = 14.sp)

                Card(
                    modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth().clickable { showBottomSheet = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = getSoftColor(user.name), modifier = Modifier.size(45.dp)) {
                            Box(contentAlignment = Alignment.Center) { Text(user.name.take(1), fontWeight = FontWeight.Bold, color = getDarkerTone(user.name)) }
                        }
                        Column(Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(user.name, fontWeight = FontWeight.Bold)
                            Text(if(isHindi) "सत्यापित विक्रेता" else "Verified Seller", color = Color(0xFF2E7D32), fontSize = 12.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = getSoftColor(user.name)) {
                    Box(contentAlignment = Alignment.Center) { Text(user.name.take(1), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = getDarkerTone(user.name)) }
                }
                Spacer(Modifier.height(16.dp))
                Text(user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(user.email, color = Color.Gray)

                Row(Modifier.padding(vertical = 20.dp), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                    DetailStat(if(isHindi) "विज्ञापन" else "Ads", user.totalAds.toString())
                    DetailStat(if(isHindi) "जुड़े" else "Joined", user.joinDate)
                }

                Button(
                    onClick = { showBottomSheet = false },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(if(isHindi) "बंद करें" else "Close")
                }
            }
        }
    }
}

@Composable
fun DetailStat(label: String, value: String, color: Color = Color.Black) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}