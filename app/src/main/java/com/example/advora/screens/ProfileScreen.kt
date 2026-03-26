package com.example.advora.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        val userId = auth.currentUser?.uid

        if (userId != null) {

            db.collection("users")
                .document(userId)
                .addSnapshotListener { snapshot, _ ->

                    if (snapshot != null && snapshot.exists()) {
                        name = snapshot.getString("name") ?: ""
                        email = snapshot.getString("email") ?: ""
                        phone = snapshot.getString("phone") ?: ""
                    }
                }
        } else {
            println("User not logged in")
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {

        Text("Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Name: $name")
        Text("Email: $email")
        Text("Phone: $phone")
    }
}