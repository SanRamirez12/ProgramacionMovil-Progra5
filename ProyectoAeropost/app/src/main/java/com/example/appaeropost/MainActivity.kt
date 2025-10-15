package com.example.appaeropost
// ↑ Paquete (namespace) donde vive esta clase).

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Rutas tipadas y pestañas inferiores
import com.example.appaeropost.navigation.Screen
import com.example.appaeropost.navigation.bottomDestinations

// Pantallas (modules)
import com.example.appaeropost.ui.login.LoginScreen
import com.example.appaeropost.ui.home.HomeScreen
import com.example.appaeropost.ui.clientes.ClientesScreen
import com.example.appaeropost.ui.paquetes.PaquetesScreen
import com.example.appaeropost.ui.facturacion.FacturacionScreen
import com.example.appaeropost.ui.more.MoreScreen
import com.example.appaeropost.ui.usuarios.UsuariosScreen
import com.example.appaeropost.ui.bitacora.BitacoraScreen
import com.example.appaeropost.ui.acercade.AcercaDeScreen
import com.example.appaeropost.ui.tracking.TrackingScreen
import com.example.appaeropost.ui.reportes.ReportesScreen
import com.example.appaeropost.ui.theme.AppAeropostTheme

class MainActivity : ComponentActivity() { // Activity = “contenedor” de la app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppAeropostTheme {
                val navController = rememberNavController()

                // Ruta actual para decidir si mostramos la BottomBar
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val showBottomBar = currentRoute != Screen.Login.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                bottomDestinations.forEach { dest ->
                                    NavigationBarItem(
                                        selected = currentRoute == dest.route,
                                        onClick = {
                                            if (currentRoute != dest.route) {
                                                navController.navigate(dest.route) {
                                                    // Volver al inicio del grafo y guardar/restaurar estado
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        // Solo las tabs tienen icon (en Screen el icon es nullable)
                                        icon = { dest.icon?.let { Icon(it, contentDescription = dest.label) } },
                                        label = { Text(dest.label) }
                                    )
                                }
                            }
                        }
                    }
                ) { inner ->
                    // Grafo de navegación
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route, // ← Login primero
                        modifier = Modifier.padding(inner)
                    ) {
                        // --------- Login (sin BottomBar) ----------
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onLoginOk = {
                                    // Ir a Home y quitar Login del back stack
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // --------- Tabs principales ----------
                        composable(Screen.Home.route)        { HomeScreen(navController) }
                        composable(Screen.Clientes.route)    { ClientesScreen() }
                        composable(Screen.Paquetes.route)    { PaquetesScreen() }
                        composable(Screen.Facturacion.route) { FacturacionScreen() }
                        composable(Screen.More.route)        { MoreScreen(navController) }

                        // --------- Destinos de “Más” ----------
                        composable(Screen.Usuarios.route)    { UsuariosScreen() }
                        composable(Screen.Bitacora.route)    { BitacoraScreen() }
                        composable(Screen.Tracking.route)    { TrackingScreen() }
                        composable(Screen.Reportes.route)    { ReportesScreen() }
                        composable(Screen.AcercaDe.route)    { AcercaDeScreen() }
                    }
                }
            }
        }
    }
}


