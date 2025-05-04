package com.example.okuloncesi.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.Animatable
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
import androidx.compose.ui.res.painterResource
import com.example.okuloncesi.R
import kotlinx.coroutines.delay
import java.util.*

data class ShapeModel(
    val name: String,
    val description: String,
    val imageRes: Int,
    val backgroundColor: Color
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShapeScreen() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val shapes = listOf(
        ShapeModel("Daire", "Bu bir dairedir. Yuvarlaktır.", R.drawable.circle, Color(0xFFE1BEE7)),
        ShapeModel("Kare", "Bu bir karedir. Tüm kenarları eşittir.", R.drawable.square, Color(0xFFFFF59D)),
        ShapeModel("Üçgen", "Bu bir üçgendir. Üç kenarı vardır.", R.drawable.triangle, Color(0xFFFFCDD2)),
        ShapeModel("Dikdörtgen", "Bu bir dikdörtgendir. Uzun kenarları vardır.", R.drawable.rectangle, Color(0xFFB3E5FC)),
        ShapeModel("Yıldız", "Bu bir yıldızdır. Parlak ve köşelidir.", R.drawable.star, Color(0xFFC8E6C9)),
        ShapeModel("Kalp", "Bu bir kalptir. Sevgiyi temsil eder.", R.drawable.heart, Color(0xFFF48FB1)),
        ShapeModel("Oval", "Bu bir ovaldir. Uzamış bir daireye benzer.", R.drawable.crescent, Color(0xFF80CBC4)),
        ShapeModel("Beşgen", "Bu bir beşgendir. Beş kenarı vardır.", R.drawable.pentagon, Color(0xFFAED581)),
        ShapeModel("Oval", "Bu bir ovaldir. Uzamış bir daireye benzer.", R.drawable.diamond, Color(0xFF80CBC4)),
        ShapeModel("Beşgen", "Bu bir beşgendir. Beş kenarı vardır.", R.drawable.hexagon, Color(0xFFAED581)),

    )

    val pagerState = rememberPagerState { shapes.size }
    val currentShape = shapes.getOrNull(pagerState.currentPage)

    // TTS başlat
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        tts?.stop()
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    HorizontalPager(state = pagerState) { page ->
        val shape = shapes[page]
        var isVisible by remember { mutableStateOf(false) }

        val offsetY = remember { Animatable(-300f) }
        LaunchedEffect(shape) {
            offsetY.snapTo(-300f)
            offsetY.animateTo(0f, animationSpec = tween(500))
            isVisible = true

            delay(10)
            val utteranceId = UUID.randomUUID().toString()
            tts?.speak(shape.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(shape.backgroundColor)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = shape.name,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                if (isVisible) {
                    Image(
                        painter = painterResource(id = shape.imageRes),
                        contentDescription = shape.name,
                        modifier = Modifier
                            .size(250.dp)
                            .offset(y = offsetY.value.dp)
                    )
                }
            }
        }
    }
}
