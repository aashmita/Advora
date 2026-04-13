package com.example.advora.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.advora.viewmodel.LanguageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val isHindi = languageViewModel.isHindi
    val accentColor = Color(0xFFD08C60)

    // Theme and Language State
    var isDarkMode by remember { mutableStateOf(false) }

    // UI Colors based on Dark Mode toggle
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val subTextColor = if (isDarkMode) Color.LightGray else Color.Gray

    // Profile and Dialog State
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditMode by remember { mutableStateOf(false) }
    var adminName by remember { mutableStateOf("Admin User") }
    var adminEmail by remember { mutableStateOf("admin@advora.com") }
    var activeDialog by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        profileImageUri = it
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if(isHindi) "प्रोफ़ाइल" else "Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        containerColor = bgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image Section (Centered)
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.15f)
                ) {
                    if (profileImageUri != null) {
                        AsyncImage(
                            model = profileImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Person, null, modifier = Modifier.padding(25.dp), tint = accentColor)
                    }
                }
                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.size(32.dp).background(accentColor, CircleShape).border(2.dp, cardColor, CircleShape)
                ) {
                    Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // Enhanced Edit Panel
            if (isEditMode) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if(isHindi) "विवरण संपादित करें" else "Edit Details", fontWeight = FontWeight.Bold, color = textColor)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = adminName,
                            onValueChange = { adminName = it },
                            label = { Text("Name", color = subTextColor) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                cursorColor = accentColor
                            )                        )
                        Spacer(Modifier.height(10.dp))
                        OutlinedTextField(
                            value = adminEmail,
                            onValueChange = { adminEmail = it },
                            label = { Text("Email", color = subTextColor) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                cursorColor = accentColor
                            )                        )
                        Button(
                            onClick = { isEditMode = false },
                            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if(isHindi) "सहेजें" else "Save Changes")
                        }
                    }
                }
            } else {
                Text(adminName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(adminEmail, fontSize = 14.sp, color = subTextColor)
                TextButton(onClick = { isEditMode = true }) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp), tint = accentColor)
                    Spacer(Modifier.width(4.dp))
                    Text(if(isHindi) "संपादन" else "Edit Profile", color = accentColor)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Stats Section
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AdminStatCard(if(isHindi) "क्रियाएं" else "Actions Today", "156", accentColor, cardColor, textColor, Modifier.weight(1f))
                AdminStatCard(if(isHindi) "लंबित" else "Pending Tasks", "23", accentColor, cardColor, textColor, Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // Settings & Toggles List
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column {
                    // Working Dark Mode Toggle
                    ToggleItem(
                        if(isHindi) "डार्क थीम" else "Dark Theme",
                        Icons.Default.Brightness4,
                        isDarkMode,
                        textColor
                    ) { isDarkMode = it }

                    // Language Toggle
                    ToggleItem(
                        if(isHindi) "English में बदलें" else "Switch to Hindi",
                        Icons.Default.Language,
                        isHindi,
                        textColor
                    ) { languageViewModel.toggleLanguage() }

                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp, color = subTextColor.copy(alpha = 0.2f))

                    // Menu Actions
                    ActionItem(if(isHindi) "पासवर्ड बदलें" else "Change Password", Icons.Default.Lock, accentColor, textColor) { activeDialog = "password" }
                    ActionItem(if(isHindi) "सूचनाएं" else "Notifications", Icons.Default.Notifications, accentColor, textColor) { activeDialog = "notifications" }
                    ActionItem(if(isHindi) "सहायता" else "Help & Support", Icons.Default.HelpCenter, accentColor, textColor) { activeDialog = "help" }
                }
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text(if(isHindi) "लॉगआउट" else "Logout", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Static Dialog Implementation
    if (activeDialog != null) {
        AlertDialog(
            onDismissRequest = { activeDialog = null },
            confirmButton = { TextButton(onClick = { activeDialog = null }) { Text("Close", color = accentColor) } },
            containerColor = cardColor,
            titleContentColor = textColor,
            textContentColor = subTextColor,
            title = { Text(activeDialog?.replaceFirstChar { it.uppercase() } ?: "") },
            text = {
                Text(when(activeDialog) {
                    "password" -> "Security: A password reset link has been sent to your registered email $adminEmail."
                    "notifications" -> "System: You have 3 pending moderation requests and 2 server alerts."
                    "help" -> "Support: Contact our admin desk at admin-support@advora.com for technical help."
                    else -> ""
                })
            }
        )
    }
}

@Composable
fun AdminStatCard(label: String, value: String, accent: Color, bg: Color, txt: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bg,
        shadowElevation = 1.dp
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = accent)
            Text(label, fontSize = 12.sp, color = txt.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun ToggleItem(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    txtColor: Color,
    onToggle: (Boolean) -> Unit
) {
    // accentColor (0xFFD08C60) is passed or used here for the icon tint
    val accentColor = Color(0xFFD08C60)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Updated tint from Color.Gray to accentColor
        Icon(
            icon,
            null,
            tint = accentColor,
            modifier = Modifier.size(22.dp)
        )
        Text(
            title,
            Modifier.weight(1f).padding(start = 16.dp),
            fontSize = 15.sp,
            color = txtColor,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = accentColor, // The track also uses the theme brown
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray.copy(0.4f)
            )
        )
    }
}

@Composable
fun ActionItem(title: String, icon: ImageVector, accent: Color, txtColor: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp))
        Text(title, Modifier.weight(1f).padding(start = 16.dp), fontSize = 15.sp, fontWeight = FontWeight.Medium, color = txtColor)
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}