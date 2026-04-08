package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    languageViewModel: LanguageViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)
    val bgColor = Color(0xFFF5F5F7)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    onLogout = onLogout,
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
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdminStatCard(if (isHindi) "कुल विज्ञापन" else "Total Ads", "1.2k", Icons.Default.Description, accentColor, Modifier.weight(1f))
                        AdminStatCard(if (isHindi) "कुल यूज़र" else "Total Users", "856", Icons.Default.Group, Color(0xFF4CAF50), Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdminStatCard(if (isHindi) "रिपोर्ट्स" else "Reports", "23", Icons.Default.Warning, Color(0xFFE53935), Modifier.weight(1f))
                        AdminStatCard(if (isHindi) "सक्रिय" else "Active", "892", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFF2196F3), Modifier.weight(1f))
                    }
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
                        HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.3f))
                        // Standardized route to "user_management"
                        AdminActionItem(if (isHindi) "यूज़र प्रबंधन" else "User Management", Icons.Default.PersonSearch, Color.Black) { onNavigate("user_management") }
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
                    Column(Modifier.padding(20.dp)) {
                        AdminActivityRow("New ad posted: iPhone 14 Pro Max", "5 mins ago")
                        AdminActivityRow("User 'JohnDoe' reported for spam", "15 mins ago")
                        AdminActivityRow("New user registration: Sarah Smith", "1 hour ago")
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun AdminDrawerContent(
    isHindi: Boolean,
    accentColor: Color,
    currentRoute: String,
    onLogout: () -> Unit,
    onItemClick: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.Black,
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        modifier = Modifier.width(300.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = if (isHindi) "एडवोरा" else "Advora",
                color = accentColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)
            )

            DrawerMenuItem(if (isHindi) "डैशबोर्ड" else "Dashboard", Icons.Default.Home, currentRoute == "admin_dashboard") { onItemClick("admin_dashboard") }
            DrawerMenuItem(if (isHindi) "विज्ञापनों का प्रबंधन" else "Manage Ads", Icons.Default.FormatListBulleted, currentRoute == "manage_ads") { onItemClick("manage_ads") }

            // FIXED: Route changed from "users" to "user_management" to prevent blank screen
            DrawerMenuItem(if (isHindi) "यूज़र प्रबंधन" else "User Management", Icons.Default.Person, currentRoute == "user_management") { onItemClick("user_management") }

            DrawerMenuItem(if (isHindi) "रिपोर्ट्स" else "Reports", Icons.Default.Info, currentRoute == "reports") { onItemClick("reports") }
            DrawerMenuItem(if (isHindi) "प्रोफ़ाइल" else "Profile", Icons.Default.AccountCircle, currentRoute == "admin_profile") { onItemClick("admin_profile") }
            DrawerMenuItem(if (isHindi) "नक्शा" else "Map", Icons.Default.LocationOn, currentRoute == "map") { onItemClick("map") }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                onClick = onLogout,
                color = Color(0xFF421212),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
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
    Surface(
        onClick = onClick,
        color = if (isSelected) Color(0xFF2C1C12) else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(icon, null, tint = Color(0xFFD08C60), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = if (isSelected) Color.White else Color(0xFFD08C60).copy(0.7f), fontSize = 16.sp)
        }
    }
}

@Composable
fun AdminTopBar(isHindi: Boolean, onLogout: () -> Unit, onMenuClick: () -> Unit, onToggleLang: () -> Unit) {
    val accentColor = Color(0xFFD08C60)
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Black).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, "", tint = accentColor, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(8.dp))
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
            IconButton(onClick = onLogout) {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.White, modifier = Modifier.size(24.dp))
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
fun AdminActivityRow(text: String, time: String) {
    Row(Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.Top) {
        Box(Modifier.padding(top = 6.dp).size(6.dp).background(Color(0xFFD08C60), CircleShape))
        Column(Modifier.padding(start = 12.dp)) {
            Text(text, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            Text(time, fontSize = 12.sp, color = Color.Gray)
        }
    }
}