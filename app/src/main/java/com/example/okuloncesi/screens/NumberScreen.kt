package com.example.okuloncesi.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.okuloncesi.R
import java.util.*
import androidx.compose.foundation.clickable

@Composable
fun NumberScreen() {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale("tr", "TR") // Türkçe dil desteği
            }
        }
    }

    val numbers = listOf(
        Pair("1", "Bir"),
        Pair("2", "İki"),
        Pair("3", "Üç"),
        Pair("4", "Dört"),
        Pair("5", "Beş")
    )

    val numberImages = listOf(
        R.drawable.one,
        R.drawable.two,
        R.drawable.three,
        R.drawable.four,
        R.drawable.five
    )

    val backgroundColors = listOf(
        Color(0xFFFFCDD2), // Açık kırmızı
        Color(0xFFBBDEFB), // Açık mavi
        Color(0xFFC8E6C9), // Açık yeşil
        Color(0xFFFFF9C4), // Açık sarı
        Color(0xFFD1C4E9)  // Açık mor
    )

    var currentIndex by remember { mutableStateOf(0) }
    val (number, text) = numbers[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColors[currentIndex]) // 🌈 Renkli arkaplan
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount.x > 50 && currentIndex > 0) {
                        currentIndex--
                        tts?.speak("Önceki", TextToSpeech.QUEUE_FLUSH, null, null)
                    } else if (dragAmount.x < -50 && currentIndex < numbers.size - 1) {
                        currentIndex++
                        tts?.speak("Sonraki", TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 📝 Büyük ve Renkli Sayı
            Text(
                text = number,
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Beyaz metin
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) }
            )

            // 📸 Sayı Görseli
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .size(250.dp)
                    .clickable { tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) }
            ) {
                Image(
                    painter = painterResource(id = numberImages[currentIndex]),
                    contentDescription = text,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
