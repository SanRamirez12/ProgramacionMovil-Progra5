package com.example.appaeropostv2.presentation.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Todas las rutas de la app (data objects)
sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    // ---------- Pre-App (sin BottomBar)
    data object Loading : Screen("loading", "Loading")
    data object Login : Screen("login", "Login")

    // ---------- Tabs de BottomBar
    data object Home    : Screen("home",    "Inicio",  Icons.Filled.Home)
    data object Search  : Screen("search",  "Buscar",  Icons.Filled.Search)
    data object Alerts  : Screen("alerts",  "Alertas", Icons.Filled.Notifications)
    data object Profile : Screen("profile", "Perfil",  Icons.Filled.Person)

    // ---------- Módulos navegables desde Home
    data object AcercaDe    : Screen("acercade",    "Acerca De")
    data object Usuarios    : Screen("usuarios",    "Usuarios")
    data object Bitacora    : Screen("bitacora",    "Bitácora")
    data object Clientes    : Screen("clientes",    "Clientes")
    data object Paquetes    : Screen("paquetes",    "Paquetes")
    data object Facturacion : Screen("facturacion", "Facturación")
    data object Reportes    : Screen("reportes",    "Reportes")
    data object Tracking    : Screen("tracking",    "Tracking")
}

val BottomBarScreens = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Alerts,
    Screen.Profile
)

