package com.example.appaeropost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Navegación
import com.example.appaeropost.navigation.Screen
import com.example.appaeropost.navigation.bottomDestinations

// Pantallas
import com.example.appaeropost.ui.acercade.AcercaDeScreen
import com.example.appaeropost.ui.bitacora.BitacoraScreen
import com.example.appaeropost.ui.facturacion.FacturacionEditarScreen
import com.example.appaeropost.ui.facturacion.FacturacionNuevaScreen
import com.example.appaeropost.ui.facturacion.FacturacionScreen
import com.example.appaeropost.ui.home.HomeScreen
import com.example.appaeropost.ui.login.LoginScreen
import com.example.appaeropost.ui.more.MoreScreen
import com.example.appaeropost.ui.paquetes.PaqueteEditarScreen
import com.example.appaeropost.ui.paquetes.PaqueteNuevoScreen
import com.example.appaeropost.ui.paquetes.PaquetesScreen
import com.example.appaeropost.ui.reportes.ReportesScreen
import com.example.appaeropost.ui.tracking.TrackingScreen
import com.example.appaeropost.ui.usuarios.UsuarioEditarScreen
import com.example.appaeropost.ui.usuarios.UsuarioNuevoScreen
import com.example.appaeropost.ui.usuarios.UsuariosScreen
import com.example.appaeropost.ui.clientes.ClienteEditarScreen
import com.example.appaeropost.ui.clientes.ClienteNuevoScreen
import com.example.appaeropost.ui.clientes.ClientesScreen
import com.example.appaeropost.ui.clientes.ClientesViewModel
import com.example.appaeropost.ui.theme.AppAeropostTheme

// ---------- Factory sencillo para el ViewModel de Clientes ----------
class ClientesVmFactory(
    private val repo: com.example.appaeropost.data.clientes.ClientesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ClientesViewModel(repo) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppAeropostTheme {
                val navController = rememberNavController()

                // --- Instancias para Clientes (repo + VM) ---
                val context = LocalContext.current
                val clientesRepo = remember {
                    com.example.appaeropost.data.config.ServiceLocator.clientesRepository(context)
                }
                val clientesVm: ClientesViewModel = viewModel(factory = ClientesVmFactory(clientesRepo))

                // Ruta actual para decidir si mostramos la BottomBar
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val showBottomBar = bottomDestinations.any { it.route == currentRoute }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(containerColor = Color(0xFF2F3136)) {
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
                        composable(Screen.Home.route) { HomeScreen(nav = navController) }
                        composable(Screen.More.route) { MoreScreen(nav = navController) }
                        composable(Screen.Bitacora.route) { BitacoraScreen() }
                        composable(Screen.Tracking.route) { TrackingScreen() }
                        composable(Screen.Reportes.route) { ReportesScreen() }
                        composable(Screen.AcercaDe.route) { AcercaDeScreen() }

                        // --------- Clientes ----------
                        composable(Screen.Clientes.route) {
                            ClientesScreen(
                                vm = clientesVm,
                                onNuevoClick = { navController.navigate(Screen.ClienteNuevo.route) },
                                onEditarClick = { id -> navController.navigate(Screen.ClienteEditar.route(id)) }
                            )
                        }
                        composable(Screen.ClienteNuevo.route) {
                            ClienteNuevoScreen(
                                vm = clientesVm,
                                onBack = { navController.popBackStack() },
                                onGuardado = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.ClienteEditar.route,
                            arguments = listOf(navArgument(Screen.ClienteEditar.ARG_ID) { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt(Screen.ClienteEditar.ARG_ID) ?: 0
                            ClienteEditarScreen(
                                clienteId = id,
                                vm = clientesVm,
                                onBack = { navController.popBackStack() },
                                onGuardado = { navController.popBackStack() }
                            )
                        }




                        // --------- Paquetes ----------
                        composable(Screen.Paquetes.route) {
                            PaquetesScreen(
                                onNuevoClick = { navController.navigate(Screen.PaqueteNuevo.route) },
                                onEditarClick = { id -> navController.navigate(Screen.PaqueteEditar.route(id)) }
                            )
                        }
                        composable(Screen.PaqueteNuevo.route) {
                            PaqueteNuevoScreen(
                                onCancel = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.PaqueteEditar.route,
                            arguments = listOf(navArgument(Screen.PaqueteEditar.ARG_ID) { type = NavType.StringType })
                        ) { backStackEntry ->
                            val paqueteId = backStackEntry.arguments?.getString(Screen.PaqueteEditar.ARG_ID).orEmpty()
                            PaqueteEditarScreen(
                                paqueteId = paqueteId,
                                onCancel = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
                        }

                        // --------- Facturación ----------
                        composable(Screen.Facturacion.route) {
                            FacturacionScreen(
                                onNuevoClick = { navController.navigate(Screen.FacturaNueva.route) },
                                onEditarClick = { id -> navController.navigate(Screen.FacturaEditar.route(id)) },
                                onFacturarPendientes = { /* TODO */ }
                            )
                        }
                        composable(Screen.FacturaNueva.route) {
                            FacturacionNuevaScreen(
                                onCancel = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.FacturaEditar.route,
                            arguments = listOf(navArgument(Screen.FacturaEditar.ARG_ID) { type = NavType.StringType })
                        ) { backStackEntry ->
                            val facturaId = backStackEntry.arguments?.getString(Screen.FacturaEditar.ARG_ID).orEmpty()
                            FacturacionEditarScreen(
                                facturaId = facturaId,
                                onCancel = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
                        }

                        // --------- Usuarios ----------
                        composable(Screen.Usuarios.route) {
                            UsuariosScreen(
                                onNuevoClick = { navController.navigate(Screen.UsuarioNuevo.route) },
                                onEditarClick = { username -> navController.navigate(Screen.UsuarioEditar.route(username)) }
                            )
                        }
                        composable(Screen.UsuarioNuevo.route) {
                            UsuarioNuevoScreen(
                                onCancel = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.UsuarioEditar.route,
                            arguments = listOf(navArgument(Screen.UsuarioEditar.ARG_USERNAME) { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString(Screen.UsuarioEditar.ARG_USERNAME).orEmpty()
                            UsuarioEditarScreen(
                                username = username,
                                onCancel = { navController.popBackStack() },
                                onSaved = { navController.popBackStack() }
                            )
                        }


                    }
                }
            }
        }
    }
}



