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

data class FruitModel(
    val name: String,
    val description: String,
    val animationRes: Int,
    val backgroundColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FruitScreen() {
    val context = LocalContext.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val fruits = listOf(
        FruitModel("Elma", "Bu meyve elmadır. Elmalar ağaçlarda yetişir.", R.raw.apple_animation, Color(0xFF81C784)),
        FruitModel("Muz", "Bu meyve muzdur. Muzlar sarıdır ve yumuşaktır.", R.raw.banana_animation, Color(0xFF81C784)),
        FruitModel("Çilek", "Bu meyve çilektir. Tatlı ve kırmızıdır.", R.raw.strawberry_animation, Color(0xFF81C784)),
        FruitModel("Portakal", "Bu meyve portakaldır. Turuncu ve sulu bir meyvedir.", R.raw.orange_animation, Color(0xFF81C784)),
        FruitModel("Armut", "Bu meyve armuttur. Armutlar yeşil ya da sarı olabilir.", R.raw.pear_animation, Color(0xFF81C784)),
        FruitModel("Limon", "Bu meyve limondur. Limonlar ekşidir ve sarıdır.", R.raw.lemon_animation, Color(0xFF81C784)),
        FruitModel("Ananas", "Bu meyve ananastır. Tropik ve tatlı bir meyvedir.", R.raw.pineapple_animation, Color(0xFF81C784)),
        FruitModel("Karpuz", "Bu meyve karpuzdur. Karpuzlar büyük ve suludur.", R.raw.watermelon_animation, Color(0xFF81C784)),
        FruitModel("Vişne", "Bu meyve vişnedir. Vişneler kırmızı ve ekşidir.", R.raw.cherry_animation, Color(0xFF81C784)),
        FruitModel("Avokado", "Bu meyve avokadodur. Avokadolar yeşil ve sağlıklıdır.", R.raw.avocado_animation, Color(0xFF81C784)),
    )

    val pagerState = rememberPagerState { fruits.size }
    val currentFruit = fruits.getOrNull(pagerState.currentPage)

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

    // Ekran kapanınca TTS kapat
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    HorizontalPager(state = pagerState) { page ->
        val fruit = fruits[page]
        var isClicked by remember { mutableStateOf(false) }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(fruit.animationRes))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

        val scale by animateFloatAsState(
            targetValue = if (isClicked) 1.1f else 1f,
            animationSpec = tween(200),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fruit.backgroundColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = fruit.name,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            isClicked = true
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(fruit.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
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
                            tts?.speak(fruit.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
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
