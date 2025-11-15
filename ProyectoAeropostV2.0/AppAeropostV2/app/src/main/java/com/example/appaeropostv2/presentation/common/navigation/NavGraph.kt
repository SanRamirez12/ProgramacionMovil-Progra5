package com.example.appaeropostv2.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appaeropostv2.presentation.home.HomeScreen
import com.example.appaeropostv2.presentation.login.LoginScreen



@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,   // <--- ahora inicia en Login
        modifier = modifier
    ) {
        // ---------- Login ----------
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        // quitamos Login del back stack para que no se pueda volver atrás
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------- Home ----------
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenAcercaDe    = { navController.navigate(Screen.AcercaDe.route) },
                onOpenUsuarios    = { navController.navigate(Screen.Usuarios.route) },
                onOpenBitacora    = { navController.navigate(Screen.Bitacora.route) },
                onOpenClientes    = { navController.navigate(Screen.Clientes.route) },
                onOpenPaquetes    = { navController.navigate(Screen.Paquetes.route) },
                onOpenFacturacion = { navController.navigate(Screen.Facturacion.route) },
                onOpenReportes    = { navController.navigate(Screen.Reportes.route) },
                onOpenTracking    = { navController.navigate(Screen.Tracking.route) },
            )
        }

        // ---------- Rutas vacías por ahora ----------
        composable(Screen.AcercaDe.route)    { /* TODO: Acerca De */ }
        composable(Screen.Usuarios.route)    { /* TODO: Usuarios */ }
        composable(Screen.Bitacora.route)    { /* TODO: Bitácora */ }
        composable(Screen.Clientes.route)    { /* TODO: Clientes */ }
        composable(Screen.Paquetes.route)    { /* TODO: Paquetes */ }
        composable(Screen.Facturacion.route) { /* TODO: Facturación */ }
        composable(Screen.Reportes.route)    { /* TODO: Reportes */ }
        composable(Screen.Tracking.route)    { /* TODO: Tracking */ }

        // Futuro: Search, Alerts, Profile (si quieres rutas internas)
    }
}


