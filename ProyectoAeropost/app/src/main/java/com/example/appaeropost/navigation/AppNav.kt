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

    //——— Destinos dentro de “Clientes”
    data object ClienteNuevo  : Screen("clienteNuevo", "Nuevo cliente")
    data object ClienteEditar : Screen("clienteEditar/{id}", "Editar cliente") {
        const val ARG_ID = "id"
        fun route(id: Int) = "clienteEditar/$id"
    }

    // ——— Destinos dentro de “Paquetes”
    data object PaqueteNuevo  : Screen("paqueteNuevo", "Registrar paquete")
    data object PaqueteEditar : Screen("paqueteEditar/{id}", "Editar paquete") {
        const val ARG_ID = "id"
        fun route(id: String) = "paqueteEditar/$id"
    }

    // ——— Paquetes (subrutas) — fuera de BottomBar
    data object PaquetesCancelados : Screen("paquetes/cancelados", "Cancelados", Icons.Filled.Inventory2)

    data object PaquetesCancelar : Screen("paquetes/cancelar/{id}", "Cancelar", Icons.Filled.Inventory2) {
        const val ARG_ID = "id"
        fun route(id: String) = "paquetes/cancelar/$id"
    }

    // ——— Destinos dentro de “Facturación”
    data object FacturaNueva  : Screen("facturaNueva", "Nueva factura")
    data object FacturaEditar : Screen("facturaEditar/{id}", "Editar factura") {
        const val ARG_ID = "id"
        fun route(id: String) = "facturaEditar/$id"
    }

    // ——— Destinos dentro de “Usuarios”
    data object UsuarioNuevo  : Screen("usuarioNuevo", "Nuevo usuario")
    data object UsuarioEditar : Screen("usuarioEditar/{username}", "Editar usuario") {
        const val ARG_USERNAME = "username"
        fun route(username: String) = "usuarioEditar/$username"
    }
}

// Pestañas de la BottomBar (las 5 principales)
val bottomDestinations = listOf(
    Screen.Home, Screen.Clientes, Screen.Paquetes, Screen.Facturacion, Screen.More
)
