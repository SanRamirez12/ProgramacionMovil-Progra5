package com.example.appaeropostv2.presentation.common.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appaeropostv2.data.local.db.AppDatabase
import com.example.appaeropostv2.data.repository.RepositoryUsuario
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.presentation.home.HomeScreen
import com.example.appaeropostv2.presentation.login.LoginScreen
import com.example.appaeropostv2.presentation.usuario.CrearUsuarioScreen
import com.example.appaeropostv2.presentation.usuario.DeshabilitarUsuarioScreen
import com.example.appaeropostv2.presentation.usuario.DetallesUsuarioScreen
import com.example.appaeropostv2.presentation.usuario.EditarUsuarioScreen
import com.example.appaeropostv2.presentation.usuario.UsuarioScreen
import com.example.appaeropostv2.presentation.usuario.UsuarioViewModel
import com.example.appaeropostv2.presentation.usuario.UsuarioViewModelFactory
import com.example.appaeropostv2.core.session.SessionManager
import com.example.appaeropostv2.data.repository.RepositoryBitacora
import com.example.appaeropostv2.presentation.bitacora.BitacoraScreen
import com.example.appaeropostv2.presentation.bitacora.BitacoraViewModel
import com.example.appaeropostv2.presentation.bitacora.BitacoraViewModelFactory
import com.example.appaeropostv2.presentation.login.LoginViewModel
import com.example.appaeropostv2.presentation.login.LoginViewModelFactory

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
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoUsuario = RepositoryUsuario(db.usuarioDao())
            val repoBitacora = RepositoryBitacora(db.bitacoraDao())

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(repoUsuario, repoBitacora)
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------- Home ----------
        composable(Screen.Home.route) {
            val currentUser by SessionManager.currentUser.collectAsState()

            HomeScreen(
                currentUser = currentUser,
                onOpenAcercaDe = { navController.navigate(Screen.AcercaDe.route) },
                onOpenUsuarios = { navController.navigate(Screen.Usuarios.route) },
                onOpenBitacora = { navController.navigate(Screen.Bitacora.route) },
                onOpenClientes = { navController.navigate(Screen.Clientes.route) },
                onOpenPaquetes = { navController.navigate(Screen.Paquetes.route) },
                onOpenFacturacion = { navController.navigate(Screen.Facturacion.route) },
                onOpenReportes = { navController.navigate(Screen.Reportes.route) },
                onOpenTracking = { navController.navigate(Screen.Tracking.route) }
            )
        }

        // ---------- Usuarios: listado ----------
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
                onEditarUsuario = { usuario ->
                    navController.navigate("usuarios/editar/${usuario.idUsuario}")
                },
                onVerDetallesUsuario = { usuario ->
                    navController.navigate("usuarios/detalles/${usuario.idUsuario}")
                },
                onDeshabilitarUsuario = { usuario ->
                    navController.navigate("usuarios/deshabilitar/${usuario.idUsuario}")
                }
            )
        }

        // ---------- Usuarios: crear ----------
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

        // ---------- Usuarios: editar ----------
        composable("usuarios/editar/{idUsuario}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryUsuario(db.usuarioDao())
            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            val id = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()

            if (id == null) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                var usuario by remember { mutableStateOf<Usuario?>(null) }

                LaunchedEffect(id) {
                    usuario = usuarioViewModel.obtenerPorId(id)
                }

                if (usuario == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    EditarUsuarioScreen(
                        usuarioOriginal = usuario!!,
                        onGuardarCambios = { actualizado ->
                            usuarioViewModel.actualizar(actualizado)
                            navController.popBackStack()
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }

        // ---------- Usuarios: habilitar/deshabilitar ----------
        composable("usuarios/deshabilitar/{idUsuario}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryUsuario(db.usuarioDao())
            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            val id = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()

            if (id == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                var usuario by remember { mutableStateOf<Usuario?>(null) }

                LaunchedEffect(id) {
                    usuario = usuarioViewModel.obtenerPorId(id)
                }

                if (usuario == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val esHabilitado = usuario!!.estadoUsuario == Estados.HABILITADO

                    DeshabilitarUsuarioScreen(
                        usuario = usuario!!,
                        onConfirmar = {
                            usuarioViewModel.cambiarEstadoUsuario(usuario!!.idUsuario)
                            navController.popBackStack()
                        },
                        onCancelar = { navController.popBackStack() }
                    )
                }
            }
        }


        // ---------- Usuarios: detalles ----------
        composable("usuarios/detalles/{idUsuario}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryUsuario(db.usuarioDao())
            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            val id = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()

            if (id == null) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                var usuario by remember { mutableStateOf<Usuario?>(null) }

                LaunchedEffect(id) {
                    usuario = usuarioViewModel.obtenerPorId(id)
                }

                if (usuario == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetallesUsuarioScreen(
                        usuario = usuario!!,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }

        // ───────────────────────── Bitácora ─────────────────────────
        composable(Screen.Bitacora.route) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repoBitacora = RepositoryBitacora(db.bitacoraDao())

            val bitacoraViewModel: BitacoraViewModel = viewModel(
                factory = BitacoraViewModelFactory(repoBitacora)
            )

            val uiState by bitacoraViewModel.uiState.collectAsState()

            BitacoraScreen(
                uiState = uiState
            )
        }


        // ---------- Rutas placeholder de otros módulos ----------
        composable(Screen.AcercaDe.route)    { /* TODO: Acerca De */ }
        composable(Screen.Clientes.route)    { /* TODO: Clientes */ }
        composable(Screen.Paquetes.route)    { /* TODO: Paquetes */ }
        composable(Screen.Facturacion.route) { /* TODO: Facturación */ }
        composable(Screen.Reportes.route)    { /* TODO: Reportes */ }
        composable(Screen.Tracking.route)    { /* TODO: Tracking */ }
    }
}





