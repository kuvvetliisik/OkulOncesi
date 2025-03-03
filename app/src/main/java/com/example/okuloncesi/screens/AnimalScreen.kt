package com.example.okuloncesi.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.okuloncesi.R
import java.util.*
import androidx.compose.foundation.clickable


@Composable
fun AnimalScreen() {
    val context = LocalContext.current

    // 🔊 TextToSpeech Ayarları
    var tts: TextToSpeech? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale("tr", "TR") // Türkçe dil desteği
            }
        }
    }

    // 🐾 Hayvan Listesi
    val animals = listOf(
        Triple("Köpek", R.raw.dog, "Bu hayvanın ismi köpektir."),
        Triple("Kedi", R.raw.cat, "Kediler sevimli ve oyuncudur."),
        //Triple("Kuş", R.raw.bird, "Kuşlar uçabilen canlılardır.")
    )

    var currentIndex by remember { mutableStateOf(0) }
    val (animalName, lottieAnimation, description) = animals[currentIndex]

    // 🎥 Lottie Animasyonu
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimation))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) { // 🟢 Ekranı sağa-sola kaydırarak hayvan değiştir
                detectHorizontalDragGestures { change, dragAmount ->
                    if (dragAmount > 0 && currentIndex > 0) { // ⏪ Sola kaydırınca önceki hayvan
                        currentIndex--
                    } else if (dragAmount < 0 && currentIndex < animals.size - 1) { // ⏩ Sağa kaydırınca sonraki hayvan
                        currentIndex++
                    }
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🐶 Hayvan İsmi
        Text(
            text = animalName,
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 🎥 Lottie Animasyonu (Hayvana tıklayınca sesi oynat)
        LottieAnimation(
            composition = lottieComposition,
            progress = { lottieProgress },
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
                .clickable {
                    tts?.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)
                }
        )

        // 🔊 Hoparlör Butonu (Ses çalmak için)
        IconButton(
            onClick = { tts?.speak(description, TextToSpeech.QUEUE_FLUSH, null, null) },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker), // 📢 Hoparlör ikonu ekle (res/drawable içine koymalısın)
                contentDescription = "Dinle",
                modifier = Modifier.size(80.dp)
            )
        }
    }
}
