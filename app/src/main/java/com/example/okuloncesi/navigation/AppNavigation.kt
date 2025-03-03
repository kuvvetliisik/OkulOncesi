package com.example.okuloncesi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.okuloncesi.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "categoryScreen") {
         composable("categoryScreen") { CategoryScreen(navController) }
        composable("animalScreen") { AnimalScreen(navController) }
        composable("numberScreen") { NumberScreen(navController) }
        composable("colorScreen") { ColorScreen(navController) }
        composable("shapeScreen") { ShapeScreen(navController) }
        composable("fruitScreen") { FruitScreen(navController) }
        composable("vehicleScreen") { VehicleScreen(navController) }
        composable("clothesScreen") { ClothesScreen(navController) }
    }
}
