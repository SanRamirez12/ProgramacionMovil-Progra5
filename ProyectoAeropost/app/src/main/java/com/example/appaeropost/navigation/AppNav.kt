package com.example.appaeropost.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Rutas tipadas
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    // Tabs principales (BottomBar)
    data object Home        : Screen("home", "Inicio",       Icons.Filled.Home)
    data object Clientes    : Screen("clientes", "Clientes", Icons.Filled.Person)
    data object Paquetes    : Screen("paquetes", "Paquetes", Icons.Filled.Inventory2)
    data object Facturacion : Screen("facturacion", "Facturación", Icons.Filled.ReceiptLong)
    data object More        : Screen("more", "Más", Icons.Filled.Menu)

    // Destinos “secundarios” (acceden desde Home o Más)
    data object Usuarios    : Screen("usuarios", "Usuarios", Icons.Filled.Group)
    data object Bitacora    : Screen("bitacora", "Bitácora", Icons.Filled.ListAlt)
    data object AcercaDe    : Screen("acercade", "Acerca de", Icons.Filled.Info)
    data object Tracking    : Screen("tracking", "Tracking", Icons.Filled.Map)
    data object Analisis    : Screen("analisis", "Análisis", Icons.Filled.Insights)
    data object Reportes    : Screen("reportes", "Reportes", Icons.Filled.Assessment)
}

// Pestañas de la BottomBar (5 máx es buena práctica)
val bottomDestinations = listOf(
    Screen.Home, Screen.Clientes, Screen.Paquetes, Screen.Facturacion, Screen.More
)

