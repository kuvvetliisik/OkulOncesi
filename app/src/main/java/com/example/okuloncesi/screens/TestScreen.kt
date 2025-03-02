package com.example.okuloncesi.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun TestScreen(navController: NavController, category: String) {
    Text(text = "Test Ekranı - Kategori: $category")
}