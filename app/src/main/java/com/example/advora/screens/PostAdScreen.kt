package com.example.advora.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.activity.result.PickVisualMediaRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAdScreen(
    onBack: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Jobs") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F1EC))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(8.dp))
            Text("विज्ञापन डालें", fontSize = 20.sp)
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(title, { title = it }, label = { Text("शीर्षक *") })
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(description, { description = it }, label = { Text("विवरण *") })
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(city, { city = it }, label = { Text("शहर *") })
            Spacer(Modifier.height(10.dp))

            OutlinedTextField(price, { price = it }, label = { Text("मूल्य *") })
            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFEDE3D7), RoundedCornerShape(10.dp))
                    .clickable {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {

                if (imageUri == null) {
                    Text("तस्वीर जोड़ें")
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { onBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("पोस्ट करें")
            }
        }
    }
}