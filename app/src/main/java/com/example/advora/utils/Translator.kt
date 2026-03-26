package com.example.advora.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

suspend fun translateText(text: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(
                "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=hi&dt=t&q=$text"
            )

            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"

            val response = connection.inputStream.bufferedReader().readText()

            // VERY SIMPLE PARSE
            val translated = response.split("\"")[1]

            translated
        } catch (e: Exception) {
            text // fallback
        }
    }
}