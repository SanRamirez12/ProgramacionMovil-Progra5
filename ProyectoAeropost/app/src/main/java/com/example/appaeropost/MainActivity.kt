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
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

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
import com.example.appaeropost.ui.clientes.ClienteNuevoScreen
import com.example.appaeropost.ui.clientes.ClienteEditarScreen
import com.example.appaeropost.ui.paquetes.PaqueteNuevoScreen
import com.example.appaeropost.ui.paquetes.PaqueteEditarScreen

class MainActivity : ComponentActivity() { // Activity = “contenedor” de la app
    override fun onCreate(savedInstanceState: Bundle?) {

        // 1) Instalar splash (Android 12+ y compat)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2) Ya puedes hacer setContent con tu tema normal
        setContent {
            AppAeropostTheme {
                val navController = rememberNavController()

                // Ruta actual para decidir si mostramos la BottomBar
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val showBottomBar = bottomDestinations.any { it.route == currentRoute }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = Color(0xFF2F3136) // gris oscuro similar al de la app real
                            ) {
                                bottomDestinations.forEach { dest ->
                                    NavigationBarItem(
                                        selected = currentRoute == dest.route,
                                        onClick = {
                                            if (currentRoute != dest.route) {
                                                navController.navigate(dest.route) {
                                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = { dest.icon?.let { Icon(it, contentDescription = dest.label) } },
                                        label = { Text(dest.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.White,
                                            selectedTextColor = Color.White,
                                            unselectedIconColor = Color(0xFFBEC3CF),
                                            unselectedTextColor = Color(0xFFBEC3CF),
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { inner ->
                    // Grafo de navegación
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(inner)
                    ) {
                        // --------- Login ----------
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onLoginOk = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }


                        // --------- Tabs ----------
                        // Tab del home screen
                        composable(Screen.Home.route)        { HomeScreen(navController) }

                        //Tabs de clientes
                        composable(Screen.Clientes.route) {
                            ClientesScreen(
                                onNuevoClick = { navController.navigate(Screen.ClienteNuevo.route) },
                                onEditarClick = { id -> navController.navigate("clienteEditar/$id") }
                                // o: navController.navigate(clienteEditarRoute(id))
                            )
                        }

                        //Tabs paquetes
                        // Listado de Paquetes
                        // Listado de Paquetes
                        composable(Screen.Paquetes.route) {
                            PaquetesScreen(
                                onNuevoClick = { navController.navigate(Screen.PaqueteNuevo.route) },
                                onEditarClick = { id -> navController.navigate(Screen.PaqueteEditar.route(id)) } // <-- aquí
                            )
                        }


                        // Registrar nuevo paquete
                        composable(Screen.PaqueteNuevo.route) {
                            PaqueteNuevoScreen(
                                onCancel = { navController.popBackStack() },
                                onSaved  = { navController.popBackStack() }  // luego puedes disparar un refresh vía VM/SharedViewModel
                            )
                        }

                        // Editar paquete
                        composable(
                            route = Screen.PaqueteEditar.route,
                            arguments = listOf(navArgument(Screen.PaqueteEditar.ARG_ID) { type = NavType.StringType })
                        ) { backStackEntry ->
                            val paqueteId = backStackEntry.arguments?.getString(Screen.PaqueteEditar.ARG_ID).orEmpty()
                            PaqueteEditarScreen(
                                paqueteId = paqueteId,
                                onCancel = { navController.popBackStack() },
                                onSaved  = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.Facturacion.route) { FacturacionScreen() }
                        composable(Screen.More.route)        { MoreScreen(navController) }

                        // --------- Más ----------
                        composable(Screen.Usuarios.route)    { UsuariosScreen() }
                        composable(Screen.Bitacora.route)    { BitacoraScreen() }
                        composable(Screen.Tracking.route)    { TrackingScreen() }
                        composable(Screen.Reportes.route)    { ReportesScreen() }
                        composable(Screen.AcercaDe.route)    { AcercaDeScreen() }

                        // --------- Clientes: Nuevo / Editar ----------
                        composable(Screen.ClienteNuevo.route) {
                            ClienteNuevoScreen(onBack = { navController.popBackStack() })
                        }

                        composable(
                            route = Screen.ClienteEditar.route,
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id").orEmpty()
                            val idInt = id.toIntOrNull() ?: 0
                            ClienteEditarScreen(
                                clienteId = idInt,
                                onBack = { navController.popBackStack() }
                            )
                        }

                    }
                }
            }
        }
    }
}


