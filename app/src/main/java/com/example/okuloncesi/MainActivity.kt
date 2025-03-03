package com.example.okuloncesi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.okuloncesi.ui.theme.OkulOncesiTheme
import androidx.navigation.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.okuloncesi.screens.CategoryScreen
import com.example.okuloncesi.screens.CategoryDetailScreen
import com.example.okuloncesi.screens.LearningScreen
import com.example.okuloncesi.screens.TestScreen
import com.example.okuloncesi.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "categoryScreen") {
        composable("categoryScreen") { CategoryScreen(navController) }
        composable("category/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryDetailScreen(navController, categoryName)
        }
        composable("learning/{category}/{index}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0
            LearningScreen(navController, category, index)
        }
        composable("test/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            TestScreen(navController, category)
        }
    }
}