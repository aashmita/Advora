package com.example.advora.screens
import com.example.advora.components.admin.AdminDrawer
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Shield
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.PlainTooltip
import kotlinx.coroutines.launch
// --- DATA MODEL ---
data class UserData(
    val name: String,
    val email: String,
    val phone: String,
    val totalAds: String,
    val joinedDate: String,
    val status: String,
    val initial: String
)

@Composable
fun UserManagementScreen(
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    // --- LANGUAGE STATE ---
    var currentLang by remember { mutableStateOf("EN") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    var showDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    // Translation Logic
    val titleText = if (currentLang == "EN") "User Management" else "उपयोगकर्ता प्रबंधन"
    val adsLabel = if (currentLang == "EN") "Total Ads" else "कुल विज्ञापन"
    val joinedLabel = if (currentLang == "EN") "Joined" else "शामिल हुए"
    val activateBtn = if (currentLang == "EN") "Activate" else "सक्रिय करें"
    val blockBtn = if (currentLang == "EN") "Block" else "ब्लॉक करें"


    val primaryOrange = Color(0xFFC67C4E)
    val backgroundGrey = Color(0xFF9E9E9E)

    var users by remember {
        mutableStateOf(
            listOf(
                UserData(
                    "Rajesh Kumar",
                    "rajesh@example.com",
                    "+91 98765 43210",
                    "12",
                    "2024-01-15",
                    "active",
                    "R"
                ),
                UserData(
                    "Priya Sharma",
                    "priya@example.com",
                    "+91 98123 45678",
                    "8",
                    "2024-02-20",
                    "active",
                    "P"
                ),
                UserData(
                    "Amit Patel",
                    "amit@example.com",
                    "+91 97654 32109",
                    "5",
                    "2023-12-10",
                    "blocked",
                    "A"
                )
            )
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AdminDrawer(
                    current = "user_management",
                    onNavigate = {
                        scope.launch { drawerState.close() }
                        onNavigate(it)
                    }
                )
            }
        }
    ) {
        Column {
            // --- TOP BAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = primaryOrange,
                    modifier = Modifier.clickable {
                        scope.launch { drawerState.open() }
                    }
                )
                Spacer(Modifier.weight(1f))

                Text(
                    titleText,
                    color = primaryOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                // --- WORKING LANG SWITCH ---
                Box(

                    modifier = Modifier
                        .height(32.dp)
                        .clip(RoundedCornerShape(50)) // 🔥 pill shape
                        .background(Color(0xFF2C2C2C))
                        .border(1.dp, Color(0xFF6B6B6B), RoundedCornerShape(50))
                        .clickable { currentLang = if (currentLang == "EN") "HI" else "EN" }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentLang,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.width(12.dp))
                Icon(Icons.Outlined.Notifications, null, tint = primaryOrange)
                Spacer(Modifier.width(12.dp))
                AsyncImage(
                    model = "https://i.pravatar.cc/150?u=9",
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // --- USER LIST ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(users) { index, user ->
                    UserCard(
                        user = user,
                        accentColor = primaryOrange,
                        adsText = adsLabel,
                        joinedText = joinedLabel,
                        activateText = activateBtn,
                        blockText = blockBtn,
                        onNavigate = onNavigate,

                        onActivate = {
                            users = users.toMutableList().also {
                                it[index] = it[index].copy(status = "active")
                            }
                        },

                        onBlock = {
                            selectedIndex = index
                            showDialog = true
                        }
                    )
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Block") },
                    text = { Text("Are you sure you want to block this user?") },

                    confirmButton = {
                        Button(onClick = {
                            users = users.toMutableList().also {
                                it[selectedIndex] =
                                    it[selectedIndex].copy(status = "blocked")
                            }
                            showDialog = false
                        }) {
                            Text("Yes")
                        }
                    },

                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // --- BOTTOM BAR ---

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun UserCard(
    user: UserData,
    accentColor: Color,
    adsText: String,
    joinedText: String,
    activateText: String,
    blockText: String,
    onActivate: () -> Unit,
    onBlock: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 🔹 TOP SECTION
            Row(verticalAlignment = Alignment.Top) {

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        user.initial,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(user.email, color = Color.Gray, fontSize = 13.sp)
                    Text(user.phone, color = Color.Gray, fontSize = 13.sp)
                }

                val badgeColor =
                    if (user.status == "active") Color(0xFF00C853) else Color(0xFFFF5252)

                Surface(
                    color = badgeColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        user.status.uppercase(),
                        color = Color.White,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // 🔹 STATS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatBox(
                    modifier = Modifier.weight(1f),
                    value = user.totalAds,
                    label = adsText
                )
                StatBox(
                    modifier = Modifier.weight(1f),
                    value = user.joinedDate,
                    label = joinedText
                )
            }

            Spacer(Modifier.height(16.dp))

            // 🔹 ACTION BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ✅ ACTIVATE
                ActionButton(
                    modifier = Modifier.weight(1f),
                    text = activateText,
                    color = Color(0xFF1AB65C),
                    icon = Icons.Default.PersonOutline,
                    onClick = onActivate
                )

                // ❌ BLOCK
                ActionButton(
                    modifier = Modifier.weight(1f),
                    text = blockText,
                    color = Color(0xFFF05151),
                    icon = Icons.Default.PersonRemoveAlt1,
                    onClick = onBlock
                )

                // 🛡️ SHIELD (DETAILS)
                val tooltipState = rememberTooltipState()

                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("View Details")
                        }
                    },
                    state = tooltipState
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF333333))
                            .clickable {
                                onNavigate("user_detail")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Shield,
                            contentDescription = "View Details",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(modifier: Modifier, value: String, label: String) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF333333))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
fun ActionButton(
    modifier: Modifier,
    text: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
)  {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

// --- BOTTOM BAR ---

@Composable
fun NavItem(label: String, icon: ImageVector, accentColor: Color, isSelected: Boolean = false, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) accentColor else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(label, color = if (isSelected) accentColor else Color.White, fontSize = 11.sp)
    }
}