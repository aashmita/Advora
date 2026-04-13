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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.example.advora.viewmodel.LanguageViewModel
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.AdItem
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    languageViewModel: LanguageViewModel,
    adViewModel: AdViewModel,
    onNavigate: (String) -> Unit,
    onAdClick: (AdItem) -> Unit
) {
    val isHindi = languageViewModel.isHindi
    var showFilter by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var selectedCategory by remember { mutableStateOf("All") }
    var filterLocation by remember { mutableStateOf("") }
    var priceRange by remember { mutableStateOf(0f..100000f) }
    var filterSelectedCategory by remember { mutableStateOf("All") }

    // --- RESTORED ORIGINAL ADS + FIXED MACBOOK ---
    val restoredStaticAds = listOf(
        AdItem(UUID.randomUUID().toString(), "Sales Executive Needed", "₹25,000", "Ujjain, MP", "Jobs", "Seeking experienced sales professionals.", "https://images.unsplash.com/photo-1492724441997-5dc865305da7", "Advora Official", "+91 98765 XXXX", "seller@advora.com", "Ujjain, MP", "Active", "1 hr ago"),
        AdItem(UUID.randomUUID().toString(), "2BHK Flat near Mahakal", "₹10,000", "Ujjain, MP", "Rentals", "Spacious flat near Mahakal Temple.", "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2", "Advora Official", "+91 98765 XXXX", "seller@advora.com", "Ujjain, MP", "Active", "2 hrs ago"),
        AdItem(UUID.randomUUID().toString(), "Royal Enfield Classic 350", "₹1,20,000", "Ujjain, MP", "Vehicles", "Excellent condition Classic 350.", "https://images.unsplash.com/photo-1558981806-ec527fa84c39", "Advora Official", "+91 98765 XXXX", "seller@advora.com", "Ujjain, MP", "Active", "3 hrs ago"),
        AdItem(UUID.randomUUID().toString(), "iPhone 13 (Like New)", "₹42,000", "Ujjain, MP", "Buy/Sell", "Hardly used iPhone 13.", "https://images.unsplash.com/photo-1632661674596-df8be070a5c5", "Advora Official", "+91 98765 XXXX", "seller@advora.com", "Ujjain, MP", "Active", "4 hrs ago"),
        AdItem(UUID.randomUUID().toString(), "MacBook Air M1", "₹65,000", "Indore, MP", "Electronics", "Pristine condition MacBook Air.", "https://images.unsplash.com/photo-1517336714460-4c504b07fe27", "Advora Official", "+91 98765 XXXX", "seller@advora.com", "Indore, MP", "Expiring", "Just now"),
        AdItem(UUID.randomUUID().toString(), "Pandit for Mahakal Puja", "₹1,500", "Ujjain, MP", "Services", "Experienced Pandit available.", "https://images.unsplash.com/photo-1604608672516-7a3f7b2f1f4d", "Advora Official", "+91 98765 XXXX", "seller@advora.com", "Ujjain, MP", "Active", "5 hrs ago")
    )

    // Merge viewmodel ads with static list and filter by: Active/Expiring + HAS IMAGE
    val allVisibleAds = (adViewModel.ads + restoredStaticAds).filter { ad ->
        // ✅ FIXED: Allow both web URLs and local content Uris
        val hasProperImage = ad.imageUri.isNotEmpty()
        // ✅ FIXED: Case-insensitive status check to ensure approved ads appear
        val isCorrectStatus = ad.status.equals("Active", ignoreCase = true) ||
                ad.status.equals("Expiring", ignoreCase = true) || ad.status.equals("Approved", ignoreCase = true)
        hasProperImage && isCorrectStatus
    }

    val filteredAds = allVisibleAds.filter { ad ->
        val matchesSearch = ad.title.contains(searchQuery, ignoreCase = true)
        val matchesTopCategory = selectedCategory == "All" || ad.category == selectedCategory
        val matchesFilterCategory = filterSelectedCategory == "All" || ad.category == filterSelectedCategory
        val matchesLocation = filterLocation.isEmpty() || ad.location.contains(filterLocation, ignoreCase = true)
        val numericPrice = ad.price.replace(Regex("[^0-9]"), "").toFloatOrNull() ?: 0f
        val matchesPrice = numericPrice in priceRange

        matchesSearch && matchesTopCategory && matchesFilterCategory && matchesLocation && matchesPrice
    }

    Scaffold(
        bottomBar = { BottomNavBar(onNavigate, isHindi) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(isHindi, onNavigate, adViewModel) { languageViewModel.toggleLanguage() }
            SearchSection(isHindi, searchQuery, { searchQuery = it }, { showFilter = true })
            Spacer(modifier = Modifier.height(12.dp))
            CategoryChips(isHindi, selectedCategory) { selectedCategory = it }
            Spacer(modifier = Modifier.height(16.dp))

            if (filteredAds.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text(if (isHindi) "कोई विज्ञापन नहीं मिला" else "No ads found")
                }
            } else {
                filteredAds.forEach { adItem -> AdCard(adItem, isHindi, adViewModel, onAdClick) }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showFilter) {
        ModalBottomSheet(onDismissRequest = { showFilter = false }, containerColor = Color.White) {
            FilterSheetContent(
                isHindi, filterLocation, { filterLocation = it },
                priceRange, { priceRange = it },
                filterSelectedCategory, { filterSelectedCategory = it },
                onApply = { showFilter = false },
                onClear = { filterLocation = ""; filterSelectedCategory = "All"; priceRange = 0f..100000f }
            )
        }
    }
}

@Composable
fun AdCard(adItem: AdItem, isHindi: Boolean, adViewModel: AdViewModel, onAdClick: (AdItem) -> Unit) {
    // Translation Mapping
    val displayTitle = if (isHindi) {
        when (adItem.title) {
            "Sales Executive Needed" -> "सेल्स एग्जीक्यूटिव की आवश्यकता"
            "2BHK Flat near Mahakal" -> "महाकाल के पास 2BHK फ्लैट"
            "Royal Enfield Classic 350" -> "रॉयल एनफील्ड क्लासिक 350"
            "iPhone 13 (Like New)" -> "आईफोन 13 (नए जैसा)"
            "MacBook Air M1" -> "मैकबुक एयर M1"
            "Study Table & Chair" -> "पढ़ने की मेज और कुर्सी"
            else -> adItem.title
        }
    } else adItem.title

    val displayCategory = if (isHindi) {
        when (adItem.category) {
            "Jobs" -> "नौकरी"
            "Rentals" -> "किराया"
            "Vehicles" -> "वाहन"
            "Electronics" -> "इलेक्ट्रॉनिक्स"
            "Buy/Sell" -> "खरीदें/बेचें"
            "Furniture" -> "फर्नीचर"
            else -> adItem.category
        }
    } else adItem.category

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { onAdClick(adItem) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box {
                AsyncImage(model = adItem.imageUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(180.dp), contentScale = ContentScale.Crop)

                Row(modifier = Modifier.padding(12.dp)) {
                    if (adItem.status.equals("Expiring", ignoreCase = true)) {
                        Surface(color = Color(0xFF2196F3), shape = RoundedCornerShape(8.dp)) {
                            Text(if (isHindi) "जल्द समाप्त" else "Expiring Soon", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (adItem.status.equals("Active", ignoreCase = true)) {
                        Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp)) {
                            Text(if (isHindi) "सक्रिय" else "Active", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(displayTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Icon(if (adViewModel.isSaved(adItem)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (adViewModel.isSaved(adItem)) Color.Red else Color.Gray, modifier = Modifier.clickable { adViewModel.toggleSave(adItem) })
                }
                Text(adItem.location, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(adItem.price, color = Color(0xFFD08C60), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Surface(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(6.dp)) {
                        Text(displayCategory, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 11.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(isHindi: Boolean, onNavigate: (String) -> Unit, adViewModel: AdViewModel, onToggleLang: () -> Unit)  {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Black).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Menu, "", tint = Color(0xFFD08C60), modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(12.dp))
            Text(if (isHindi) "एडवोरा" else "Advora", color = Color(0xFFD08C60), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(onClick = onToggleLang, shape = RoundedCornerShape(8.dp), color = Color(0xFFD08C60).copy(alpha = 0.2f), border = BorderStroke(1.dp, Color(0xFFD08C60).copy(alpha = 0.5f))) {
                Text(if (isHindi) "हिं" else "EN", color = Color(0xFFD08C60), modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { adViewModel.setNewNotification(false); onNavigate("notifications") }) {
                BadgedBox(badge = { if (adViewModel.hasNewNotifications) { Badge(containerColor = Color.Red, modifier = Modifier.offset(x = (-4).dp, y = 4.dp)) } }) {
                    Icon(Icons.Default.NotificationsNone, null, tint = Color.White, modifier = Modifier.size(26.dp))
                }
            }
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = { onNavigate("profile") }) { Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(26.dp)) }
        }
    }
}

@Composable
fun SearchSection(isHindi: Boolean, searchQuery: String, onSearchChange: (String) -> Unit, onFilterClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().background(Color.Black).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        TextField(value = searchQuery, onValueChange = onSearchChange, placeholder = { Text(if (isHindi) "विज्ञापन खोजें..." else "Search ads...", color = Color.Gray) }, leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) }, modifier = Modifier.weight(1f).height(52.dp), shape = RoundedCornerShape(12.dp), textStyle = TextStyle(color = Color.Black), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
        Spacer(Modifier.width(12.dp))
        Surface(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFD08C60), onClick = onFilterClick) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Tune, null, tint = Color.White) } }
    }
}

@Composable
fun CategoryChips(isHindi: Boolean, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf(Triple("All", Icons.Default.List, "सभी"), Triple("Jobs", Icons.Default.Work, "नौकरी"), Triple("Rentals", Icons.Default.Home, "किराया"), Triple("Vehicles", Icons.Default.DirectionsCar, "वाहन"), Triple("Services", Icons.Default.Build, "सेवा"), Triple("Buy/Sell", Icons.Default.ShoppingCart, "खरीदें"))
    LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(categories) { (eng, icon, hindi) ->
            val isSelected = selectedCategory == eng
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCategorySelected(eng) }) {
                Surface(shape = RoundedCornerShape(16.dp), color = if (isSelected) Color(0xFFD08C60) else Color.White, shadowElevation = 2.dp, modifier = Modifier.size(56.dp)) { Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = if (isSelected) Color.White else Color(0xFFD08C60)) } }
                Spacer(Modifier.height(4.dp))
                Text(if (isHindi) hindi else eng, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = if (isSelected) Color(0xFFD08C60) else Color.Black)
            }
        }
    }
}

@Composable
fun BottomNavBar(onNavigate: (String) -> Unit, isHindi: Boolean) {
    Box(modifier = Modifier.fillMaxWidth().navigationBarsPadding(), contentAlignment = Alignment.BottomCenter) {
        Surface(color = Color.Black, modifier = Modifier.fillMaxWidth().height(70.dp)) {
            Row(Modifier.fillMaxSize(), Arrangement.SpaceAround, Alignment.CenterVertically) {
                NavItem(if (isHindi) "होम" else "Home", Icons.Default.Home, true) { onNavigate("home") }
                NavItem(if (isHindi) "मेरे विज्ञापन" else "My Ads", Icons.AutoMirrored.Filled.List, false) { onNavigate("ads") }
                Spacer(Modifier.width(60.dp))
                NavItem(if (isHindi) "नक्शा" else "Map", Icons.Default.LocationOn, false) { onNavigate("map") }
                NavItem(if (isHindi) "सेव्ड" else "Saved", Icons.Default.FavoriteBorder, false) { onNavigate("saved") }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.offset(y = (-14).dp)) {
            Box(modifier = Modifier.size(62.dp).background(Color(0xFFD08C60), CircleShape).clickable { onNavigate("post") }, contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(38.dp)) }
            Spacer(Modifier.height(2.dp))
            Text(if (isHindi) "पोस्ट करें" else "Post Ad", color = Color(0xFFD08C60), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun NavItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(icon, null, tint = if (selected) Color(0xFFD08C60) else Color.Gray, modifier = Modifier.size(24.dp))
        Text(label, color = if (selected) Color(0xFFD08C60) else Color.Gray, fontSize = 11.sp)
    }
}

@Composable
fun FilterSheetContent(isHindi: Boolean, loc: String, onLoc: (String) -> Unit, range: ClosedFloatingPointRange<Float>, onRange: (ClosedFloatingPointRange<Float>) -> Unit, cat: String, onCat: (String) -> Unit, onApply: () -> Unit, onClear: () -> Unit) {
    val categories = listOf(Triple("All", Icons.Default.List, "सभी"), Triple("Jobs", Icons.Default.Work, "नौकरी"), Triple("Rentals", Icons.Default.Home, "किराया"), Triple("Vehicles", Icons.Default.DirectionsCar, "वाहन"))
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(if (isHindi) "फ़िल्टर" else "Filters", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(if (isHindi) "साफ़ करें" else "Clear All", color = Color(0xFFD08C60), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onClear() })
        }
        Spacer(Modifier.height(24.dp))
        Text(if (isHindi) "स्थान" else "Location", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        OutlinedTextField(loc, onLoc, placeholder = { Text(if (isHindi) "स्थान चुनें" else "Select location") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = Color(0xFFD08C60)) })
        Spacer(Modifier.height(24.dp))
        Text(if (isHindi) "श्रेणी" else "Category", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 12.dp)) {
            items(categories) { (eng, icon, hindi) ->
                val isSelected = cat == eng
                Surface(onClick = { onCat(eng) }, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, if (isSelected) Color(0xFFD08C60) else Color.LightGray), color = if (isSelected) Color(0xFFD08C60) else Color.White) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, tint = if (isSelected) Color.White else Color.Gray, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(if (isHindi) hindi else eng, color = if (isSelected) Color.White else Color.Black, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(if (isHindi) "कीमत सीमा" else "Price Range", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("₹${range.start.toInt()} - ₹${range.endInclusive.toInt()}", color = Color(0xFFD08C60), fontWeight = FontWeight.Bold)
        }
        RangeSlider(range, onRange, valueRange = 0f..100000f, colors = SliderDefaults.colors(thumbColor = Color(0xFFD08C60), activeTrackColor = Color(0xFFD08C60)))
        Spacer(Modifier.height(32.dp))
        Button(onApply, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08C60))) {
            Text(if (isHindi) "लागू करें" else "Apply Filters", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}