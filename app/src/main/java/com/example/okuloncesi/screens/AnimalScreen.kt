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

data class AnimalModel(
    val name: String,
    val description: String,
    val animationRes: Int,
    val soundRes: Int,
    val backgroundColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimalScreen() {
    val context = LocalContext.current

    // MediaPlayer state
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    fun playAnimalSound(context: Context, soundRes: Int) {
        mediaPlayer.value?.release()
        mediaPlayer.value = MediaPlayer.create(context, soundRes)
        mediaPlayer.value?.start()
    }

    val animals = listOf(
        AnimalModel("At", "Bu gördüğün hayvan attır. Atlar çok hızlı hayvanlardır Atların sesi böyledir", R.raw.horse_animation, R.raw.horse_sound,  Color(
            0xFF6628C9
        )
        ),
        AnimalModel("Kedi", "Bu gördüğün hayvan kedidir. Kediler tatlı hayvanlardır Kedilerin sesi böyledir.", R.raw.cat_animation, R.raw.cat_sound, Color(
            0xFF6029AD
        )
        ),
        AnimalModel("Kuş", "Bu gördüğün hayvan kuştur. Kuşlar havada uçarlar ve öterler Kuşların sesi böyledir.", R.raw.bird_animation, R.raw.bird_sound, Color(
            0xFF5B14C4
        )
        ),
        AnimalModel("Köpek", "Bu gördüğün hayvan köpektir. Köpekler havlarlar Köpeklerin sesi böyledir.", R.raw.dog_animation, R.raw.dog_voice, Color(
            0xFF6120C4
        )
        ),
        AnimalModel("Aslan", "Bu gördüğün hayvan aslandır. Aslanlar ormanın kralıdır Aslanların sesi böyledir.", R.raw.lion_animation, R.raw.lion_sound, Color(
            0xFF5922AD
        )
        ),
        AnimalModel("Fil", "Bu gördüğün hayvan fildir. Filler çok su içerler. fillerin sesi böyledir", R.raw.elephant_animation, R.raw.elephant_sound, Color(
            0xFF733DB4
        )
        ),
        AnimalModel("Kurt", "Bu gördüğün hayvan kurttur. Kurtlar Türklerin simgesidir. Kurtların sesi böyledir", R.raw.wolf_animation, R.raw.wolf_sound, Color(
            0xFF6B29C7
        )
        ),
        AnimalModel("İnek", "Bu görüdüğün hayvan inektir. İnekler süt verirler ve bizde içeriz. İneklerin sesi böyledir", R.raw.cow_animation, R.raw.cow_sound, Color(
            0xFF6426C0
        )
        ),
        AnimalModel("Maymun", "Bu gördüğün hayvan maymundur. Maymunlar ağaçlarda yaşar. Maymunların sesi böyledir", R.raw.monkey_animation, R.raw.monkey_sound, Color(
            0xFF6021B9
        )
        ),
        AnimalModel("Tilki", "Bu gördüğün hayvan tilkidir. Tilkilerin sesi böyledir.", R.raw.fox_animation, R.raw.fox_sound, Color(
            0xFF5C1EC2
        )
        )
    )

    val pagerState = rememberPagerState { animals.size }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val currentPage = pagerState.currentPage
    val animal = animals.getOrNull(currentPage)

    // TTS başlat
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        tts?.stop()
        mediaPlayer.value?.stop()
        mediaPlayer.value?.release()
        mediaPlayer.value = null
    }

    // 🧹 Ekran kapanınca: Temizle
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
            mediaPlayer.value?.release()
            mediaPlayer.value = null
        }
    }

    // TTS listener her değişiklikte ayarlanır
    LaunchedEffect(animal) {
        val utteranceId = UUID.randomUUID().toString()
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                animal?.let {
                    playAnimalSound(context, it.soundRes)
                }
            }

            override fun onError(utteranceId: String?) {}
            override fun onStart(utteranceId: String?) {}
        })
    }

    HorizontalPager(state = pagerState) { page ->
        val item = animals[page]
        var isClicked by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (isClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 150),
            label = "scaleAnimation"
        )

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(item.animationRes))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

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
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            isClicked = true
                            val utteranceId = UUID.randomUUID().toString()
                            tts?.speak(item.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
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
                            tts?.speak(item.description, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
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
