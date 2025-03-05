package com.example.okuloncesi.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.okuloncesi.R
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NumberScreen() {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current // Titreşim için

    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale("tr", "TR") // Türkçe dil desteği
            }
        }
    }

    val numbers = listOf(
        Triple("1", "Bir Elma", R.drawable.apple),
        Triple("2", "İki Ördek", R.drawable.duck),
        Triple("3", "Üç Çiçek", R.drawable.flower),
        Triple("4", "Dört Domates", R.drawable.tomato),
        Triple("5", "Beş Yıldız", R.drawable.star)
    )

    val backgroundColors = listOf(
        Color(0xFFFFCDD2), // Açık kırmızı
        Color(0xFFBBDEFB), // Açık mavi
        Color(0xFFC8E6C9), // Açık yeşil
        Color(0xFFFFF9C4), // Açık sarı
        Color(0xFFD1C4E9)  // Açık mor
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            count = numbers.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val (number, text, imageRes) = numbers[page]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColors[page]),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 📝 Büyük ve Renkli Sayı (Titreşim + Ses)
                    Text(
                        text = number,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress) // 🔥 Titreşim
                                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                    )

                    // 📸 Sayıyı Temsil Eden Görsel (Meyve, nesne vb.)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress) // 🔥 Titreşim
                                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = text,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}



