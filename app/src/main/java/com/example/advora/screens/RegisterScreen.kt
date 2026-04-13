package com.example.advora.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.LanguageViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RegisterScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    languageViewModel: LanguageViewModel
) {
    val isHindi = languageViewModel.isHindi

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val primaryOrange = Color(0xFFD08C60)
    val appBackground = Color(0xFFFDFDFD)
    val darkCardBackground = Color(0xFF2D2D2D)
    val inputFieldBg = Color(0xFF454545)

    Box(modifier = Modifier.fillMaxSize().background(appBackground)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(28.dp))
            }

            Surface(
                onClick = { languageViewModel.toggleLanguage() },
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF333333),
                modifier = Modifier.height(38.dp).width(70.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Translate, null, tint = primaryOrange, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (isHindi) "EN" else "हि", color = primaryOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(110.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text("Ad", color = primaryOrange, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
                Text("vora", color = Color.Black, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text(
                text = if (isHindi) "अपना खाता बनाएं" else "Create your account",
                color = Color.Gray,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = darkCardBackground),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isHindi) "रजिस्टर" else "Register",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    InputField(if (isHindi) "पूरा नाम" else "Full Name", Icons.Default.Person, name, inputFieldBg) { name = it }
                    InputField(if (isHindi) "ईमेल" else "Email", Icons.Default.Email, email, inputFieldBg) { email = it }

                    PasswordField(if (isHindi) "पासवर्ड" else "Password", password, passwordVisible, Icons.Default.Lock, inputFieldBg, { password = it }) { passwordVisible = !passwordVisible }
                    PasswordField(if (isHindi) "पासवर्ड की पुष्टि करें" else "Confirm Password", confirmPassword, confirmVisible, Icons.Default.Lock, inputFieldBg, { confirmPassword = it }) { confirmVisible = !confirmVisible }

                    if (error.isNotEmpty()) {
                        Text(error, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    }

                    Button(
                        onClick = {
                            val trimmedEmail = email.trim()
                            // CHECK IF MAIL ALREADY EXISTS
                            val emailExists = registeredUsers.any { it.email.equals(trimmedEmail, ignoreCase = true) }

                            if (name.isBlank() || trimmedEmail.isBlank() || password.isBlank()) {
                                error = if (isHindi) "सभी फ़ील्ड भरें" else "Please fill all fields"
                            } else if (emailExists) {
                                error = if (isHindi) "यह ईमेल पहले से मौजूद है" else "Email already registered"
                            } else if (password != confirmPassword) {
                                error = if (isHindi) "पासवर्ड मेल नहीं खाते" else "Passwords do not match"
                            } else {
                                registeredUsers.add(UserItem(
                                    id = (registeredUsers.size + 1).toString(),
                                    name = name,
                                    email = trimmedEmail,
                                    password = password,
                                    phone = "",
                                    totalAds = 0,
                                    joinDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
                                ))
                                onSuccess()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isHindi) "रजिस्टर" else "Register", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(modifier = Modifier.padding(top = 20.dp)) {
                        Text(if (isHindi) "पहले से खाता है? " else "Already have an account? ", color = Color.Gray)
                        Text(
                            text = if (isHindi) "लॉगिन करें" else "Login",
                            color = primaryOrange,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onBack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(label: String, icon: ImageVector, value: String, color: Color, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color(0xFFAAAAAA)) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFFAAAAAA)) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = color, unfocusedContainerColor = color,
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            cursorColor = Color(0xFFD08C60),
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordField(label: String, value: String, visible: Boolean, icon: ImageVector, color: Color, onValueChange: (String) -> Unit, toggle: () -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color(0xFFAAAAAA)) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFFAAAAAA)) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = { IconButton(onClick = toggle) { Icon(if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color(0xFF888888)) } },
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = color, unfocusedContainerColor = color,
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            cursorColor = Color(0xFFD08C60),
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        )
    )
}