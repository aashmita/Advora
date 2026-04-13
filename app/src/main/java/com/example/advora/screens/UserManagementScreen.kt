package com.example.advora.screens

import android.content.Intent
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
import com.example.advora.viewmodel.AdItem
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel

// Theme Colors
private val AdvoraOrange = Color(0xFFD08C60)
private val AdvoraBlack = Color(0xFF000000)
private val AdvoraBg = Color(0xFFF8F9FA) // Clean off-white for contrast

@Composable
fun UserManagementScreen(
    languageViewModel: LanguageViewModel,
    adViewModel: AdViewModel,
    onBack: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    var selectedUser by remember { mutableStateOf<UserItem?>(null) }
    var selectedAd by remember { mutableStateOf<AdItem?>(null) }

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
            onBack = { selectedUser = null },
            onAdClick = { selectedAd = it }
        )
    } else {
        UserListContent(
            userList = registeredUsers,
            isHindi = isHindi,
            onBack = onBack,
            onSelectUser = { selectedUser = it }
        )
    }
}

@Composable
fun AdvoraTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AdvoraBlack)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = AdvoraOrange)
        }
        Text(
            text = title,
            color = AdvoraOrange,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun UserListContent(
    userList: List<UserItem>,
    isHindi: Boolean,
    onBack: () -> Unit,
    onSelectUser: (UserItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Logic: Newest Users at top + Search Filter
    val filteredUsers = remember(searchQuery, userList.size) {
        userList.reversed().filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.email.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = AdvoraBg,
        topBar = { AdvoraTopBar(if (isHindi) "उपयोगकर्ता प्रबंधन" else "User Management", onBack) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Enhanced Search Box Visibility with Card Elevation
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if(isHindi) "नाम या ईमेल से खोजें..." else "Search by name or email...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Outlined.Search, null, tint = AdvoraOrange) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = AdvoraOrange
                    )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredUsers) { user ->
                    UserListItem(user, onSelectUser, isHindi)
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: UserItem, onClick: (UserItem) -> Unit, isHindi: Boolean) {
    val isActive = user.status.value.equals("ACTIVE", ignoreCase = true)
    val profileColor = getProfileColor(user.name)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick(user) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = profileColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        user.name.take(1).uppercase(),
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(user.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AdvoraBlack)
                Text(user.email, fontSize = 12.sp, color = Color.Gray)
            }

            // Status Badge
            Surface(
                color = if(isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if(isActive) (if(isHindi) "सक्रिय" else "ACTIVE") else (if(isHindi) "ब्लॉक" else "BLOCKED"),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if(isActive) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun UserDetailPage(user: UserItem, isHindi: Boolean, onBack: () -> Unit, onAdClick: (AdItem) -> Unit) {
    val isActive = user.status.value.equals("ACTIVE", ignoreCase = true)
    val profileColor = getProfileColor(user.name)

    Scaffold(
        containerColor = AdvoraBg,
        topBar = { AdvoraTopBar(if(isHindi) "उपयोगकर्ता विवरण" else "User Details", onBack) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = profileColor) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(user.name.take(1).uppercase(), fontSize = 32.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(user.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Text(user.email, color = Color.Gray)

                    Row(Modifier.padding(vertical = 20.dp), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                        DetailStat(if(isHindi) "विज्ञापन" else "Ads", "${user.postedAds.size}")
                        DetailStat(if(isHindi) "जुड़े" else "Joined", user.joinDate)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if(isActive) "ACTIVE" else "BLOCKED",
                                color = if(isActive) Color(0xFF2E7D32) else Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(if(isHindi) "स्थिति" else "Status", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { user.status.value = "ACTIVE" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "सक्रिय करें" else "Activate")
                        }
                        Button(
                            onClick = { user.status.value = "BLOCKED" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Block, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if(isHindi) "ब्लॉक करें" else "Block")
                        }
                    }
                }
                Text(
                    text = if(isHindi) "उपयोगकर्ता की सूचियाँ" else "USER'S LISTINGS",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AdvoraBlack
                )
            }
            items(user.postedAds) { ad ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onAdClick(ad) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = ad.imageUri, contentDescription = null, modifier = Modifier.size(65.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(ad.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(ad.price, color = AdvoraOrange, fontWeight = FontWeight.ExtraBold)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(16.dp), tint = Color.LightGray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserAdDetailScreen(ad: AdItem, user: UserItem, isHindi: Boolean, onBack: () -> Unit) {
    val context = LocalContext.current
    Scaffold(
        containerColor = Color.White,
        topBar = { AdvoraTopBar(if(isHindi) "विज्ञापन विवरण" else "Ad Details", onBack) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                AsyncImage(model = ad.imageUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(300.dp), contentScale = ContentScale.Crop)

                Column(Modifier.padding(20.dp).padding(bottom = 100.dp)) {
                    Text(ad.price, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = AdvoraOrange)
                    Text(ad.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AdvoraBlack)

                    Spacer(Modifier.height(16.dp))
                    Text(if(isHindi) "विवरण" else "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(ad.description, color = Color.DarkGray, fontSize = 15.sp, lineHeight = 22.sp)
                }
            }

            // Fixed Bottom Action Drawer Style
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter),
                shadowElevation = 16.dp,
                color = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(modifier = Modifier.size(45.dp), shape = CircleShape, color = getProfileColor(user.name)) {
                        Box(contentAlignment = Alignment.Center) { Text(user.name.take(1).uppercase(), fontWeight = FontWeight.Bold, color = Color.DarkGray) }
                    }
                    Column(Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(user.phone, color = Color.Gray, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out this ${ad.title} on Advora!")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AdvoraOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(if(isHindi) "शेयर" else "Share")
                    }
                }
            }
        }
    }
}

// Helpers
@Composable
fun DetailStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AdvoraBlack)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

fun getProfileColor(name: String): Color {
    val softColors = listOf(
        Color(0xFFE3F2FD), Color(0xFFF1F8E9), Color(0xFFFFF3E0),
        Color(0xFFF3E5F5), Color(0xFFE0F2F1), Color(0xFFFFEBEE)
    )
    val index = name.hashCode().let { if (it < 0) -it else it } % softColors.size
    return softColors[index]
}