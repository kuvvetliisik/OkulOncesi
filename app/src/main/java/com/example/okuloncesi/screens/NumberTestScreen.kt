import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.okuloncesi.utils.ProgressManager
import androidx.navigation.NavController
import com.example.okuloncesi.R
import com.example.okuloncesi.data.Category
import java.util.Locale
import java.util.UUID

sealed class Question {
    data class Voice(val count: Int, val item: ObjectItem) : Question()
    data class MultipleChoice(val correct: Int, val item: ObjectItem) : Question()
}

data class ObjectItem(
    val resId: Int,
    val nameForTTSAccusative: String,
    val nameForTTSNominative: String,
)
@Composable
fun NumberTestScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    val scope = rememberCoroutineScope()

    val objectList = listOf(
        ObjectItem(R.drawable.apple, "elmayı", "elma"),
        ObjectItem(R.drawable.gray, "kediyi", "kedi"),
        ObjectItem(R.drawable.hat, "şapkayı", "şapka"),
        ObjectItem(R.drawable.heart, "kalbi", "kalp"),
        ObjectItem(R.drawable.blue, "balonu", "balon")
    )

    val questions = remember {
        val voiceQs = List(10) { Question.Voice((1..10).random(), objectList.random()) }
        val mcqQs = List(10) { Question.MultipleChoice((1..10).random(), objectList.random()) }
        (voiceQs + mcqQs).shuffled()
    }

    var index by remember { mutableStateOf(0) }
    val current = questions.getOrNull(index)

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }
    }

    LaunchedEffect(current, tts) {
        if (tts != null && current != null) {
            val sentence = when (current) {
                is Question.Voice -> "Kaç tane ${current.item.nameForTTSNominative} var?"
                is Question.MultipleChoice -> "Bana ${current.correct} tane ${current.item.nameForTTSAccusative} göster."
                else -> ""
            }
            Handler(Looper.getMainLooper()).postDelayed({
                tts?.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null)
            }, 500) //
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    if (current == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("⭐ Test Tamamlandı!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Ana Menüye Dön")
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0)) // Arka plan tüm ekranı kaplar
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Soru ${index + 1} / ${questions.size}", fontWeight = FontWeight.Bold)

            when (current) {
                is Question.Voice -> VoiceQuestionUI(q = current, tts = tts) {
                    tts?.speak("", TextToSpeech.QUEUE_FLUSH, null, null)
                    index++
                }

                is Question.MultipleChoice -> MultipleChoiceUI(q = current, tts = tts) {
                    tts?.speak("Bravo!", TextToSpeech.QUEUE_FLUSH, null, null)
                    index++
                }

                else -> {}
            }
        }
    }
}
@Composable
fun VoiceQuestionUI(q: Question.Voice, tts: TextToSpeech?, onCorrect: () -> Unit) {
    val context = LocalContext.current
    var spoken by remember(q) { mutableStateOf("") }
    var feedback by remember(q) { mutableStateOf("") }
    var listening by remember(q) { mutableStateOf(false) }

    val recognizerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        val numberWords = mapOf(
            "bir" to 1,
            "iki" to 2,
            "üç" to 3,
            "dört" to 4,
            "beş" to 5,
            "altı" to 6,
            "yedi" to 7,
            "sekiz" to 8,
            "dokuz" to 9,
            "on" to 10
        )

        if (spokenText != null) {
            spoken = spokenText
            val num = spoken.filter { it.isDigit() }.toIntOrNull()
                ?: numberWords.entries.firstOrNull { spoken.lowercase().contains(it.key) }?.value

            if (num == q.count) {
                feedback = "✅ Doğru!"
                // ✅ SADECE bu olacak — başka yerde "Süpersin!" dememelisin
                val utteranceId = UUID.randomUUID().toString()
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onDone(utteranceId: String?) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            onCorrect()
                        }, 1000)
                    }
                    override fun onError(utteranceId: String?) {}
                    override fun onStart(utteranceId: String?) {}
                })
                tts?.speak("Süpersin!", TextToSpeech.QUEUE_FLUSH, null, utteranceId)

            } else {
                feedback = "❌ Tekrar dene"
                tts?.speak("Tekrar dene", TextToSpeech.QUEUE_FLUSH, null, null)
            }

        }
        listening = false
    }

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
    }

    Text("Aşağıdaki nesneleri say ve söyle")

    repeat(q.count) {
        Image(
            painter = painterResource(id = q.item.resId),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }

    Button(onClick = {
        listening = true
        recognizerLauncher.launch(intent)
    }) {
        if (listening) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Dinleniyor...", color = Color.White)
        } else {
            Text("🎤 Konuşmak için tıkla")
        }
    }

    if (spoken.isNotEmpty()) {
        Text("Sen söyledin: \"$spoken\"")
        Text(feedback, color = if (feedback.startsWith("✅")) Color.Green else Color.Red)
    }
}
@Composable
fun MultipleChoiceUI(q: Question.MultipleChoice, tts: TextToSpeech?, onCorrect: () -> Unit) {
    val options = remember(q) {
        val others = (1..10).filter { it != q.correct }.shuffled().take(2)
        (others + q.correct).shuffled()
    }
    var feedback by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0)) // 💡 Tüm ekran arka planı
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kaç tane ${q.item.nameForTTSNominative} var?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            options.forEach { count ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 10.dp)
                        .clickable {
                            if (count == q.correct) {
                                feedback = "✅ Doğru!"
                                val utteranceId = UUID.randomUUID().toString()
                                tts?.setOnUtteranceProgressListener(object :
                                    UtteranceProgressListener() {
                                    override fun onDone(utteranceId: String?) {
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            onCorrect()
                                        }, 1000)
                                    }

                                    override fun onError(utteranceId: String?) {}
                                    override fun onStart(utteranceId: String?) {}
                                })
                                tts?.speak(
                                    "Doğru cevap",
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    utteranceId
                                )
                            } else {
                                feedback = "❌ Yanlış"
                                tts?.speak(
                                    "Bu yanlış, tekrar dene",
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    null
                                )
                            }
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), // sabit yükseklik
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(count) {
                            Image(
                                painter = painterResource(id = q.item.resId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }

            if (feedback.isNotEmpty()) {
                Text(
                    feedback,
                    color = if (feedback.startsWith("✅")) Color(0xFF2E7D32) else Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
        if (feedback.isNotEmpty()) {
            Text(feedback, color = if (feedback.startsWith("✅")) Color.Green else Color.Red)
        }
    }