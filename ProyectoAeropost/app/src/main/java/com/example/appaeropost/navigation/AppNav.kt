package com.example.appaeropost.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// 1) Definimos las rutas de la app
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "Inicio", Icons.Filled.Home)
    data object Clientes : Screen("clientes", "Clientes", Icons.Filled.Person)
    data object Paquetes : Screen("paquetes", "Paquetes", Icons.Filled.Inventory2)
    data object Facturacion : Screen("facturacion", "Facturación", Icons.Filled.ReceiptLong)
    data object More : Screen("more", "Más", Icons.Filled.Settings)
}

// 2) Lista de pestañas que mostraremos en la BottomBar
val bottomDestinations = listOf(
    Screen.Home,
    Screen.Clientes,
    Screen.Paquetes,
    Screen.Facturacion,
    Screen.More
)
