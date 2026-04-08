package com.example.advora.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.R
import com.example.advora.viewmodel.LanguageViewModel

@Composable
fun LoginScreen(
    onLogin: (Boolean) -> Unit, // Boolean true = Admin, false = User
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    languageViewModel: LanguageViewModel
) {
    val context = LocalContext.current
    val isHindi = languageViewModel.isHindi

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val backgroundColor = Color(0xFFEDEDED)
    val primaryColor = Color(0xFFB86B4B)
    val cardColor = Color(0xFF2C2C2C)
    val fieldColor = Color(0xFF3A3A3A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        /////////////////////////////////////////////////////////////
        // 🌐 LANGUAGE TOGGLE
        /////////////////////////////////////////////////////////////
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier
                    .shadow(6.dp, RoundedCornerShape(50))
                    .background(
                        color = Color(0xFF2C2C2C),
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { languageViewModel.toggleLanguage() }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Translate,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isHindi) "EN" else "हिं",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        /////////////////////////////////////////////////////////////
        // MAIN CONTENT
        /////////////////////////////////////////////////////////////
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔥 LOGO
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Text(
                        text = "Ad",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        text = "vora",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isHindi) "Advora में आपका स्वागत है"
                    else "Welcome back to Advora",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /////////////////////////////////////////////////////////////
            // CARD
            /////////////////////////////////////////////////////////////
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isHindi) "लॉगिन करें" else "Login",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = if (isHindi) "ईमेल" else "Email",
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                if (isHindi) "आपका ईमेल" else "your@email.com",
                                color = Color.Gray
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Email, null, tint = Color.Gray)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = fieldColor,
                            unfocusedContainerColor = fieldColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isHindi) "पासवर्ड" else "Password",
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text("••••••••", color = Color.Gray)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, null, tint = Color.Gray)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = fieldColor,
                            unfocusedContainerColor = fieldColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFFB56E4A),
                        modifier = Modifier.clickable { onForgotPassword() }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 🔥 UPDATED BUTTON LOGIC FOR ADMIN
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                // Admin Logic
                                if (email == "admin@gmail.com" && password == "1234") {
                                    onLogin(true)
                                } else {
                                    onLogin(false)
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    if (isHindi) "डिटेल भरें" else "Enter details",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(
                            text = if (isHindi) "लॉगिन" else "Login",
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isHindi) "नया उपयोगकर्ता?" else "New User? ",
                            color = Color.LightGray
                        )
                        Text(
                            text = if (isHindi) "रजिस्टर करें" else "Register",
                            color = primaryColor,
                            modifier = Modifier.clickable { onRegister() }
                        )
                    }
                }
            }
        }
    }
}