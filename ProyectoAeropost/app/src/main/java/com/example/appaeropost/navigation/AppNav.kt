package com.example.appaeropost.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Todas las rutas de la app
sealed class Screen(val route: String, val label: String, val icon: ImageVector? = null) {

    // ——— Pre-App (sin BottomBar)
    data object Login       : Screen("login", "Login")

    // ——— Pestañas principales (BottomBar)
    data object Home        : Screen("home",        "Inicio",       Icons.Filled.Home)
    data object Clientes    : Screen("clientes",    "Clientes",     Icons.Filled.Person)
    data object Paquetes    : Screen("paquetes",    "Paquetes",     Icons.Filled.Inventory2)
    data object Facturacion : Screen("facturacion", "Facturación",  Icons.Filled.ReceiptLong)
    data object More        : Screen("more",        "Más",          Icons.Filled.Menu)

    // ——— Destinos dentro de “Más”
    data object Usuarios    : Screen("usuarios",    "Usuarios",     Icons.Filled.Group)
    data object Bitacora    : Screen("bitacora",    "Bitácora",     Icons.Filled.ListAlt)
    data object Tracking    : Screen("tracking",    "Tracking",     Icons.Filled.Map)
    data object Reportes    : Screen("reportes",    "Reportes",     Icons.Filled.Assessment)
    data object AcercaDe    : Screen("acercade",    "Acerca de",    Icons.Filled.Info)
}

// Pestañas de la BottomBar (las 5 principales)
val bottomDestinations = listOf(
    Screen.Home, Screen.Clientes, Screen.Paquetes, Screen.Facturacion, Screen.More
)


