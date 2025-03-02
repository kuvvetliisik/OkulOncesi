package com.example.okuloncesi.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LearningScreen(navController: NavController, category: String, index: Int) {
    Text(text = "Öğrenme Ekranı - Kategori: $category, Kavram: $index")
}
