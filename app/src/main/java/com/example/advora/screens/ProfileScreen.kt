package com.example.advora.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel

enum class ProfileState { MAIN, EDIT_PROFILE, PRIVACY, NOTIFICATIONS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    adViewModel: AdViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    var currentState by remember { mutableStateOf(ProfileState.MAIN) }
    val isDarkMode = adViewModel.isDarkMode
    val isHindi = languageViewModel.isHindi

    // User State
    var userName by remember { mutableStateOf("Rajesh Kumar") }
    var userEmail by remember { mutableStateOf("rajesh@example.com") }
    var userPhone by remember { mutableStateOf("+91 98765 43210") }
    var userAddress by remember { mutableStateOf("Ujjain, Madhya Pradesh") } // Added Address State
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) profileImageUri = uri
    }

    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFDF7F2)
    val accentColor = Color(0xFFD08C60)
    val textColor = if (isDarkMode) Color.White else Color.Black

    BackHandler(enabled = currentState != ProfileState.MAIN) { currentState = ProfileState.MAIN }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentState) {
                            ProfileState.MAIN -> if (isHindi) "प्रोफ़ाइल" else "Profile"
                            ProfileState.EDIT_PROFILE -> if (isHindi) "संपादित करें" else "Edit Profile"
                            ProfileState.PRIVACY -> if (isHindi) "गोपनीयता" else "Privacy Policy"
                            ProfileState.NOTIFICATIONS -> if (isHindi) "सूचनाएं" else "Notifications"
                        },
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentState == ProfileState.MAIN) onBack() else currentState = ProfileState.MAIN
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = accentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = bgColor
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentState) {
                ProfileState.MAIN -> MainView(
                    isDark = isDarkMode, isHindi = isHindi,
                    name = userName, email = userEmail, address = userAddress, img = profileImageUri,
                    adVm = adViewModel, langVm = languageViewModel, textColor = textColor,
                    onEdit = { currentState = ProfileState.EDIT_PROFILE },
                    onPrivacy = { currentState = ProfileState.PRIVACY },
                    onNotif = { currentState = ProfileState.NOTIFICATIONS },
                    onNavigate = onNavigate, onLogout = onLogout
                )
                ProfileState.EDIT_PROFILE -> EditView(
                    isDark = isDarkMode, isHindi = isHindi, textColor = textColor,
                    name = userName, email = userEmail, phone = userPhone, address = userAddress, img = profileImageUri,
                    onImagePick = { launcher.launch("image/*") },
                    onOpenMap = { onNavigate("map") }, // Navigates to your separate map screen
                    onSave = { n, e, p, a ->
                        userName = n; userEmail = e; userPhone = p; userAddress = a
                        currentState = ProfileState.MAIN
                    }
                )
                ProfileState.NOTIFICATIONS -> NotificationsView(isDarkMode, textColor)
                ProfileState.PRIVACY -> PrivacyView(isDarkMode, textColor)
            }
        }
    }
}

@Composable
fun MainView(
    isDark: Boolean, isHindi: Boolean, name: String, email: String, address: String, img: Uri?,
    adVm: AdViewModel, langVm: LanguageViewModel, textColor: Color,
    onEdit: () -> Unit, onPrivacy: () -> Unit, onNotif: () -> Unit,
    onNavigate: (String) -> Unit, onLogout: () -> Unit
) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(Modifier.size(110.dp), shape = CircleShape, border = BorderStroke(1.dp, Color.LightGray)) {
                        if (img != null) AsyncImage(img, null, contentScale = ContentScale.Crop, modifier = Modifier.clip(CircleShape))
                        else Icon(Icons.Default.Person, null, Modifier.padding(25.dp), tint = Color.Gray)
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(34.dp).background(Color(0xFFD08C60), CircleShape)) {
                        Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(email, color = Color.Gray)

                // Address display in main card
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Icon(Icons.Default.LocationOn, null, tint = Color(0xFFD08C60), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(address, color = Color.Gray, fontSize = 14.sp)
                }
            }
        }

        MenuSection(isDark) {
            MenuItem(if (isHindi) "मेरे विज्ञापन" else "My Ads", Icons.Default.Inventory, isDark, textColor, { onNavigate("ads") })
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.2f))
            MenuItem(if (isHindi) "सूचनाएं" else "Notifications", Icons.Default.Notifications, isDark, textColor, onNotif)
        }

        Spacer(Modifier.height(16.dp))

        MenuSection(isDark) {
            MenuItem(if (isHindi) "गोपनीयता" else "Privacy", Icons.Default.Security, isDark, textColor, onPrivacy)
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.2f))

            Row(Modifier.fillMaxWidth().clickable { langVm.toggleLanguage() }.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, null, tint = Color(0xFFD08C60))
                Text(if (isHindi) "भाषा" else "Language", Modifier.weight(1f).padding(start = 16.dp), color = textColor)
                Text(if (isHindi) "HI" else "EN", color = Color(0xFFD08C60), fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(0.2f))

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DarkMode, null, tint = Color(0xFFD08C60))
                Text(if (isHindi) "डार्क मोड" else "Dark Mode", Modifier.weight(1f).padding(start = 16.dp), color = textColor)
                Switch(checked = isDark, onCheckedChange = { adVm.toggleDarkMode() }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD08C60)))
            }
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isHindi) "लॉगआउट" else "Logout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun EditView(
    isDark: Boolean, isHindi: Boolean, textColor: Color,
    name: String, email: String, phone: String, address: String, img: Uri?,
    onImagePick: () -> Unit, onOpenMap: () -> Unit, onSave: (String, String, String, String) -> Unit
) {
    var n by remember { mutableStateOf(name) }
    var e by remember { mutableStateOf(email) }
    var p by remember { mutableStateOf(phone) }
    var a by remember { mutableStateOf(address) }
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(vertical = 24.dp).clickable { onImagePick() }) {
            Surface(Modifier.size(130.dp), shape = CircleShape, border = BorderStroke(2.dp, Color(0xFFD08C60))) {
                AsyncImage(img, null, contentScale = ContentScale.Crop, modifier = Modifier.clip(CircleShape))
            }
            Box(Modifier.size(36.dp).background(Color(0xFFD08C60), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.PhotoCamera, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                EditField(n, { n = it }, if (isHindi) "पूरा नाम" else "Full Name", Icons.Default.Person, isDark, textColor)
                Spacer(Modifier.height(12.dp))
                EditField(e, { e = it }, if (isHindi) "ईमेल पता" else "Email Address", Icons.Default.Email, isDark, textColor)
                Spacer(Modifier.height(12.dp))
                EditField(p, { p = it }, if (isHindi) "फ़ोन नंबर" else "Phone Number", Icons.Default.Phone, isDark, textColor)
                Spacer(Modifier.height(12.dp))

                // Address/Location Field
                EditField(a, { a = it }, if (isHindi) "स्थान" else "Location/Address", Icons.Default.LocationOn, isDark, textColor)

                // Map Navigation Button
                TextButton(
                    onClick = onOpenMap,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                ) {
                    Icon(Icons.Default.Map, null, tint = Color(0xFFD08C60), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(if (isHindi) "मानचित्र पर चुनें" else "Pick on Map", color = Color(0xFFD08C60))
                }
            }
        }

        Spacer(Modifier.height(40.dp))
        Button(
            onClick = { onSave(n, e, p, a) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08C60)),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(if (isHindi) "सहेजें" else "Save Profile", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}

// ... Rest of the components (PrivacyView, NotificationsView, EditField, MenuSection, MenuItem) remain exactly as in your provided code ...
// Ensure you include the helper functions below if they were missing from your file.

@Composable
fun EditField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, isDark: Boolean, textColor: Color) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFFD08C60)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedBorderColor = Color(0xFFD08C60),
            unfocusedBorderColor = Color.LightGray.copy(0.4f),
            focusedLabelColor = Color(0xFFD08C60)
        )
    )
}

@Composable
fun MenuSection(isDark: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = if(isDark) Color(0xFF1E1E1E) else Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column { content() }
    }
}

@Composable
fun MenuItem(title: String, icon: ImageVector, isDark: Boolean, textColor: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color(0xFFD08C60), modifier = Modifier.size(22.dp))
        Text(title, Modifier.weight(1f).padding(start = 16.dp), fontSize = 16.sp, color = textColor)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)
    }
}

@Composable
fun PrivacyView(isDark: Boolean, textColor: Color) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Privacy Policy", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD08C60))
        Spacer(Modifier.height(20.dp))
        PrivacySection("Data Collection", "We collect minimal data to ensure your ads reach the right audience.", isDark, textColor)
        PrivacySection("Security", "Your account is protected by high-level encryption.", isDark, textColor)
    }
}

@Composable
fun PrivacySection(title: String, desc: String, isDark: Boolean, textColor: Color) {
    Card(Modifier.fillMaxWidth().padding(vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = if(isDark) Color(0xFF1E1E1E) else Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFFD08C60))
            Text(desc, fontSize = 14.sp, color = textColor.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun NotificationsView(isDark: Boolean, textColor: Color) {
    val list = listOf("Your ad is live!", "New message received.", "Welcome to Advora!")
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(list) { item ->
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp), colors = CardDefaults.cardColors(containerColor = if(isDark) Color(0xFF1E1E1E) else Color.White)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, null, tint = Color(0xFFD08C60))
                    Text(item, Modifier.padding(start = 16.dp), color = textColor)
                }
            }
        }
    }
}