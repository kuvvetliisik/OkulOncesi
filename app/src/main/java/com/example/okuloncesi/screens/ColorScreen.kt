package com.example.okuloncesi.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.okuloncesi.R
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorScreen() {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    // 🔊 Text-to-Speech Ayarı
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    // 🎨 Renkler Listesi (İsim, Arka Plan Rengi, Görsel)
    val colors = listOf(
        Triple("Kırmızı", Color(0xFFFF0000), R.drawable.apple),
        Triple("Mavi", Color(0xFF0000FF), R.drawable.duck),
        //Triple("Yeşil", Color(0xFF00FF00), R.drawable.green_object),
        //Triple("Sarı", Color(0xFFFFFF00), R.drawable.yellow_object),
        //Triple("Turuncu", Color(0xFFFFA500), R.drawable.orange_object)
    )

    val pagerState = rememberPagerState { colors.size }

    HorizontalPager(state = pagerState) { page ->
        val (colorName, colorValue, imageRes) = colors[page]
        var isClicked by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (isClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 150),
            label = "scaleAnimation"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorValue),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 🔵 Renk İsmi
                Text(
                    text = colorName,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            isClicked = true
                            tts?.speak(colorName, TextToSpeech.QUEUE_FLUSH, null, null)
                            isClicked = false
                        }
                )

                // 🎨 Rengi Temsil Eden Görsel
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .size(250.dp)
                        .clickable {
                            isClicked = true
                            tts?.speak(colorName, TextToSpeech.QUEUE_FLUSH, null, null)
                            isClicked = false
                        }
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = colorName,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
