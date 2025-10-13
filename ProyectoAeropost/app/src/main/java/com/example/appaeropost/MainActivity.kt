package com.example.appaeropost
// ↑ Paquete (namespace) donde vive esta clase.

import android.os.Bundle
import androidx.activity.ComponentActivity          // Activity base “ligera” para apps Compose.
import androidx.activity.compose.setContent        // Permite definir la UI con Jetpack Compose.
import androidx.activity.enableEdgeToEdge         // Extiende la UI bajo status/navigation bars.
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*                // Componentes Material 3 (Scaffold, TopBar, etc.)
import androidx.compose.runtime.getValue           // Delegado 'by' para leer estados Compose.
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost         // Contenedor de destinos de navegación.
import androidx.navigation.compose.composable      // Conecta una ruta con un @Composable.
import androidx.navigation.compose.currentBackStackEntryAsState // Lee la ruta actual (reactivo).
import androidx.navigation.compose.rememberNavController        // Crea/recuerda un NavController.
import com.example.appaeropost.navigation.Screen  // Rutas tipadas (sealed class).
import com.example.appaeropost.navigation.bottomDestinations   // Lista de pestañas de la BottomBar.
import com.example.appaeropost.ui.home.HomeScreen
import com.example.appaeropost.ui.clientes.ClientesScreen
import com.example.appaeropost.ui.paquetes.PaquetesScreen
import com.example.appaeropost.ui.facturacion.FacturacionScreen
import com.example.appaeropost.ui.more.MoreScreen
import com.example.appaeropost.ui.theme.AppAeropostTheme       // Tema Material 3 de la app.

class MainActivity : ComponentActivity() {         // Una Activity = “contenedor” de pantalla.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()                         // Pinta contenido hasta los bordes del sistema.

        // Define la interfaz con Compose (reemplaza setContentView de XML).
        setContent {
            AppAeropostTheme {                     // Aplica colores/tipografías/forma (M3).
                // Controlador de navegación para cambiar de pantalla por rutas.
                val navController = rememberNavController()

                // Scaffold = layout base con slots (topBar, bottomBar, FAB…)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {                   // Slot de barra inferior persistente.
                        NavigationBar {             // Barra de navegación Material 3.
                            // Observa reactivamente la entrada del back stack
                            // para saber qué ruta está activa y marcar el tab.
                            val backStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = backStackEntry?.destination?.route

                            // Dibuja un item por cada destino de la BottomBar.
                            bottomDestinations.forEach { dest ->
                                NavigationBarItem(
                                    selected = currentRoute == dest.route, // ¿tab activo?
                                    onClick = {
                                        // Navega evitando duplicar la misma ruta en el stack
                                        // y preservando estado al volver a un tab previo.
                                        if (currentRoute != dest.route) {
                                            navController.navigate(dest.route) {
                                                // Vuelve al inicio del grafo guardando el estado
                                                // para restaurarlo cuando regreses a ese tab.
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true  // evita “apilar” la misma ruta
                                                restoreState = true     // restaura el estado guardado
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
                    // Contenido del Scaffold. 'inner' trae padding para no tapar la BottomBar.
                    // NavHost: mapea cada ruta -> Composable (pantalla) y gestiona el back stack.
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route, // Ruta inicial al abrir la app.
                        modifier = Modifier.padding(inner)    // Respeta el espacio de la BottomBar.
                    ) {
                        // Por cada ruta declaramos qué UI se muestra.
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

