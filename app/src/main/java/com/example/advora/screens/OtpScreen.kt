package com.example.advora.screens

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.advora.viewmodel.LanguageViewModel

@Composable
fun OtpScreen(
    onVerify: () -> Unit,
    onBack: () -> Unit,
    languageViewModel: LanguageViewModel
) {

    val isHindi = languageViewModel.isHindi

    val primaryColor = Color(0xFFB86B4B)
    val cardColor = Color(0xFF2C2C2C)
    val fieldColor = Color(0xFF4A4A4A)

    val otpLength = 6
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(otpLength) { FocusRequester() }

    var timer by remember { mutableStateOf(30) }
    var isResendEnabled by remember { mutableStateOf(false) }

    // ⏳ TIMER
    LaunchedEffect(timer) {
        if (timer > 0) {
            delay(1000)
            timer--
        } else {
            isResendEnabled = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
    ) {

        // 🔙 BACK + 🌐 LANGUAGE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 12.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
            }

            Box(
                modifier = Modifier
                    .background(cardColor, RoundedCornerShape(50))
                    .clickable { languageViewModel.toggleLanguage() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Translate, null, tint = primaryColor)
                    Spacer(Modifier.width(4.dp))
                    Text(if (isHindi) "EN" else "हिं", color = primaryColor)
                }
            }
        }

        // 🔥 CENTERED CONTENT
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

                // 🔥 BRAND
                Text("Advora", fontSize = 26.sp, color = primaryColor)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isHindi) "खाता सत्यापित करें" else "Verify your account",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // 🔥 CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 400.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {

                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = if (isHindi) "OTP दर्ज करें" else "Enter OTP",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isHindi) "6 अंकों का कोड दर्ज करें" else "Enter 6-digit code",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // 🔢 OTP BOXES
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            for (i in 0 until otpLength) {

                                OutlinedTextField(
                                    value = otpValues[i],
                                    onValueChange = { value ->
                                        if (value.length <= 1) {
                                            otpValues[i] = value

                                            if (value.isNotEmpty() && i < otpLength - 1) {
                                                focusRequesters[i + 1].requestFocus()
                                            }
                                            if (value.isEmpty() && i > 0) {
                                                focusRequesters[i - 1].requestFocus()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .width(45.dp)
                                        .height(55.dp)
                                        .focusRequester(focusRequesters[i]),
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = fieldColor,
                                        unfocusedContainerColor = fieldColor,
                                        focusedBorderColor = primaryColor,
                                        unfocusedBorderColor = Color.Transparent
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(26.dp))

                        Button(
                            onClick = onVerify,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text(if (isHindi) "सत्यापित करें" else "Verify")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (isResendEnabled) {
                            Text(
                                text = if (isHindi) "OTP पुनः भेजें" else "Resend OTP",
                                color = primaryColor,
                                modifier = Modifier.clickable {
                                    timer = 30
                                    isResendEnabled = false
                                }
                            )
                        } else {
                            Text(
                                text = if (isHindi) "फिर से भेजें: $timer सेकंड"
                                else "Resend in $timer s",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}