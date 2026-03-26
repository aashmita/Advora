package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.advora.utils.LocalLanguage
import com.example.advora.utils.getText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    titleHindi: String,                // ✅ NEW
    description: String,
    descriptionHindi: String,          // ✅ NEW
    price: String,
    location: String,
    imageUrl: String?,
    onBack: () -> Unit
) {

    val isHindi = LocalLanguage.current

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {

            // 🔥 IMAGE SECTION
            Box {

                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )

                // 🔙 BACK
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }

                // ❤️ FAVORITE
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Default.FavoriteBorder, null)
                }
            }

            // 🔽 CONTENT
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                // 💰 PRICE
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = getText("Active", "सक्रिय"),
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF16A34A))
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🏷 TITLE (DYNAMIC)
                Text(
                    text = if (isHindi) titleHindi else title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 📍 LOCATION
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 📄 DESCRIPTION TITLE
                Text(
                    getText("Description", "विवरण"),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 📄 DESCRIPTION (DYNAMIC)
                Text(
                    text = if (isHindi) descriptionHindi else description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 👤 SELLER CARD
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1F3C88)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title.take(2).uppercase(),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = getText("Seller", "विक्रेता"),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = getText("Active user", "सक्रिय उपयोगकर्ता"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // 🔥 BOTTOM BUTTONS
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedButton(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, null)
                Spacer(Modifier.width(6.dp))
                Text(getText("Share", "शेयर करें"))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Call, null)
                Spacer(Modifier.width(6.dp))
                Text(getText("Call Now", "कॉल करें"))
            }
        }
    }
}