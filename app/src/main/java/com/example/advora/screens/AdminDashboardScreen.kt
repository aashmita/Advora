package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch

// --- Data Models ---
enum class ActivityType { NEW_AD, USER_REPORTED, NEW_USER }

data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val targetName: String,
    val timestamp: String,
    val detailEn: String,
    val detailHi: String,
    val targetRoute: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adViewModel: AdViewModel,
    languageViewModel: LanguageViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)
    val bgColor = Color(0xFFF5F5F7)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val activities = remember {
        listOf(
            ActivityItem("1", ActivityType.NEW_AD, "iPhone 14 Pro Max", "5 mins ago",
                "A new ad for iPhone 14 Pro Max was posted in Freeganj, Ujjain. It is pending review.",
                "iPhone 14 Pro Max के लिए एक नया विज्ञापन फ्रीगंज, उज्जैन में पोस्ट किया गया था। यह समीक्षा के लिए लंबित है।",
                "manage_ads"),
            ActivityItem("2", ActivityType.USER_REPORTED, "JohnDoe123", "15 mins ago",
                "User reported for suspicious activity in the property section. Manual verification required.",
                "प्रॉपर्टी सेक्शन में संदिग्ध गतिविधि के लिए यूज़र की रिपोर्ट की गई। मैनुअल सत्यापन आवश्यक है।",
                "reports"),
            ActivityItem("3", ActivityType.NEW_USER, "Sarah Smith", "1 hour ago",
                "New business account registration from Nanakheda area.",
                "नानाखेड़ा क्षेत्र से नया बिजनेस अकाउंट पंजीकरण प्राप्त हुआ।",
                "user_management")
        )
    }

    var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }

    if (selectedActivity != null) {
        ActivityDetailScreen(
            activity = selectedActivity!!,
            isHindi = isHindi,
            accentColor = accentColor,
            onBack = { selectedActivity = null },
            onAction = { route ->
                selectedActivity = null
                onNavigate(route)
            }
        )
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AdminDrawerContent(
                    isHindi = isHindi,
                    accentColor = accentColor,
                    currentRoute = "admin_dashboard",
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
                        hasNewNotif = adViewModel.hasNewNotifications,
                        onNotificationClick = { onNavigate("admin_notifications") },
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onToggleLang = { languageViewModel.toggleLanguage() }
                    )
                },
                containerColor = bgColor
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = if (isHindi) "डैशबोर्ड अवलोकन" else "Dashboard Overview",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdminStatCard(if (isHindi) "कुल विज्ञापन" else "Total Ads", "${adViewModel.ads.size}", Icons.Default.Description, accentColor, Modifier.weight(1f))
                        AdminStatCard(if (isHindi) "कुल यूज़र" else "Total Users", "856", Icons.Default.Group, Color(0xFF4CAF50), Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdminStatCard(if (isHindi) "रिपोर्ट्स" else "Reports", "${adViewModel.reportedAds.size}", Icons.Default.Warning, Color(0xFFE53935), Modifier.weight(1f))
                        AdminStatCard(if (isHindi) "सक्रिय" else "Active", "${adViewModel.ads.filter { it.status == "Active" }.size}", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFF2196F3), Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(32.dp))

                    Text(if (isHindi) "त्वरित क्रियाएं" else "Quick Actions", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            AdminActionItem(if (isHindi) "विज्ञापनों का प्रबंधन" else "Manage All Ads", Icons.Default.Settings, accentColor) { onNavigate("manage_ads") }
                            HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.3f))
                            AdminActionItem(if (isHindi) "रिपोर्ट्स देखें" else "Review Reports", Icons.Default.Report, Color(0xFFE53935)) { onNavigate("reports") }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    Text(if (isHindi) "हाल की गतिविधि" else "Recent Activity", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            activities.forEachIndexed { index, item ->
                                AdminActivityRow(
                                    label = if (isHindi) getHindiLabel(item.type) else getEnglishLabel(item.type),
                                    target = item.targetName,
                                    time = translateTime(item.timestamp, isHindi),
                                    onClick = { selectedActivity = item }
                                )
                                if (index < activities.size - 1) {
                                    HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.2f))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNotificationScreen(
    adViewModel: AdViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)

    // ✅ Accessing live notifications from AdViewModel
    val adminNotifications = adViewModel.notifications

    // ✅ Clear the notification badge when this screen is viewed
    LaunchedEffect(Unit) {
        adViewModel.setNewNotification(false)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor, modifier = Modifier.size(24.dp))
                }
                Text(
                    text = if (isHindi) "एडमिन सूचनाएं" else "Admin Notifications",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = Color(0xFFF5F5F7)
    ) { padding ->
        if (adminNotifications.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(if(isHindi) "कोई सूचना नहीं" else "No notifications", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)) {
                item { Spacer(Modifier.height(16.dp)) }
                items(adminNotifications) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            // Unread status indicator
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .size(8.dp)
                                    .background(if (!item.isRead) Color.Red else accentColor, CircleShape)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                    Text(item.time, fontSize = 11.sp, color = Color.Gray)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(item.message, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)

                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    TextButton(
                                        onClick = { adViewModel.notifications.remove(item) },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(if(isHindi) "हटाएं" else "Dismiss", color = accentColor, fontSize = 12.sp)
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

// --- Support Components ---

@Composable
fun AdminTopBar(
    isHindi: Boolean,
    hasNewNotif: Boolean,
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit,
    onToggleLang: () -> Unit
) {
    val accentColor = Color(0xFFD08C60)
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Black).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, "", tint = accentColor, modifier = Modifier.size(26.dp))
            }
            Text(if (isHindi) "एडवोरा" else "Advora", color = accentColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                onClick = onToggleLang,
                shape = RoundedCornerShape(8.dp),
                color = accentColor.copy(0.2f),
                border = BorderStroke(1.dp, accentColor.copy(0.5f))
            ) {
                Text(if (isHindi) "हिं" else "EN", color = accentColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onNotificationClick) {
                BadgedBox(badge = {
                    if (hasNewNotif) {
                        Badge(containerColor = Color.Red)
                    }
                }) {
                    Icon(Icons.Default.Notifications, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(label: String, value: String, icon: ImageVector, iconColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(iconColor.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Column(Modifier.padding(start = 12.dp)) {
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                Text(label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun AdminActivityRow(label: String, target: String, time: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(Color(0xFFD08C60), CircleShape))
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Text("$label $target", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun AdminActionItem(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(38.dp).background(color.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Text(title, Modifier.weight(1f).padding(start = 14.dp), fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun AdminDrawerContent(isHindi: Boolean, accentColor: Color, currentRoute: String, onLogout: () -> Unit, onItemClick: (String) -> Unit) {
    ModalDrawerSheet(drawerContainerColor = Color.Black, drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp), modifier = Modifier.width(300.dp)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(if (isHindi) "एडवोरा" else "Advora", color = accentColor, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp))
            DrawerMenuItem(if (isHindi) "डैशबोर्ड" else "Dashboard", Icons.Default.Home, currentRoute == "admin_dashboard") { onItemClick("admin_dashboard") }
            DrawerMenuItem(if (isHindi) "विज्ञापनों का प्रबंधन" else "Manage Ads", Icons.AutoMirrored.Filled.FormatListBulleted, currentRoute == "manage_ads") { onItemClick("manage_ads") }
            DrawerMenuItem(if (isHindi) "यूज़र प्रबंधन" else "User Management", Icons.Default.Person, currentRoute == "user_management") { onItemClick("user_management") }
            DrawerMenuItem(if (isHindi) "रिपोर्ट्स" else "Reports", Icons.Default.Info, currentRoute == "reports") { onItemClick("reports") }
            DrawerMenuItem(if (isHindi) "प्रोफ़ाइल" else "Profile", Icons.Default.AccountCircle, currentRoute == "admin_profile") { onItemClick("admin_profile") }
            DrawerMenuItem(if (isHindi) "नक्शा" else "Map", Icons.Default.LocationOn, currentRoute == "map") { onItemClick("map") }
            Spacer(modifier = Modifier.weight(1f))
            Surface(onClick = onLogout, color = Color(0xFF421212), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.Red)
                    Spacer(Modifier.width(12.dp))
                    Text(if (isHindi) "लॉगआउट" else "Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, color = if (isSelected) Color(0xFF2C1C12) else Color.Transparent, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(50.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(icon, null, tint = Color(0xFFD08C60), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = if (isSelected) Color.White else Color(0xFFD08C60).copy(0.7f), fontSize = 16.sp)
        }
    }
}

@Composable
fun ActivityDetailScreen(
    activity: ActivityItem,
    isHindi: Boolean,
    accentColor: Color,
    onBack: () -> Unit,
    onAction: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.Black).statusBarsPadding().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Text(if (isHindi) "गतिविधि विवरण" else "Activity Details", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(translateTime(activity.timestamp, isHindi), color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(activity.targetName, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, modifier = Modifier.padding(vertical = 4.dp))
                    HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                    Text(text = if (isHindi) activity.detailHi else activity.detailEn, fontSize = 14.sp, lineHeight = 22.sp, color = Color.DarkGray)
                    Spacer(Modifier.height(24.dp))
                    val btnText = when(activity.type) {
                        ActivityType.NEW_AD -> if (isHindi) "विज्ञापन समीक्षा करें" else "Review Ad"
                        ActivityType.USER_REPORTED -> if (isHindi) "रिपोर्ट देखें" else "View Report"
                        ActivityType.NEW_USER -> if (isHindi) "यूज़र प्रबंधित करें" else "Manage User"
                    }
                    Button(onClick = { onAction(activity.targetRoute) }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = accentColor)) {
                        Text(btnText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        Text(if (isHindi) "पीछे हटें" else "Go Back", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// Helpers
fun getHindiLabel(type: ActivityType) = when(type) {
    ActivityType.NEW_AD -> "नया विज्ञापन:"
    ActivityType.USER_REPORTED -> "रिपोर्ट किया गया:"
    ActivityType.NEW_USER -> "नया यूज़र:"
}
fun getEnglishLabel(type: ActivityType) = when(type) {
    ActivityType.NEW_AD -> "New ad:"
    ActivityType.USER_REPORTED -> "Reported:"
    ActivityType.NEW_USER -> "New user:"
}
fun translateTime(time: String, isHindi: Boolean) = if (isHindi) {
    time.replace("mins ago", "मिनट पहले").replace("hour ago", "घंटा पहले")
} else time