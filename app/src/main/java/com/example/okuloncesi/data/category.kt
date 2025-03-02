package com.example.okuloncesi.data

import androidx.compose.ui.graphics.Color

data class Category(
    val name: String,
    val image: String, // URL veya drawable resource ID olabilir
    val backgroundColor: Color
)

// Kategorileri tanımlayalım
val categoryList = listOf(
    Category("Hayvanlar", "https://cdn-icons-png.flaticon.com/512/1998/1998627.png", Color(0xFFFFCDD2)),
    Category("Sayılar", "https://cdn-icons-png.flaticon.com/512/109/109613.png", Color(0xFFC8E6C9)),
    Category("Renkler", "https://cdn-icons-png.flaticon.com/512/2682/2682065.png", Color(0xFFBBDEFB)),
    Category("Şekiller", "https://cdn-icons-png.flaticon.com/512/921/921490.png", Color(0xFFFFF176)),
    Category("Meyveler", "https://cdn-icons-png.flaticon.com/512/415/415682.png", Color(0xFFD1C4E9)),
    Category("Araçlar", "https://cdn-icons-png.flaticon.com/512/1995/1995539.png", Color(0xFFFFAB91)),
    Category("Kıyafetler", "https://cdn-icons-png.flaticon.com/512/892/892458.png", Color(0xFFA5D6A7))
)
