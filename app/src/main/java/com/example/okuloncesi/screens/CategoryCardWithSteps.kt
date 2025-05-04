import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.Image
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

@Composable
fun CategoryCardWithSteps(category: Category, navController: NavController) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val egitimKey = "${category.name.lowercase()}_egitim"
    val testKey = "${category.name.lowercase()}_test"

    val egitimDone = remember { mutableStateOf(false) }
    val testDone = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale("tr", "TR")
            }
        }

        ProgressManager.saveProgress(context, "sayılar_egitim", true)
        egitimDone.value = ProgressManager.getProgress(context, egitimKey)
        testDone.value = ProgressManager.getProgress(context, testKey)
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                tts?.speak("${category.name}", TextToSpeech.QUEUE_FLUSH, null, null)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = category.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.name,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = category.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // ✅ ÖĞREN BUTONU
            Button(
                onClick = {
                    val utteranceId = UUID.randomUUID().toString()
                    tts?.speak("${category.name} öğren", TextToSpeech.QUEUE_FLUSH, null, utteranceId)

                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onDone(id: String?) {
                            Handler(Looper.getMainLooper()).post {
                                when (category.name) {
                                    "Hayvanlar" -> navController.navigate("animalScreen")
                                    "Sayılar" -> navController.navigate("numberScreen")
                                    "Renkler" -> navController.navigate("colorScreen")
                                    "Şekiller" -> navController.navigate("shapeScreen")
                                    "Meyveler" -> navController.navigate("fruitScreen")
                                    "Araçlar" -> navController.navigate("vehicleScreen")
                                    "Kıyafetler" -> navController.navigate("clothesScreen")
                                }
                            }
                        }

                        override fun onError(utteranceId: String?) {}
                        override fun onStart(utteranceId: String?) {}
                    })
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)) // Mor
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_learn), // 👂 ikonunu buraya ekle
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${category.name} Öğren", color = Color.White)
            }


            // ✅ TEST BUTONU
            Button(
                onClick = {
                    val utteranceId = UUID.randomUUID().toString()
                    tts?.speak("${category.name} testi", TextToSpeech.QUEUE_FLUSH, null, utteranceId)

                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onDone(id: String?) {
                            Handler(Looper.getMainLooper()).post {
                                when (category.name) {
                                    "Sayılar" -> navController.navigate("numberTestScreen")
                                    // diğer test ekranları eklenebilir
                                }
                            }
                        }

                        override fun onError(utteranceId: String?) {}
                        override fun onStart(utteranceId: String?) {}
                    })
                },
                enabled = egitimDone.value,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (egitimDone.value) Color(0xFFFFA726) else Color(0xFFE0E0E0)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_test), // 🎯 ikonunu buraya ekle
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${category.name} Test Et", color = Color.White)
            }


            if (egitimDone.value && testDone.value) {
                Text(
                    text = "✅ Tamamlandı",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Green
                )
            }
        }
    }
}
