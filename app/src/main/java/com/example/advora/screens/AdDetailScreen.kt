package com.example.advora.screens

import android.content.Intent
import com.example.advora.screens.registeredUsers
import com.example.advora.screens.UserItem
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdItem
import com.example.advora.viewmodel.AdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdDetailScreen(
    ad: AdItem,
    onBack: () -> Unit,
    adViewModel: AdViewModel
) {
    val context = LocalContext.current
    val isSaved = adViewModel.isSaved(ad)
    val brandColor = Color(0xFFD08C60)
    val pinkishBg = Color(0xFFF3EBF0)

    // ✅ SAFER SEARCH: Find the actual seller from the global registeredUsers list
    // Note: If 'registeredUsers' is red, Alt+Enter to import it.
    val seller = remember(ad.ownerEmail) {
        registeredUsers.find { it.email.equals(ad.ownerEmail, ignoreCase = true) }
    }

    // Resolve display details
    val displayName = seller?.name ?: ad.ownerName
    val displayPhone = seller?.phone ?: ad.ownerPhone
    val displayEmail = seller?.email ?: ad.ownerEmail

    var showProfileSheet by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showSuccessReport by remember { mutableStateOf(false) }
    var reportMessage by remember { mutableStateOf("") }

    // Helper function to mask the number
    fun getMaskedNumber(phone: String): String = if (phone.length >= 5) phone.take(6) + "XXXX" else phone

    if (showReportDialog) {
        Dialog(onDismissRequest = { showReportDialog = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = pinkishBg,
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Report Ad", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1D1B20))
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = reportMessage,
                        onValueChange = { reportMessage = it },
                        placeholder = { Text("Why are you reporting this ad?", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = brandColor,
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                    Spacer(Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showReportDialog = false }) {
                            Text("Cancel", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (reportMessage.isNotBlank()) {
                                    adViewModel.reportAd(ad, reportMessage)
                                    showReportDialog = false
                                    showSuccessReport = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = brandColor)
                        ) {
                            Text("Submit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showSuccessReport) {
        Dialog(onDismissRequest = { showSuccessReport = false }) {
            Surface(shape = RoundedCornerShape(28.dp), color = pinkishBg) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("Report Sent", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { showSuccessReport = false; reportMessage = "" }, colors = ButtonDefaults.buttonColors(brandColor)) {
                        Text("OK")
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            Surface(tonalElevation = 4.dp, color = Color.White) {
                Row(modifier = Modifier.navigationBarsPadding().padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Check out this ${ad.title} on Advora!")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        },
                        modifier = Modifier.size(50.dp).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.Share, null, tint = brandColor)
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$displayPhone"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = brandColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Contact Seller", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState()).padding(innerPadding)) {
            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                AsyncImage(model = ad.imageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Row(modifier = Modifier.statusBarsPadding().fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    FilledIconButton(onClick = onBack, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.9f))) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
                    }
                    Row {
                        FilledIconButton(onClick = { showReportDialog = true }, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.9f))) {
                            Icon(Icons.Default.Report, null, tint = Color.Red)
                        }
                        Spacer(Modifier.width(8.dp))
                        FilledIconButton(onClick = { adViewModel.toggleSave(ad) }, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.9f))) {
                            Icon(if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (isSaved) Color.Red else Color.Black)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(ad.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = brandColor)
                Spacer(Modifier.height(8.dp))
                Text(ad.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Text(" ${ad.location} • Posted ${ad.postedDate}", color = Color.Gray, fontSize = 13.sp)
                }
                HorizontalDivider(Modifier.padding(vertical = 20.dp), thickness = 0.5.dp)
                Text("Description", fontWeight = FontWeight.Bold)
                Text(text = ad.description.ifEmpty { "No description provided." }, color = Color.DarkGray)
                Spacer(Modifier.height(24.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { showProfileSheet = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(44.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
                            Text(displayName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(displayName, fontWeight = FontWeight.Bold)
                            Text("Verified Seller", color = Color(0xFF2E7D32), fontSize = 11.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)
                    }
                }
            }
        }
    }

    if (showProfileSheet) {
        ModalBottomSheet(onDismissRequest = { showProfileSheet = false }, containerColor = Color.White) {
            SellerDetailedProfile(displayName, displayPhone, displayEmail, ad.location, ad.ownerAddress, brandColor)
        }
    }
}

@Composable
fun SellerDetailedProfile(name: String, phone: String, email: String, location: String, address: String, brandColor: Color) {
    val context = LocalContext.current
    val maskedPhone = if(phone.length > 5) phone.take(6) + "XXXX" else phone

    Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(64.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
            Text(name.take(1), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Seller in $location", color = Color.Gray)
        HorizontalDivider(Modifier.padding(vertical = 20.dp))
        ProfileRow(Icons.Default.Phone, "Mobile", maskedPhone)
        ProfileRow(Icons.Default.Email, "Email", email)
        ProfileRow(Icons.Default.Home, "Location", address)
        Spacer(Modifier.height(30.dp))
        Button(onClick = { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))) }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(brandColor)) {
            Text("Call Now")
        }
    }
}

@Composable
fun ProfileRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}