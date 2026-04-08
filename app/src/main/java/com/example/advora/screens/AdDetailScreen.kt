package com.example.advora.screens



import android.content.Intent

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

import androidx.compose.ui.unit.*

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

    var showProfileSheet by remember { mutableStateOf(false) }



// Helper function to mask the number for the Dialer

    fun getMaskedNumber(phone: String): String {

        return if (phone.length >= 5) phone.take(6) + "XXXX" else phone

    }



    Scaffold(

        bottomBar = {

            Surface(tonalElevation = 4.dp, shadowElevation = 12.dp, color = Color.White) {

                Row(

                    modifier = Modifier.navigationBarsPadding().padding(16.dp).fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    IconButton(

                        onClick = {

                            val intent = Intent(Intent.ACTION_SEND).apply {

                                type = "text/plain"

                                putExtra(Intent.EXTRA_TEXT, "Check out this ${ad.title} for ${ad.price} on Advora!")

                            }

                            context.startActivity(Intent.createChooser(intent, "Share via"))

                        },

                        modifier = Modifier.size(50.dp).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))

                    ) {

                        Icon(Icons.Default.Share, contentDescription = null, tint = brandColor)

                    }

                    Spacer(Modifier.width(12.dp))

                    Button(

                        onClick = {

// ✅ Updated to use Masked Number in the Dialer as requested

                            val maskedNum = getMaskedNumber(ad.ownerPhone)

                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$maskedNum"))

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

        Column(

            modifier = Modifier.fillMaxSize().background(Color.White)

                .verticalScroll(rememberScrollState())

                .padding(bottom = innerPadding.calculateBottomPadding())

        ) {

            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {

                AsyncImage(

                    model = ad.imageUri,

                    contentDescription = null,

                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Crop

                )

                Row(

                    modifier = Modifier.statusBarsPadding().fillMaxWidth().padding(12.dp),

                    horizontalArrangement = Arrangement.SpaceBetween

                ) {

                    FilledIconButton(onClick = onBack, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.9f))) {

                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)

                    }

                    FilledIconButton(onClick = { adViewModel.toggleSave(ad) }, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.9f))) {

                        Icon(if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (isSaved) Color.Red else Color.Black)

                    }

                }

            }



            Column(modifier = Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(ad.price, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = brandColor, modifier = Modifier.weight(1f))



// ✅ Dynamic Status Badge Color based on Ad status

                    val statusBg = when(ad.status.lowercase()) {

                        "active" -> Color(0xFFE8F5E9)

                        "pending" -> Color(0xFFFFF3E0)

                        else -> Color(0xFFFFEBEE)

                    }

                    val statusText = when(ad.status.lowercase()) {

                        "active" -> Color(0xFF2E7D32)

                        "pending" -> Color(0xFFEF6C00)

                        else -> Color(0xFFC62828)

                    }



                    Surface(color = statusBg, shape = RoundedCornerShape(6.dp)) {

                        Text(ad.status.uppercase(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = statusText, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                    }

                }



                Spacer(Modifier.height(8.dp))

                Text(ad.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)



                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))

                    Text(" ${ad.location} • Posted ${ad.postedDate}", color = Color.Gray, fontSize = 13.sp)

                }



                HorizontalDivider(Modifier.padding(vertical = 20.dp), thickness = 0.5.dp)



                Text("Description", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(6.dp))

// ✅ Shows the detailed 2-3 line description from the AdItem

                Text(

                    text = ad.description.ifEmpty { "Verified listing. Contact for more details." },

                    style = MaterialTheme.typography.bodyMedium,

                    color = Color.DarkGray,

                    lineHeight = 20.sp

                )



                Spacer(Modifier.height(24.dp))



// SELLER CARD

                Surface(

                    modifier = Modifier.fillMaxWidth().clickable { showProfileSheet = true },

                    shape = RoundedCornerShape(12.dp),

                    color = Color(0xFFF9FAFB),

                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))

                ) {

                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

                        Box(modifier = Modifier.size(44.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {

                            Text(ad.ownerName.take(1), color = Color.White, fontWeight = FontWeight.Bold)

                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(ad.ownerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                            Text("Verified Seller", color = Color(0xFF2E7D32), fontSize = 11.sp)

                        }

                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)

                    }

                }

            }

        }

    }



    if (showProfileSheet) {

        ModalBottomSheet(

            onDismissRequest = { showProfileSheet = false },

            containerColor = Color.White,

            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }

        ) {

            SellerDetailedProfile(ad, brandColor)

        }

    }

}



@Composable

fun SellerDetailedProfile(ad: AdItem, brandColor: Color) {

    val context = LocalContext.current

// Masking the phone and email for the Profile UI

    val maskedPhone = if(ad.ownerPhone.length > 5) ad.ownerPhone.take(6) + "XXXX" else ad.ownerPhone

    val maskedEmail = ad.ownerEmail.replaceBefore("@", "seller_xx")



    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier.size(64.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {

            Text(ad.ownerName.take(1), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        }

        Spacer(Modifier.height(12.dp))

        Text(ad.ownerName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Text("Seller in ${ad.location}", color = Color.Gray, fontSize = 14.sp)

        HorizontalDivider(Modifier.padding(vertical = 20.dp), thickness = 0.5.dp)



        ProfileRow(Icons.Default.Phone, "Mobile", maskedPhone)

        ProfileRow(Icons.Default.Email, "Email", maskedEmail)

        ProfileRow(Icons.Default.Home, "Location", ad.ownerAddress)



        Spacer(Modifier.height(30.dp))

        Button(

            onClick = {

// ✅ Use masked number in dialer from profile as well

                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$maskedPhone"))

                context.startActivity(dialIntent)

            },

            modifier = Modifier.fillMaxWidth().height(48.dp),

            colors = ButtonDefaults.buttonColors(brandColor),

            shape = RoundedCornerShape(12.dp)

        ) {

            Text("Call Now", fontWeight = FontWeight.Bold)

        }

        Spacer(Modifier.height(16.dp))

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