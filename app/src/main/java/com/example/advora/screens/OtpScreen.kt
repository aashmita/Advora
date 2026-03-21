package com.example.advora.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(onBack: () -> Unit, onVerify: () -> Unit) {

    val context = LocalContext.current

    var otp by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = List(6) { FocusRequester() }

    var timeLeft by remember { mutableIntStateOf(30) }
    var isResendEnabled by remember { mutableStateOf(false) }

    // ⏳ Timer
    LaunchedEffect(isResendEnabled) {
        if (!isResendEnabled) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isResendEnabled = true
        }
    }

    val gradient = Brush.horizontalGradient(
        listOf(Color(0xFFFF7A18), Color(0xFF9333EA))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        // 🔙 Back Button
        IconButton(
            onClick = { onBack() },
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Verify OTP", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Enter the 6-digit code sent to your email",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 🔢 OTP INPUT
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                otp.forEachIndexed { index, value ->

                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {

                                otp = otp.toMutableList().also {
                                    it[index] = newValue
                                }

                                // 👉 Move forward
                                if (newValue.isNotEmpty() && index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .width(48.dp)
                            .height(56.dp)
                            .focusRequester(focusRequesters[index])
                            .onKeyEvent { event ->
                                if (event.type == KeyEventType.KeyDown &&
                                    event.key == Key.Backspace &&
                                    otp[index].isEmpty() &&
                                    index > 0
                                ) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                                false
                            },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = VisualTransformation.None,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ✅ Verify Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(gradient, RoundedCornerShape(12.dp))
                    .clickable {
                        if (otp.all { it.isNotEmpty() }) {
                            onVerify()
                        } else {
                            Toast.makeText(context, "Enter OTP", Toast.LENGTH_SHORT).show()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Verify", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ⏳ Resend
            if (isResendEnabled) {
                Text(
                    "Resend OTP",
                    color = Color(0xFF6366F1),
                    modifier = Modifier.clickable {
                        timeLeft = 30
                        isResendEnabled = false
                        Toast.makeText(context, "OTP Resent", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Text("Resend in $timeLeft s", color = Color.Gray)
            }
        }
    }

    // 👉 Focus first box
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

/////////////////////////////////////////////////////
// 🔍 PREVIEW
/////////////////////////////////////////////////////

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OtpPreview() {
    OtpScreen(
        onBack = {},
        onVerify = {}
    )
}