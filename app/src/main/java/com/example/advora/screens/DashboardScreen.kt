package com.example.advora.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.example.advora.viewmodel.LanguageViewModel

// ---------------------- DATA MODEL ----------------------

data class Ad(
    val title: String,
    val location: String,
    val price: String,
    val category: String,
    val imageUrl: String
)

// ---------------------- MAIN SCREEN ----------------------

@Composable
fun DashboardScreen(
    languageViewModel: LanguageViewModel,
    onNavigate: (String) -> Unit
) {

    val isHindi = languageViewModel.isHindi

    var selectedCategory by remember {
        mutableStateOf(if (isHindi) "सभी" else "All")
    }

    val ads = listOf(
        Ad("Sales Executive Needed", "Indore, MP", "₹25,000 - ₹35,000/month", "Jobs",
            "https://images.unsplash.com/photo-1492724441997-5dc865305da7"),

        Ad("2BHK Flat for Rent", "Vijay Nagar, Indore", "₹12,000/month", "Rentals",
            "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2"),

        Ad("iPhone 13 - Like New", "Delhi", "₹45,000", "Buy/Sell",
            "https://images.unsplash.com/photo-1632661674596-df8be070a5c5"),

        Ad("Home Cleaning Service", "Mumbai", "₹999/service", "Service",
            "https://images.unsplash.com/photo-1581578731548-c64695cc6952"),

        Ad("Laptop Developer Setup", "Bangalore", "₹75,000", "Buy/Sell",
            "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"),

        Ad("Office Workspace Hiring", "Pune", "₹30,000/month", "Jobs",
            "https://images.unsplash.com/photo-1497215728101-856f4ea42174")
    )

    val filteredAds = ads.filter {
        selectedCategory == "All" || selectedCategory == "सभी" ||
                it.category == selectedCategory
    }

    Scaffold(
        bottomBar = { BottomNavBar(onNavigate, isHindi) },
        containerColor = Color(0xFFF2F2F2)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            TopBar(
                isHindi = isHindi,
                onToggleLang = { languageViewModel.toggleLanguage() }
            )

            SearchSection(isHindi)

            CategoryChips(
                isHindi = isHindi,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            filteredAds.forEach {
                AdCard(it, isHindi)
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ---------------------- TOP BAR ----------------------

@Composable
fun TopBar(
    isHindi: Boolean,
    onToggleLang: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Menu, contentDescription = "", tint = Color(0xFFD08C60))
            Spacer(Modifier.width(8.dp))
            Text(
                if (isHindi) "एडवोरा" else "Advora",
                color = Color(0xFFD08C60),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF3A2A20))
                    .clickable { onToggleLang() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(if (isHindi) "हिं" else "EN", color = Color(0xFFD08C60))
            }

            Spacer(Modifier.width(14.dp))

            Icon(
                Icons.Default.NotificationsNone,
                contentDescription = "",
                tint = Color(0xFFD08C60)
            )

            Spacer(Modifier.width(14.dp))

            Icon(
                Icons.Default.Person,
                contentDescription = "",
                tint = Color(0xFFD08C60)
            )
        }
    }
}

// ---------------------- SEARCH ----------------------

@Composable
fun SearchSection(isHindi: Boolean) {

    var search by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = search,
            onValueChange = { search = it },
            placeholder = {
                Text(if (isHindi) "विज्ञापन खोजें..." else "Search ads...")
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "")
            },
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(55.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Tune, contentDescription = "")
        }
    }
}

// ---------------------- CATEGORY ----------------------

@Composable
fun CategoryChips(
    isHindi: Boolean,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {

    val categories = if (isHindi)
        listOf("सभी", "नौकरी", "किराया", "खरीदें", "सेवा")
    else
        listOf("All", "Jobs", "Rentals", "Buy/Sell", "Service")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F2))
            .padding(vertical = 10.dp)
    ) {

        items(categories) { item ->

            val isSelected = item == selectedCategory

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) Color.White else Color.Black)
                    .clickable { onCategorySelected(item) }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text = item,
                    color = if (isSelected) Color(0xFF3F51B5) else Color(0xFFD08C60),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ---------------------- CARD ----------------------

@Composable
fun AdCard(ad: Ad, isHindi: Boolean) {

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            Box {

                AsyncImage(
                    model = ad.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color(0xFF1DB954), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(if (isHindi) "सक्रिय" else "Active", color = Color.White, fontSize = 12.sp)
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(ad.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "")
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(ad.location, color = Color.Gray)

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(ad.price, color = Color(0xFFD08C60), fontSize = 16.sp)

                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(ad.category)
                    }
                }
            }
        }
    }
}

// ---------------------- BOTTOM NAV ----------------------

@Composable
fun BottomNavBar(
    onNavigate: (String) -> Unit,
    isHindi: Boolean
) {

    Box {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            NavItem(if (isHindi) "होम" else "Home", Icons.Default.Home, true) {
                onNavigate("home")
            }

            NavItem(if (isHindi) "मेरे विज्ञापन" else "My Ads", Icons.AutoMirrored.Filled.List, false) {
                onNavigate("ads")
            }

            Spacer(modifier = Modifier.width(60.dp))

            NavItem(if (isHindi) "मैप" else "Map", Icons.Default.LocationOn, false) {}

            NavItem(if (isHindi) "सेव्ड" else "Saved", Icons.Default.FavoriteBorder, false) {
                onNavigate("saved")
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
                .clickable { onNavigate("post") },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFD08C60), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "", tint = Color.Black)
            }

            Text(
                if (isHindi) "पोस्ट" else "Post Ad",
                color = Color(0xFFD08C60),
                fontSize = 12.sp
            )
        }
    }
}

// ---------------------- NAV ITEM ----------------------

@Composable
fun NavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {

        Icon(
            icon,
            contentDescription = "",
            tint = if (selected) Color(0xFFD08C60) else Color.Gray
        )

        Text(
            label,
            color = if (selected) Color(0xFFD08C60) else Color.Gray,
            fontSize = 12.sp
        )
    }
}