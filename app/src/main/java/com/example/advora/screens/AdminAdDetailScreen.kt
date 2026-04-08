package com.example.advora.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAdDetailScreen(
    adId: String?,
    adViewModel: AdViewModel,
    onBack: () -> Unit
) {
    val ad = adViewModel.ads.find { it.id == adId } ?: return
    val brandColor = Color(0xFFD08C60)
    val context = LocalContext.current

    // State to show seller profile popup
    var showSellerProfile by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                color = Color.White,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // SHARE BUTTON
                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out this ad: ${ad.title}\nPrice: ${ad.price}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, brandColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = brandColor)
                    ) {
                        Icon(Icons.Default.Share, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Share", fontWeight = FontWeight.Bold)
                    }

                    // VIEW PROFILE BUTTON
                    Button(
                        onClick = { showSellerProfile = true },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = brandColor)
                    ) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Seller Profile", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                    AsyncImage(
                        model = ad.imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(12.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(ad.price, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = brandColor)
                        Spacer(Modifier.height(8.dp))
                        Text(ad.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Text(" ${ad.location}", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(ad.description, color = Color.DarkGray, fontSize = 14.sp)
                }
            }
        }
    }

    // --- SELLER PROFILE DIALOG ---
    if (showSellerProfile) {
        AlertDialog(
            onDismissRequest = { showSellerProfile = false },
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${ad.ownerPhone}")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Call, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Call Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSellerProfile = false }) {
                    Text("Close", color = Color.Gray)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).background(brandColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(ad.ownerName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Seller Profile", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminDetailRow(Icons.Default.Person, "Name", ad.ownerName)
                    AdminDetailRow(Icons.Default.Phone, "Phone", ad.ownerPhone)
                    AdminDetailRow(Icons.Default.Email, "Email", ad.ownerEmail)
                    AdminDetailRow(Icons.Default.Home, "Address", ad.ownerAddress)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun AdminDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}