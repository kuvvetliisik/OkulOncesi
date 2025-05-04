package com.example.okuloncesi.screens

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.animation.core.Animatable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.okuloncesi.R
import kotlinx.coroutines.delay
import java.util.*


data class NumberModel(
    val number: Int,
    val description: String,
    val iconRes: Int, // Tek görsel, çok kez çoğaltılacak
    val backgroundColor: Color
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NumberScreen() {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val numberText = listOf("Biiir", "İkii", "Üç", "Döört", "Beşş", "Alttı", "Yedii", "Sekizz", "Dokuzz", "Oon")
    val pagerState = rememberPagerState { 10 }

    // Animasyon listesi
    val allOffsets = remember { List(10) { Animatable(0f) } }

    // TTS başlat
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    // Sayfa değişince yeni geleni indir + konuş
    LaunchedEffect(pagerState.currentPage) {
        val index = pagerState.currentPage
        allOffsets[index].snapTo(-300f)
        allOffsets[index].animateTo(0f, tween(600))
        delay(300)
        tts?.speak(numberText[index], TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString())
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    HorizontalPager(state = pagerState) { page ->
        val total = page + 1 // kaç görsel gösterilecek

        // 👇 Listeyi 4 satıra böl (üst, orta, alt, en alt)
        val rows = listOf(
            listOfNotNull(if (total == 10) 9 else null), // sadece 10 için en üste 1
            (6..8).filter { it < total },
            (3..5).filter { it < total },
            (0..2).filter { it < total }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFDE7)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                rows.forEach { indexList ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        indexList.forEach { i ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                modifier = Modifier
                                    .size(110.dp)
                                    .offset(y = allOffsets[i].value.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.apple), // 🍎 istediğin ikon
                                    contentDescription = "Sayı görseli",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
