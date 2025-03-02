package com.example.okuloncesi.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CategoryDetailScreen(navController: NavController, categoryName: String) {
    Text(text = "Seçilen Kategori: $categoryName")
}