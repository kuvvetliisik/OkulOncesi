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
fun ShapeScreen() {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    val shapes = listOf(
        Triple("Daire", Color(0xFFFFC107), R.drawable.circle),
        Triple("Kare", Color(0xFF2196F3), R.drawable.square),
        Triple("Üçgen", Color(0xFF4CAF50), R.drawable.triangle),
        Triple("Dikdörtgen", Color(0xFFFF5722), R.drawable.rectangle),
        Triple("Yıldız", Color(0xFFFFEB3B), R.drawable.star)
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            count = shapes.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val (shapeName, bgColor, imageRes) = shapes[page]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 📝 Şekil İsmi
                    Text(
                        text = shapeName,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                tts?.speak(shapeName, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                    )

                    // 📸 Şekil Görseli
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                tts?.speak(shapeName, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = shapeName,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
