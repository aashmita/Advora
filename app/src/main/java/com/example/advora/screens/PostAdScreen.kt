package com.example.advora.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.advora.viewmodel.AdItem
import com.example.advora.viewmodel.AdViewModel
import com.example.advora.viewmodel.LanguageViewModel
import java.util.UUID

data class CategoryItem(val name: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAdScreen(
    adViewModel: AdViewModel,
    languageViewModel: LanguageViewModel,
    adToEdit: AdItem? = null, // ✅ Added parameter for editing
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val isHindi = languageViewModel.isHindi

    // --- State Variables ---
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var error by remember { mutableStateOf("") }

    // ✅ Pre-fill details if adToEdit is not null
    LaunchedEffect(adToEdit) {
        adToEdit?.let {
            title = it.title
            description = it.description
            price = it.price.replace("₹", "")
            location = it.location
            selectedCategory = it.category
            if (it.imageUri.isNotEmpty()) {
                imageUri = Uri.parse(it.imageUri)
            }
        }
    }

    val categories = listOf(
        CategoryItem("Electronics", Icons.Default.Devices),
        CategoryItem("Vehicles", Icons.Default.DirectionsCar),
        CategoryItem("Real Estate", Icons.Default.HomeWork),
        CategoryItem("Jobs", Icons.Default.Work),
        CategoryItem("Furniture", Icons.Default.Chair),
        CategoryItem("Fashion", Icons.Default.Checkroom)
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (adToEdit != null) (if (isHindi) "संपादित करें" else "Edit Ad")
                        else (if (isHindi) "विज्ञापन डालें" else "Post New Ad"),
                        color = Color(0xFFD08C60),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "", tint = Color(0xFFD08C60))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFFF1F1F1)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- PHOTO SECTION ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color.LightGray.copy(0.5f), RoundedCornerShape(16.dp))
                    .clickable { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null, tint = Color(0xFFD08C60), modifier = Modifier.size(40.dp))
                        Text(if (isHindi) "फोटो जोड़ें" else "Add Photos", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    }
                } else {
                    AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
            }

            Spacer(Modifier.height(20.dp))

            // --- INPUT FIELDS ---
            EnhancedInputCard(if (isHindi) "शीर्षक *" else "Ad Title *") {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text(if (isHindi) "आप क्या बेच रहे हैं?" else "What are you selling?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFFD08C60)
                    )
                )
            }

            EnhancedInputCard(if (isHindi) "श्रेणी *" else "Category *") {
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text(if (isHindi) "श्रेणी चुनें" else "Select category") },
                        trailingIcon = {
                            Icon(if(expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, "", tint = Color(0xFFD08C60))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledTextColor = Color.Black,
                            disabledPlaceholderColor = Color.Gray
                        )
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        offset = androidx.compose.ui.unit.DpOffset(x = (0).dp, y = (4).dp),
                        modifier = Modifier.fillMaxWidth(0.9f).background(Color.White)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                leadingIcon = { Icon(category.icon, null, tint = Color(0xFFD08C60), modifier = Modifier.size(20.dp)) },
                                onClick = { selectedCategory = category.name; expanded = false }
                            )
                        }
                    }
                    Box(Modifier.matchParentSize().clickable { expanded = true })
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    EnhancedInputCard(if (isHindi) "कीमत" else "Price") {
                        TextField(
                            value = price,
                            onValueChange = { price = it },
                            placeholder = { Text("0.00") },
                            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = Color(0xFFD08C60)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1.2f)) {
                    EnhancedInputCard(if (isHindi) "स्थान *" else "Location *") {
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            placeholder = { Text(if (isHindi) "शहर" else "City/Area") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            EnhancedInputCard(if (isHindi) "विवरण" else "Description") {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text(if (isHindi) "विवरण लिखें..." else "Describe your item...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            // --- SUBMIT BUTTON (Update or Post) ---
            Button(
                onClick = {
                    if (title.isBlank() || selectedCategory.isBlank() || location.isBlank()) {
                        error = if (isHindi) "आवश्यक फ़ील्ड भरें" else "Please fill required fields"
                        return@Button
                    }

                    val finalAd = AdItem(
                        id = adToEdit?.id ?: UUID.randomUUID().toString(), // Keep ID if editing
                        title = title,
                        price = if (price.startsWith("₹")) price else "₹$price",
                        category = selectedCategory,
                        location = location,
                        description = description,
                        imageUri = imageUri?.toString() ?: "",
                        status = adToEdit?.status ?: "Active",
                        postedDate = adToEdit?.postedDate ?: "Just now"
                    )

                    if (adToEdit != null) {
                        adViewModel.updateAdDetails(finalAd) // ✅ Save changes
                    } else {
                        adViewModel.addAd(finalAd) // ✅ Post new
                    }

                    onNavigate("ads") // Navigate to My Ads to see the change
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD08C60)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (adToEdit != null) (if (isHindi) "अपडेट करें" else "Update Ad")
                    else (if (isHindi) "विज्ञापन पोस्ट करें" else "Post Ad Now"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun EnhancedInputCard(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray, modifier = Modifier.padding(start = 4.dp, bottom = 6.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            content()
        }
    }
}