package com.example.appaeropostv2.presentation.common.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.appaeropostv2.data.local.db.AppDatabase
import com.example.appaeropostv2.data.repository.RepositoryUsuario
import com.example.appaeropostv2.presentation.home.HomeScreen
import com.example.appaeropostv2.presentation.login.LoginScreen
import com.example.appaeropostv2.presentation.usuario.CrearUsuarioScreen
import com.example.appaeropostv2.presentation.usuario.UsuarioScreen
import com.example.appaeropostv2.presentation.usuario.UsuarioViewModel
import com.example.appaeropostv2.presentation.usuario.UsuarioViewModelFactory


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // ---------- Login ----------
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
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

        // ---------- Usuarios ----------
        composable(Screen.Usuarios.route) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryUsuario(db.usuarioDao())

            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            val uiState by usuarioViewModel.uiState.collectAsState()

            UsuarioScreen(
                usuarios = uiState.usuarios,
                onIrCrearUsuario = { navController.navigate("usuarios/crear") },
                onIrDeshabilitarUsuario = { navController.navigate("usuarios/deshabilitar") },
                onEditarUsuario = { usuario ->
                    navController.navigate("usuarios/editar/${usuario.idUsuario}")
                },
                onVerDetallesUsuario = { usuario ->
                    navController.navigate("usuarios/detalles/${usuario.idUsuario}")
                }
            )
        }
        composable("usuarios/crear") {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryUsuario(db.usuarioDao())
            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            CrearUsuarioScreen(
                onGuardarUsuario = { usuario ->
                    usuarioViewModel.insertar(usuario)
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() }
            )
        }





        // ---------- Rutas placeholder de otros módulos ----------
        composable(Screen.AcercaDe.route)    { /* TODO: Acerca De */ }
        composable(Screen.Bitacora.route)    { /* TODO: Bitácora */ }
        composable(Screen.Clientes.route)    { /* TODO: Clientes */ }
        composable(Screen.Paquetes.route)    { /* TODO: Paquetes */ }
        composable(Screen.Facturacion.route) { /* TODO: Facturación */ }
        composable(Screen.Reportes.route)    { /* TODO: Reportes */ }
        composable(Screen.Tracking.route)    { /* TODO: Tracking */ }
    }
}





