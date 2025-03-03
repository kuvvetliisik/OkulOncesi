package com.example.okuloncesi.data

import androidx.compose.ui.graphics.Color
import com.example.okuloncesi.R



data class Category(
    val name: String,
    val imageRes: Int,
    val backgroundColor: Color
)

// Kategorileri tanımlayalım
val categoryList = listOf(
    Category("Hayvanlar", R.drawable.hayvanlarr, Color(0xFFFFCDD2)),
    Category("Sayılar", R.drawable.sayilar, Color(0xFFC8E6C9)),
    Category("Renkler", R.drawable.renkler, Color(0xFFBBDEFB)),
    Category("Şekiller", R.drawable.sekiller, Color(0xFFFFF176)),
    Category("Meyveler", R.drawable.meyveler, Color(0xFFD1C4E9)),
    Category("Araçlar", R.drawable.araclar, Color(0xFFFFAB91)),
    Category("Kıyafetler", R.drawable.kiyafetler, Color(0xFFA5D6A7))
)
