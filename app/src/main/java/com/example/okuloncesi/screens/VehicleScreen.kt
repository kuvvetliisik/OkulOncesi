package com.example.okuloncesi.screens

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.okuloncesi.R
import java.util.*

data class VehicleModel(
    val name: String,
    val description: String,
    val animationRes: Int,
    val backgroundColor: Color
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VehicleScreen() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val vehicles = listOf(
        VehicleModel("Araba", "Bu bir arabadır. Arabalar yolda gider.", R.raw.car_animation, Color(0xFFBBDEFB)),
        VehicleModel("Tren", "Bu bir otobüstür. Otobüsle okula gideriz.", R.raw.train_animation, Color(0xFFBBDEFB)),
        VehicleModel("Uçak", "Bu bir uçaktır. Uçak gökyüzünde uçar.", R.raw.plane_animation, Color(0xFFBBDEFB)),
        VehicleModel("Helikopter", "Bu bir trendir. Tren raylarda gider.", R.raw.helicopter_animation, Color(0xFFBBDEFB)),
        VehicleModel("Gemi", "Bu bir gemidir. Gemiler denizde yüzer.", R.raw.boat_animation, Color(0xFFBBDEFB)),
        VehicleModel("Traktör", "Bu bir traktördür. Tarlalarda çalışır.", R.raw.tractor_animation, Color(0xFFBBDEFB)),
        VehicleModel("Bisiklet", "Bu bir motosiklettir. Motosikletlerde sürer.", R.raw.bicycle_animation, Color(0xFFBBDEFB)),
        VehicleModel("Ambulans", "Bu bir motosiklettir. Motosikletlerde sürer.", R.raw.ambulance_animation, Color(0xFFBBDEFB)),
        VehicleModel("Taksi", "Bu bir motosiklettir. Motosikletlerde sürer.", R.raw.taxi_animation, Color(0xFFBBDEFB)),
        VehicleModel("Otobüs", "Bu bir motosiklettir. Motosikletlerde sürer.", R.raw.bus_animation, Color(0xFFBBDEFB)),


    )

    val pagerState = rememberPagerState { vehicles.size }
    val currentItem = vehicles.getOrNull(pagerState.currentPage)

    // TTS başlat
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    // Sayfa değişince TTS durdur
    LaunchedEffect(pagerState.currentPage) {
        tts?.stop()
    }

    // Ekrandan çıkınca TTS kapat
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    HorizontalPager(state = pagerState) { page ->
        val vehicle = vehicles[page]
        var isClicked by remember { mutableStateOf(false) }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(vehicle.animationRes))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

        val scale by animateFloatAsState(
            targetValue = if (isClicked) 1.1f else 1f,
            animationSpec = tween(200),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(vehicle.backgroundColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = vehicle.name,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            isClicked = true
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(vehicle.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                            isClicked = false
                        }
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .size(300.dp)
                        .clickable {
                            isClicked = true
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(vehicle.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                            isClicked = false
                        }
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
