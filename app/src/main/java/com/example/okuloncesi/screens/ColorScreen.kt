package com.example.okuloncesi.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.okuloncesi.R
import java.util.*

data class ColorModel(
    val name: String,
    val description: String,
    val colorValue: Color,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorScreen() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val colors = listOf(
        ColorModel("Kırmızı", "Kırmızı. Yuvarlağın rengi kırmızıdır.", Color(0xFFFF1111), R.drawable.red),
        ColorModel("Mavi", "Mavi. Balonun rengi mavidir.", Color(0xFF2196F3), R.drawable.blue),
        ColorModel("Yeşil", " Yeşil . Yaprakların rengi yeşildir.", Color(0xFF4CAF50), R.drawable.green),
        ColorModel("Sarı", "Sarı. Güneşin rengi sarıdır.", Color(0xFFFFEB3B), R.drawable.yellow),
        ColorModel("Turuncu", "Turuncu. Portakalın rengi turuncudur.", Color(0xFFFF9800), R.drawable.orangee),
        ColorModel("Mor", "Mor. Üzümün rengi mordur.", Color(0xFF9C27B0), R.drawable.grape),
        ColorModel("Siyah", "Siyah. Gördüğün şapka siyah renktedir.", Color(0xFF000000), R.drawable.black),
        ColorModel("Beyaz", "Beyaz. Telefon beyaz renktedir.", Color(0xFFFFFFFF), R.drawable.white),
        ColorModel("Kahverengi", "Kahverengi. Fındıkların rengi kahverengidir.", Color(0xFFB64624), R.drawable.brown),
        ColorModel("Gri", "Gri. Bu gördüğün kedi gri renktedir .", Color(0xFF86868D), R.drawable.gray)
    )

    val pagerState = rememberPagerState { colors.size }
    val currentColor = colors.getOrNull(pagerState.currentPage)

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        tts?.stop()
        currentColor?.let {
            val utteranceId = UUID.randomUUID().toString()
            tts?.speak(it.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    HorizontalPager(state = pagerState) { page ->
        val item = colors[page]
        var visible by remember { mutableStateOf(false) }

        val alphaAnim by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(durationMillis = 800),
            label = "alphaAnimation"
        )

        LaunchedEffect(item) {
            visible = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(item.colorValue)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.name,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.colorValue == Color.White) Color.Black else Color.White,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .clickable {
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(item.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                        }
                )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                val utteranceId = UUID.randomUUID().toString()
                                tts?.speak(item.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                            }
                            .graphicsLayer { alpha = alphaAnim }
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

            }
        }
    }
}
