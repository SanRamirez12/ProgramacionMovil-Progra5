package com.example.appaeropostv2.presentation.common.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.example.appaeropostv2.presentation.bottonbar_views.home.HomeScreen
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
import com.example.appaeropostv2.data.repository.RepositoryCliente
import com.example.appaeropostv2.data.repository.RepositoryPaquete
import com.example.appaeropostv2.data.repository.RepositoryFacturacion
import com.example.appaeropostv2.presentation.bitacora.BitacoraScreen
import com.example.appaeropostv2.presentation.bitacora.BitacoraViewModel
import com.example.appaeropostv2.presentation.bitacora.BitacoraViewModelFactory
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.presentation.clientes.ClienteViewModel
import com.example.appaeropostv2.presentation.clientes.ClienteViewModelFactory
import com.example.appaeropostv2.presentation.clientes.ClienteScreen
import com.example.appaeropostv2.presentation.clientes.CrearClienteScreen
import com.example.appaeropostv2.presentation.clientes.EditarClienteScreen
import com.example.appaeropostv2.presentation.clientes.DeshabilitarClienteScreen
import com.example.appaeropostv2.presentation.clientes.DetallesClienteScreen
import com.example.appaeropostv2.presentation.login.LoginViewModel
import com.example.appaeropostv2.presentation.login.LoginViewModelFactory
import com.example.appaeropostv2.presentation.login.RegistrarUsuarioScreen
import com.example.appaeropostv2.presentation.paquete.CrearPaqueteScreen
import com.example.appaeropostv2.presentation.paquete.DetallesPaqueteScreen
import com.example.appaeropostv2.presentation.paquete.EditarPaqueteScreen
import com.example.appaeropostv2.presentation.paquete.EliminarPaqueteScreen
import com.example.appaeropostv2.presentation.paquete.PaqueteScreen
import com.example.appaeropostv2.presentation.paquete.PaqueteViewModel
import com.example.appaeropostv2.presentation.paquete.PaqueteViewModelFactory
import com.example.appaeropostv2.data.pdf.FacturaPdfGenerator
import com.example.appaeropostv2.presentation.facturacion.FacturacionScreen
import com.example.appaeropostv2.presentation.facturacion.CrearFacturacionScreen
import com.example.appaeropostv2.presentation.facturacion.DetallesFacturaScreen
import com.example.appaeropostv2.presentation.facturacion.EliminarFacturaScreen
import com.example.appaeropostv2.presentation.facturacion.FacturacionViewModel
import com.example.appaeropostv2.presentation.facturacion.FacturacionViewModelFactory
import com.example.appaeropostv2.data.security.RepositorySecurityHash
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.example.appaeropostv2.presentation.loadingscreen.LoadingScreen
import com.example.appaeropostv2.presentation.acercade.AcercaDeScreen
import com.example.appaeropostv2.presentation.acercade.AcercaDeViewModel
import com.example.appaeropostv2.presentation.acercade.AcercaDeViewModelFactory


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Loading.route,
        modifier = modifier
    ) {
        // ---------- Loading ----------
        composable(Screen.Loading.route) {
            LoadingScreen()

            LaunchedEffect(Unit) {
                delay(1600) // duración del loading (ajustable)
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Loading.route) { inclusive = true }
                }
            }
        }
        // ---------- Login ----------
        composable(Screen.Login.route) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            // Repos con seguridad de contraseñas
            val securityHash = remember { RepositorySecurityHash() }
            val repoUsuario = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }
            val repoBitacora = remember { RepositoryBitacora(db.bitacoraDao()) }

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(repoUsuario, repoBitacora)
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("login/registrar")
                }
            )
        }

        // ---------- Registro de usuario desde Login ----------
        composable("login/registrar") {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val securityHash = remember { RepositorySecurityHash() }
            val repo = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }

            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            RegistrarUsuarioScreen(
                onGuardarUsuario = { usuario, plainPassword ->
                    usuarioViewModel.insertar(usuario, plainPassword)
                    // Guardado en BD y regreso a la pantalla de login
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // ---------- Acerca de ----------
        composable(Screen.AcercaDe.route) {
            val context = LocalContext.current
            val acercaDeViewModel: AcercaDeViewModel = viewModel(
                factory = AcercaDeViewModelFactory(context.applicationContext as android.app.Application)
            )

            AcercaDeScreen(viewModel = acercaDeViewModel)
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

            val securityHash = remember { RepositorySecurityHash() }
            val repo = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }

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

            val securityHash = remember { RepositorySecurityHash() }
            val repo = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }

            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(repo)
            )

            CrearUsuarioScreen(
                onGuardarUsuario = { usuario, plainPassword ->
                    usuarioViewModel.insertar(usuario, plainPassword)
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // ---------- Usuarios: editar ----------
        composable("usuarios/editar/{idUsuario}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val securityHash = remember { RepositorySecurityHash() }
            val repo = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }

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

            val securityHash = remember { RepositorySecurityHash() }
            val repo = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }

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

            val securityHash = remember { RepositorySecurityHash() }
            val repo = remember { RepositoryUsuario(db.usuarioDao(), securityHash) }

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

        // ---------- Clientes: listado ----------
        composable(Screen.Clientes.route) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryCliente(db.clienteDao())

            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repo)
            )

            val uiState by clienteViewModel.uiState.collectAsState()

            ClienteScreen(
                clientes = uiState.clientes,
                onIrCrearCliente = { navController.navigate("clientes/crear") },
                onEditarCliente = { cliente ->
                    navController.navigate("clientes/editar/${cliente.idCliente}")
                },
                onVerDetallesCliente = { cliente ->
                    navController.navigate("clientes/detalles/${cliente.idCliente}")
                },
                onDeshabilitarCliente = { cliente ->
                    navController.navigate("clientes/deshabilitar/${cliente.idCliente}")
                }
            )
        }

        // ---------- Clientes: crear ----------
        composable("clientes/crear") {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryCliente(db.clienteDao())
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repo)
            )

            CrearClienteScreen(
                onGuardarCliente = { cliente ->
                    clienteViewModel.insertar(cliente)
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // ---------- Clientes: editar ----------
        composable("clientes/editar/{idCliente}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryCliente(db.clienteDao())
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repo)
            )

            val id = backStackEntry.arguments?.getString("idCliente")?.toIntOrNull()

            if (id == null) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                var cliente by remember { mutableStateOf<Cliente?>(null) }

                LaunchedEffect(id) {
                    cliente = clienteViewModel.obtenerPorId(id)
                }

                if (cliente == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    EditarClienteScreen(
                        clienteOriginal = cliente!!,
                        onGuardarCambios = { actualizado ->
                            clienteViewModel.actualizar(actualizado)
                            navController.popBackStack()
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }

        // ---------- Clientes: habilitar/deshabilitar ----------
        composable("clientes/deshabilitar/{idCliente}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryCliente(db.clienteDao())
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repo)
            )

            val id = backStackEntry.arguments?.getString("idCliente")?.toIntOrNull()

            if (id == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                var cliente by remember { mutableStateOf<Cliente?>(null) }

                LaunchedEffect(id) {
                    cliente = clienteViewModel.obtenerPorId(id)
                }

                if (cliente == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DeshabilitarClienteScreen(
                        cliente = cliente!!,
                        onConfirmar = {
                            clienteViewModel.cambiarEstadoCliente(cliente!!.idCliente)
                            navController.popBackStack()
                        },
                        onCancelar = { navController.popBackStack() }
                    )
                }
            }
        }

        // ---------- Clientes: detalles ----------
        composable("clientes/detalles/{idCliente}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = RepositoryCliente(db.clienteDao())
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repo)
            )

            val id = backStackEntry.arguments?.getString("idCliente")?.toIntOrNull()

            if (id == null) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                var cliente by remember { mutableStateOf<Cliente?>(null) }

                LaunchedEffect(id) {
                    cliente = clienteViewModel.obtenerPorId(id)
                }

                if (cliente == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetallesClienteScreen(
                        cliente = cliente!!,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }

        // ───────────────────────── Paquetes ─────────────────────────
        // Paquetes: listado
        composable(Screen.Paquetes.route) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val repoCliente = RepositoryCliente(db.clienteDao())

            val paqueteViewModel: PaqueteViewModel = viewModel(
                factory = PaqueteViewModelFactory(repoPaquete)
            )
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repoCliente)
            )

            val uiState by paqueteViewModel.uiState.collectAsState()

            PaqueteScreen(
                uiState = uiState,
                onIrCrearPaquete = { navController.navigate("paquetes/crear") },
                onEditarPaquete = { paquete ->
                    navController.navigate("paquetes/editar/${paquete.idPaquete}")
                },
                onVerDetallesPaquete = { paquete ->
                    navController.navigate("paquetes/detalles/${paquete.idPaquete}")
                },
                onEliminarPaquete = { paquete ->
                    navController.navigate("paquetes/eliminar/${paquete.idPaquete}")
                },
                onActualizarBusqueda = { texto ->
                    paqueteViewModel.actualizarBusqueda(texto)
                },
                onActualizarFechaDesde = { fecha ->
                    paqueteViewModel.actualizarFechaDesde(fecha)
                },
                onActualizarFechaHasta = { fecha ->
                    paqueteViewModel.actualizarFechaHasta(fecha)
                },
                onLimpiarFechas = {
                    paqueteViewModel.limpiarFechas()
                }
            )
        }

        // Paquetes: crear
        composable("paquetes/crear") {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val repoCliente = RepositoryCliente(db.clienteDao())

            val paqueteViewModel: PaqueteViewModel = viewModel(
                factory = PaqueteViewModelFactory(repoPaquete)
            )
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repoCliente)
            )

            val clientesState by clienteViewModel.uiState.collectAsState()
            val clientes = clientesState.clientes

            CrearPaqueteScreen(
                clientes = clientes,
                onGuardarPaquete = { paquete ->
                    paqueteViewModel.insertar(paquete)
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // Paquetes: editar
        composable("paquetes/editar/{idPaquete}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val repoCliente = RepositoryCliente(db.clienteDao())

            val paqueteViewModel: PaqueteViewModel = viewModel(
                factory = PaqueteViewModelFactory(repoPaquete)
            )
            val clienteViewModel: ClienteViewModel = viewModel(
                factory = ClienteViewModelFactory(repoCliente)
            )

            val clientesState by clienteViewModel.uiState.collectAsState()
            val clientes = clientesState.clientes

            val idPaquete = backStackEntry.arguments?.getString("idPaquete")

            if (idPaquete == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                var paquete by remember { mutableStateOf<Paquete?>(null) }

                LaunchedEffect(idPaquete) {
                    paquete = paqueteViewModel.obtenerPorId(idPaquete)
                }

                if (paquete == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    EditarPaqueteScreen(
                        paqueteOriginal = paquete!!,
                        clientes = clientes,
                        onGuardarCambios = { actualizado ->
                            paqueteViewModel.actualizar(actualizado)
                            navController.popBackStack()
                        },
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }

        // Paquetes: detalles
        composable("paquetes/detalles/{idPaquete}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoPaquete = RepositoryPaquete(db.paqueteDao())

            val paqueteViewModel: PaqueteViewModel = viewModel(
                factory = PaqueteViewModelFactory(repoPaquete)
            )

            val idPaquete = backStackEntry.arguments?.getString("idPaquete")

            if (idPaquete == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                var paquete by remember { mutableStateOf<Paquete?>(null) }

                LaunchedEffect(idPaquete) {
                    paquete = paqueteViewModel.obtenerPorId(idPaquete)
                }

                if (paquete == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    DetallesPaqueteScreen(
                        paquete = paquete!!,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
        }

        // Paquetes: eliminar
        composable("paquetes/eliminar/{idPaquete}") { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoPaquete = RepositoryPaquete(db.paqueteDao())

            val paqueteViewModel: PaqueteViewModel = viewModel(
                factory = PaqueteViewModelFactory(repoPaquete)
            )

            val idPaquete = backStackEntry.arguments?.getString("idPaquete")

            if (idPaquete == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                var paquete by remember { mutableStateOf<Paquete?>(null) }

                LaunchedEffect(idPaquete) {
                    paquete = paqueteViewModel.obtenerPorId(idPaquete)
                }

                if (paquete == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    EliminarPaqueteScreen(
                        paquete = paquete!!,
                        onConfirmar = {
                            paqueteViewModel.eliminar(paquete!!)
                            navController.popBackStack()
                        },
                        onCancelar = { navController.popBackStack() }
                    )
                }
            }
        }

        // ───────────────────────── Facturación ─────────────────────────
        // Listado de facturas
        composable(Screen.Facturacion.route) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoFact = RepositoryFacturacion(db.facturacionDao())
            val repoCliente = RepositoryCliente(db.clienteDao())
            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val pdfGenerator = remember { FacturaPdfGenerator(context) }

            val factViewModel: FacturacionViewModel = viewModel(
                factory = FacturacionViewModelFactory(
                    repoFacturacion = repoFact,
                    repoCliente = repoCliente,
                    repoPaquete = repoPaquete,
                    pdfGenerator = pdfGenerator
                )
            )

            val uiState by factViewModel.ui.collectAsState()

            FacturacionScreen(
                uiState = uiState,
                onIrCrearFactura = { navController.navigate("facturacion/crear") },
                onVerDetalleFactura = { factura ->
                    navController.navigate("facturacion/detalles/${factura.idFacturacion}")
                },
                onEliminarFactura = { factura ->
                    navController.navigate("facturacion/eliminar/${factura.idFacturacion}")
                },
                onActualizarBusqueda = factViewModel::actualizarBusqueda,
                onActualizarFechaDesde = factViewModel::actualizarFechaDesde,
                onActualizarFechaHasta = factViewModel::actualizarFechaHasta,
                onLimpiarFechas = factViewModel::limpiarFechas
            )
        }

        // Crear factura
        composable("facturacion/crear") {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoFact = RepositoryFacturacion(db.facturacionDao())
            val repoCliente = RepositoryCliente(db.clienteDao())
            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val pdfGenerator = remember { FacturaPdfGenerator(context) }

            val factViewModel: FacturacionViewModel = viewModel(
                factory = FacturacionViewModelFactory(
                    repoFacturacion = repoFact,
                    repoCliente = repoCliente,
                    repoPaquete = repoPaquete,
                    pdfGenerator = pdfGenerator
                )
            )

            val uiState by factViewModel.ui.collectAsState()

            CrearFacturacionScreen(
                uiState = uiState,
                onActualizarFechaFacturacion = factViewModel::actualizarFechaFacturacion,
                onActualizarCedula = factViewModel::actualizarCedula,
                onSeleccionarPaquete = factViewModel::seleccionarPaquete,
                onCargarCliente = factViewModel::cargarCliente,
                onGenerarFactura = {
                    factViewModel.generarFactura()
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() },
                onConsumirError = factViewModel::limpiarError,
                onConsumirPdf = factViewModel::limpiarPdfUri
            )
        }

        // Detalles factura
        composable("facturacion/detalles/{idFactura}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("idFactura")?.toIntOrNull()

            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoFact = RepositoryFacturacion(db.facturacionDao())
            val repoCliente = RepositoryCliente(db.clienteDao())
            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val pdfGenerator = remember { FacturaPdfGenerator(context) }

            val factViewModel: FacturacionViewModel = viewModel(
                factory = FacturacionViewModelFactory(
                    repoFacturacion = repoFact,
                    repoCliente = repoCliente,
                    repoPaquete = repoPaquete,
                    pdfGenerator = pdfGenerator
                )
            )

            val uiState by factViewModel.ui.collectAsState()

            val factura = uiState.facturas.firstOrNull { it.idFacturacion == id }

            if (factura == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Factura no encontrada")
                }
            } else {
                DetallesFacturaScreen(
                    factura = factura,
                    uiState = uiState,
                    onGenerarPdfDesdeDetalle = {
                        factViewModel.generarPdfParaFacturaExistente(factura)
                    },
                    onConsumirPdf = factViewModel::limpiarPdfUri,
                    onConsumirError = factViewModel::limpiarError,
                    onVolver = { navController.popBackStack() }
                )
            }
        }

        // Eliminar factura
        composable("facturacion/eliminar/{idFactura}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("idFactura")?.toIntOrNull()

            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)

            val repoFact = RepositoryFacturacion(db.facturacionDao())
            val repoCliente = RepositoryCliente(db.clienteDao())
            val repoPaquete = RepositoryPaquete(db.paqueteDao())
            val pdfGenerator = remember { FacturaPdfGenerator(context) }

            val factViewModel: FacturacionViewModel = viewModel(
                factory = FacturacionViewModelFactory(
                    repoFacturacion = repoFact,
                    repoCliente = repoCliente,
                    repoPaquete = repoPaquete,
                    pdfGenerator = pdfGenerator
                )
            )

            val uiState by factViewModel.ui.collectAsState()

            val factura = uiState.facturas.firstOrNull { it.idFacturacion == id }

            if (factura == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Factura no encontrada")
                }
            } else {
                EliminarFacturaScreen(
                    factura = factura,
                    onConfirmarEliminar = {
                        factViewModel.eliminarFactura(factura)
                        navController.popBackStack()
                    },
                    onCancelar = { navController.popBackStack() }
                )
            }
        }

        // ---------- Rutas placeholder de otros módulos ----------
        composable(Screen.Reportes.route)    { /* TODO: Reportes */ }
        composable(Screen.Tracking.route)    { /* TODO: Tracking */ }
    }
}
