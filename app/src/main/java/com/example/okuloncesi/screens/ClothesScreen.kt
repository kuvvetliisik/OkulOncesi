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
import java.util.*

data class ClothingModel(
    val name: String,
    val description: String,
    val imageRes: Int,         // 🖼️ Görsel kaynağı
    val backgroundColor: Color
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClothesScreen() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val clothes = listOf(
        ClothingModel("Tişört", "Bu bir tişörttür. Sıcak havalarda giyilir.", R.drawable.tshirt, Color(0xFFFFCDD2)),
        ClothingModel("Şapka", "Bu bir şapkadır. Başımıza takarız.", R.drawable.hat, Color(0xFFFFCDD2)),
        ClothingModel("Elbise", "Bu bir elbisedir. Renkli ve güzeldir.", R.drawable.skirt, Color(0xFFFFCDD2)),
        ClothingModel("Ceket", "Bu bir cekettir. Soğukta bizi ısıtır.", R.drawable.jacket, Color(0xFFFFCDD2)),
        ClothingModel("Ayakkabı", "Bu bir ayakkabıdır. Ayaklarımızı korur.", R.drawable.socks, Color(0xFFFFCDD2)),
        ClothingModel("Tişört", "Bu bir tişörttür. Sıcak havalarda giyilir.", R.drawable.jacket, Color(0xFFFFCDD2)),
        ClothingModel("Şapka", "Bu bir şapkadır. Başımıza takarız.", R.drawable.hoodie, Color(0xFFFFCDD2)),
        ClothingModel("Elbise", "Bu bir elbisedir. Renkli ve güzeldir.", R.drawable.jumper, Color(0xFFFFCDD2)),
        ClothingModel("Ceket", "Bu bir cekettir. Soğukta bizi ısıtır.", R.drawable.jeans, Color(0xFFFFCDD2)),
        ClothingModel("Ayakkabı", "Bu bir ayakkabıdır. Ayaklarımızı korur.", R.drawable.sneaker, Color(0xFFFFCDD2)),
    )

    val pagerState = rememberPagerState { clothes.size }
    val currentItem = clothes.getOrNull(pagerState.currentPage)

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
        val item = clothes[page]
        var isClicked by remember { mutableStateOf(false) }

        // 🎞️ Sağa-sola salınım animasyonu
        val offsetX = remember { Animatable(0f) }
        LaunchedEffect(item) {
            offsetX.snapTo(0f)
            repeat(1) {
                offsetX.animateTo(20f, animationSpec = tween(250))
                offsetX.animateTo(-20f, animationSpec = tween(250))
            }
            offsetX.animateTo(0f, animationSpec = tween(300)) // Ortaya dön
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(item.backgroundColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.name,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(item.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                        }
                )

                // 🖼️ Görsel + Animasyonlu hareket
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(250.dp)
                        .offset(x = offsetX.value.dp) // 🌀 Salınım
                        .clickable {
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(item.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                        }
                )
            }
        }
    }
}

