package com.example.advora.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.advora.viewmodel.LanguageViewModel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onResetSuccess: () -> Unit,
    languageViewModel: LanguageViewModel
) {

    val context = LocalContext.current
    val isHindi = languageViewModel.isHindi

    var step by remember { mutableStateOf(1) }

    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val accentColor = Color(0xFFB56E4A)
    val cardColor = Color(0xFF2C2C2C)
    val fieldColor = Color(0xFF3A3A3A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
    ) {

        // 🔙 BACK + 🌐 LANGUAGE (MATCH LOGIN EXACTLY)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {
                if (step == 1) onBackToLogin() else step--
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
            }

            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(50))
                    .background(cardColor, RoundedCornerShape(50))
                    .clickable { languageViewModel.toggleLanguage() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Translate, null, tint = accentColor)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (isHindi) "EN" else "हिं",
                        color = accentColor,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 🔥 CENTER CONTENT
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

                // 🔥 EXACT LOGO (MATCH LOGIN)
                Row {
                    Text(
                        text = "Ad",
                        color = accentColor,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "vora",
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isHindi) "पासवर्ड रीसेट करें" else "Reset your password",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 400.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {

                    Column(modifier = Modifier.padding(22.dp)) {

                        // STEP 1 - EMAIL
                        if (step == 1) {

                            Text(if (isHindi) "ईमेल" else "Email", color = Color.White)

                            Spacer(Modifier.height(6.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, null, tint = Color.White)
                                },
                                placeholder = {
                                    Text(
                                        if (isHindi) "अपना ईमेल डालें" else "Enter your email",
                                        color = Color.Gray
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors(fieldColor)
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        step = 2
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "सही ईमेल डालें" else "Enter valid email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                            ) {
                                Text(if (isHindi) "आगे बढ़ें" else "Next")
                            }
                        }

                        // STEP 2 - OTP
                        if (step == 2) {

                            Text(if (isHindi) "OTP दर्ज करें" else "Enter OTP", color = Color.White)

                            Spacer(Modifier.height(6.dp))

                            OutlinedTextField(
                                value = otp,
                                onValueChange = { otp = it },
                                placeholder = { Text("OTP", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors(fieldColor)
                            )

                            Spacer(Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (otp == "1234") step = 3
                                    else Toast.makeText(
                                        context,
                                        if (isHindi) "गलत OTP" else "Invalid OTP",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                            ) {
                                Text(if (isHindi) "सत्यापित करें" else "Verify OTP")
                            }
                        }

                        // STEP 3 - PASSWORD
                        if (step == 3) {

                            Text(if (isHindi) "नया पासवर्ड" else "New Password", color = Color.White)

                            Spacer(Modifier.height(6.dp))

                            OutlinedTextField(
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            null,
                                            tint = Color.White
                                        )
                                    }
                                },
                                placeholder = {
                                    Text(
                                        if (isHindi) "पासवर्ड डालें" else "Enter password",
                                        color = Color.Gray
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors(fieldColor)
                            )

                            Spacer(Modifier.height(14.dp))

                            Text(if (isHindi) "पुष्टि करें" else "Confirm Password", color = Color.White)

                            Spacer(Modifier.height(6.dp))

                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                        Icon(
                                            if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            null,
                                            tint = Color.White
                                        )
                                    }
                                },
                                placeholder = {
                                    Text(
                                        if (isHindi) "दोबारा पासवर्ड डालें" else "Re-enter password",
                                        color = Color.Gray
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = textFieldColors(fieldColor)
                            )

                            Spacer(Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (newPassword.length >= 6 && newPassword == confirmPassword) {
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "पासवर्ड सफलतापूर्वक बदला गया" else "Password reset successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onResetSuccess()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "पासवर्ड सही नहीं है" else "Check password",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                            ) {
                                Text(if (isHindi) "रीसेट करें" else "Reset Password")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun textFieldColors(bg: Color) = TextFieldDefaults.colors(
    focusedContainerColor = bg,
    unfocusedContainerColor = bg,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)