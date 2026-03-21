package com.example.advora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.advora.navigation.AppNavigation
import com.example.advora.ui.theme.AdvoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AdvoraTheme {
                AppNavigation()
            }
        }
    }
}