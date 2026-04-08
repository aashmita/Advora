
package com.example.advora.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.advora.viewmodel.LanguageViewModel

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

    val primaryColor = Color(0xFFB86B4B)
    val cardColor = Color(0xFF2C2C2C)
    val fieldColor = Color(0xFF4A4A4A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
    ) {
        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .padding(start = 12.dp, top = 40.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        // 🌐 LANGUAGE TOGGLE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(50))
                    .background(cardColor, RoundedCornerShape(50))
                    .clickable { languageViewModel.toggleLanguage() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Translate, null, tint = primaryColor)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (isHindi) "EN" else "हिं",
                        color = primaryColor
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(75.dp))

            Row {
                Text("Ad", color = primaryColor, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("vora", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = if (isHindi) "अपना खाता बनाएं" else "Create your account",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔥 IMPROVED CARD SIZE
            Card(
                modifier = Modifier.fillMaxWidth(0.88f), // 👈 slightly bigger
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp), // 👈 more spacing
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = if (isHindi) "रजिस्टर करें" else "Register",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InputField(
                        placeholder = if (isHindi) "पूरा नाम" else "Full Name",
                        icon = Icons.Default.Person,
                        value = name,
                        fieldColor = fieldColor,
                        onChange = { name = it }
                    )

                    InputField(
                        placeholder = if (isHindi) "ईमेल" else "Email",
                        icon = Icons.Default.Email,
                        value = email,
                        fieldColor = fieldColor,
                        onChange = { email = it }
                    )

                    PasswordField(
                        placeholder = if (isHindi) "पासवर्ड" else "Password",
                        value = password,
                        visible = passwordVisible,
                        icon = Icons.Default.Lock,
                        fieldColor = fieldColor,
                        onChange = { password = it },
                        toggle = { passwordVisible = !passwordVisible }
                    )

                    PasswordField(
                        placeholder = if (isHindi) "पासवर्ड पुष्टि करें" else "Confirm Password",
                        value = confirmPassword,
                        visible = confirmVisible,
                        icon = Icons.Default.Lock,
                        fieldColor = fieldColor,
                        onChange = { confirmPassword = it },
                        toggle = { confirmVisible = !confirmVisible }
                    )

                    if (error.isNotEmpty()) {
                        Text(error, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            when {
                                name.isBlank() ->
                                    error = if (isHindi) "नाम दर्ज करें" else "Enter name"
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                                    error = if (isHindi) "गलत ईमेल" else "Invalid email"
                                password.length < 6 ->
                                    error = if (isHindi) "पासवर्ड छोटा है" else "Password too short"
                                password != confirmPassword ->
                                    error = if (isHindi) "पासवर्ड मेल नहीं खाते" else "Passwords mismatch"
                                else -> {
                                    error = ""
                                    onSuccess()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(if (isHindi) "रजिस्टर करें" else "Register",
                            color = Color.White
                            )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Text(
                            text = if (isHindi) "पहले से खाता है?" else "Already have an account?",
                            color = Color.Gray
                        )
                        Text(
                            text = if (isHindi) " लॉगिन करें" else " Login",
                            color = primaryColor,
                            modifier = Modifier.clickable { onBack() }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun InputField(
    placeholder: String,
    icon: ImageVector,
    value: String,
    fieldColor: Color, // ✅ ADD THIS
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        leadingIcon = { Icon(icon, null, tint = Color.LightGray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
}
@Composable
fun PasswordField(
    placeholder: String,
    value: String,
    visible: Boolean,
    icon: ImageVector,
    fieldColor: Color,
    onChange: (String) -> Unit,
    toggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        leadingIcon = { Icon(icon, null, tint = Color.LightGray) },
        trailingIcon = {
            IconButton(onClick = toggle) {
                Icon(
                    if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Gray // 👈 FIXED
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fieldColor,
            unfocusedContainerColor = fieldColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}