package com.example.appaeropost

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
import com.example.appaeropost.navigation.Screen
import com.example.appaeropost.navigation.bottomDestinations
import com.example.appaeropost.ui.home.HomeScreen
import com.example.appaeropost.ui.clientes.ClientesScreen
import com.example.appaeropost.ui.paquetes.PaquetesScreen
import com.example.appaeropost.ui.facturacion.FacturacionScreen
import com.example.appaeropost.ui.more.MoreScreen
import com.example.appaeropost.ui.theme.AppAeropostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppAeropostTheme {
                // Controlador de navegación
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Bottom navigation persistente
                        NavigationBar {
                            // Saber cuál ruta está activa para marcar el tab seleccionado
                            val backStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = backStackEntry?.destination?.route

                            bottomDestinations.forEach { dest ->
                                NavigationBarItem(
                                    selected = currentRoute == dest.route,
                                    onClick = {
                                        // Evitar duplicar la misma ruta en el stack
                                        if (currentRoute != dest.route) {
                                            navController.navigate(dest.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(dest.icon, contentDescription = dest.label) },
                                    label = { Text(dest.label) }
                                )
                            }
                        }
                    }
                ) { inner ->
                    // Host de navegación: aquí declaramos qué Composable va con cada ruta
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(inner)
                    ) {
                        composable(Screen.Home.route) { HomeScreen() }
                        composable(Screen.Clientes.route) { ClientesScreen() }
                        composable(Screen.Paquetes.route) { PaquetesScreen() }
                        composable(Screen.Facturacion.route) { FacturacionScreen() }
                        composable(Screen.More.route) { MoreScreen() }
                    }
                }
            }
        }
    }
}
