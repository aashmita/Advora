package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
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
                // ✅ FIX: Passing currentRoute to resolve build error
                currentRoute = "admin_profile",
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Admin Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            // Profile Placeholder using Emoji to avoid unresolved reference errors
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(accentColor.copy(alpha = 0.1f))
                                    .border(2.dp, accentColor, CircleShape)
                                    .clickable { /* Support Gallery Upload later */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👨‍💼", fontSize = 50.sp)
                            }

                            Surface(
                                color = accentColor,
                                shape = CircleShape,
                                modifier = Modifier.size(30.dp).offset(x = (-4).dp, y = (-4).dp)
                            ) {
                                Icon(Icons.Default.CameraAlt, "Upload", tint = Color.White, modifier = Modifier.padding(6.dp))
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Text("Admin User", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("admin@advora.com", fontSize = 14.sp, color = Color.Gray)

                        Spacer(Modifier.height(12.dp))
                        Surface(
                            color = accentColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Administrator",
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Admin Stats Section
                Text(
                    text = if (isHindi) "एडमिन आँकड़े" else "Admin Stats",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminProfileStat(if (isHindi) "आज की क्रियाएं" else "Actions Today", "156", Modifier.weight(1f))
                    AdminProfileStat(if (isHindi) "लंबित कार्य" else "Pending Tasks", "23", Modifier.weight(1f))
                }

                Spacer(Modifier.height(24.dp))

                // Settings Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        AdminProfileSettingItem(if (isHindi) "पासवर्ड बदलें" else "Change Password", Icons.Default.Lock)
                        HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.3f))
                        AdminProfileSettingItem(if (isHindi) "सूचनाएं" else "Notifications", Icons.Default.Notifications)
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Logout Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isHindi) "लॉगआउट" else "Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AdminProfileStat(label: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFD08C60))
            Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun AdminProfileSettingItem(title: String, icon: ImageVector) {
    Row(
        Modifier.fillMaxWidth().clickable { }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFFD08C60), modifier = Modifier.size(24.dp))
        Text(title, Modifier.weight(1f).padding(start = 16.dp), fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}