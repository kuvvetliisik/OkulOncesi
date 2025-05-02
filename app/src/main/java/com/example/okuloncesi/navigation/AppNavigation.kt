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
        composable("animalScreen") { AnimalScreen() }
        composable("numberScreen") { NumberScreen() }
        composable("colorScreen") { ColorScreen() }
        composable("shapeScreen") { ShapeScreen() }
        composable("fruitScreen") { FruitScreen() }
        composable("vehicleScreen") { VehicleScreen() }
        composable("clothesScreen") { ClothesScreen() }
    }
}
