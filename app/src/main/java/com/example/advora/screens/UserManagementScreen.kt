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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel

// Data Models
data class UserItem(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val totalAds: Int,
    val joinDate: String,
    var status: String,
    val postedAds: List<UserSampleAd> = emptyList()
)

data class UserSampleAd(
    val id: String,
    val title: String,
    val price: String,
    val location: String,
    val category: String,
    val description: String
)

@Composable
fun UserManagementScreen(
    languageViewModel: LanguageViewModel,
    adViewModel: AdViewModel,
    onBack: () -> Unit
) {
    var selectedUser by remember { mutableStateOf<UserItem?>(null) }
    var selectedAd by remember { mutableStateOf<UserSampleAd?>(null) }

    val accentColor = Color(0xFFD08C60)
    val isHindi = languageViewModel.isHindi

    // Navigation Logic
    if (selectedAd != null && selectedUser != null) {
        // Detailed Ad View (Matching Image 5)
        AdminUserAdDetailScreen(
            ad = selectedAd!!,
            user = selectedUser!!,
            isHindi = isHindi,
            onBack = { selectedAd = null }
        )
    } else if (selectedUser != null) {
        // User Detail Page (Matching Image 2)
        UserDetailPage(
            user = selectedUser!!,
            isHindi = isHindi,
            accentColor = accentColor,
            onBack = { selectedUser = null },
            onAdClick = { selectedAd = it }
        )
    } else {
        // Main User List (Matching Image 4)
        UserListContent(
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
    languageViewModel: LanguageViewModel,
    accentColor: Color,
    onBack: () -> Unit,
    onSelectUser: (UserItem) -> Unit
) {
    val isHindi = languageViewModel.isHindi
    var searchQuery by remember { mutableStateOf("") }

    // Mock Data: 10 Users with Sample Ads
    val userList = remember {
        val commonAds = listOf(
            UserSampleAd("a1", "Sales Executive Needed", "₹25,000", "Ujjain, MP", "Jobs", "We are seeking a motivated Sales Executive..."),
            UserSampleAd("a2", "iPhone 13 (Like New)", "₹42,000", "Indore, MP", "Buy/Sell", "Excellent condition, 128GB variant...")
        )
        listOf(
            UserItem("1", "Rajesh Kumar", "rajesh@example.com", "+91 98765 43210", 12, "Jan 15, 2024", "ACTIVE", commonAds),
            UserItem("2", "Priya Sharma", "priya@example.com", "+91 98123 45678", 8, "Feb 20, 2024", "ACTIVE", commonAds),
            UserItem("3", "Amit Patel", "amit@example.com", "+91 97654 32109", 5, "Dec 10, 2023", "BLOCKED", commonAds),
            UserItem("4", "Suresh Raina", "suresh@example.com", "+91 91234 56789", 3, "Mar 05, 2024", "ACTIVE", commonAds),
            UserItem("5", "Megha Gupta", "megha@example.com", "+91 99887 76655", 7, "Jan 22, 2024", "ACTIVE", commonAds),
            UserItem("6", "Vikram Singh", "vikram@example.com", "+91 94433 22110", 2, "Nov 15, 2023", "ACTIVE", commonAds),
            UserItem("7", "Anjali Verma", "anjali@example.com", "+91 88776 65544", 9, "Feb 01, 2024", "ACTIVE", commonAds),
            UserItem("8", "Sunil Dhar", "sunil@example.com", "+91 77665 54433", 4, "Oct 30, 2023", "BLOCKED", commonAds),
            UserItem("9", "Kavita Rao", "kavita@example.com", "+91 66554 43322", 6, "Mar 12, 2024", "ACTIVE", commonAds),
            UserItem("10", "Deepak Jha", "deepak@example.com", "+91 55443 32211", 1, "Feb 28, 2024", "ACTIVE", commonAds)
        )
    }

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
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor)
                }
                Text(
                    text = if (isHindi) "उपयोगकर्ता प्रबंधन" else "User Management",
                    modifier = Modifier.weight(1f),
                    color = accentColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                // Language Toggle (Matching TopBar Theme)
                Surface(
                    onClick = { languageViewModel.toggleLanguage() },
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(0.2f),
                    border = BorderStroke(1.dp, accentColor.copy(0.5f))
                ) {
                    Text(
                        text = if (isHindi) "हिं" else "EN",
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar (Matching Image 4)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if(isHindi) "नाम से खोजें..." else "Search by name...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(userList.filter { it.name.contains(searchQuery, true) }) { user ->
                    UserListItem(user, onSelectUser)
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: UserItem, onClick: (UserItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick(user) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                color = Color(0xFFD08C60).copy(0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(user.name.take(1), color = Color(0xFFD08C60), fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(user.email, fontSize = 13.sp, color = Color.Gray)
            }
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
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null) }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Contact: ${user.phone}") },
                            onClick = { showMenu = false },
                            leadingIcon = { Icon(Icons.Default.Phone, null, tint = accentColor) }
                        )
                        DropdownMenuItem(
                            text = { Text(if(user.status == "ACTIVE") "Block User" else "Unblock User") },
                            onClick = { showMenu = false },
                            leadingIcon = { Icon(Icons.Default.Block, null, tint = Color.Red) }
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                // Header Profile (Matching Image 2 Layout)
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(modifier = Modifier.size(90.dp), shape = CircleShape, color = Color(0xFFD08C60).copy(0.1f)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(user.name.take(1), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(user.name, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(user.email, color = Color.Gray)

                    Spacer(Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        DetailStat(label = if(isHindi) "कुल विज्ञापन" else "Total Ads", value = "${user.totalAds}")
                        DetailStat(label = if(isHindi) "शामिल हुए" else "Joined", value = user.joinDate)
                        DetailStat(label = if(isHindi) "स्थिति" else "Status", value = user.status,
                            color = if(user.status == "ACTIVE") Color(0xFF2E7D32) else Color.Red)
                    }

                    // Action Buttons (Matching Image 2)
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "सक्रिय करें" else "Activate")
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                        ) {
                            Icon(Icons.Default.PersonRemove, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "ब्लॉक करें" else "Block")
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Shield, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Text(
                    text = if(isHindi) "उपयोगकर्ता की सूचियाँ" else "USER'S LISTINGS",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            items(user.postedAds) { ad ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onAdClick(ad) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFF0F0F0)) {
                            Icon(Icons.Default.Image, null, tint = Color.LightGray, modifier = Modifier.padding(12.dp))
                        }
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(ad.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(ad.price, color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
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
fun AdminUserAdDetailScreen(
    ad: UserSampleAd,
    user: UserItem,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth().height(80.dp), color = Color.White, shadowElevation = 8.dp) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.Share, null, modifier = Modifier.padding(12.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08C60))
                    ) {
                        Text(if(isHindi) "विक्रेता से संपर्क करें" else "Contact Seller", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) {
            // Placeholder Image (Matching Image 5)
            Box(Modifier.fillMaxWidth().height(280.dp).background(Color(0xFFF0F0F0))) {
                Row(Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Surface(onClick = onBack, shape = CircleShape, color = Color.White, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, Modifier.padding(8.dp))
                    }
                    Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.FavoriteBorder, null, Modifier.padding(8.dp))
                    }
                }
                Icon(Icons.Default.DesktopWindows, null, Modifier.size(100.dp).align(Alignment.Center), tint = Color.LightGray)
            }

            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(ad.price, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFD08C60))
                    Spacer(Modifier.weight(1f))
                    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                        Text("ACTIVE", color = Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(ad.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(" ${ad.location} • Posted Just now", color = Color.Gray, fontSize = 13.sp)
                }

                HorizontalDivider(Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color.LightGray.copy(0.3f))

                Text(if(isHindi) "विवरण" else "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(ad.description, color = Color.Gray, modifier = Modifier.padding(top = 8.dp), lineHeight = 20.sp)

                // User Info Block (Matching Image 5)
                Card(
                    modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = Color(0xFFD08C60).copy(0.2f), modifier = Modifier.size(45.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(user.name.take(1), fontWeight = FontWeight.Bold, color = Color(0xFFD08C60))
                            }
                        }
                        Column(Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(user.name, fontWeight = FontWeight.Bold)
                            Text("Verified Seller", color = Color(0xFF2E7D32), fontSize = 12.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailStat(label: String, value: String, color: Color = Color.Black) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}