package com.example.appaeropost.ui.usuarios

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.appaeropost.data.config.ServiceLocator
import com.example.appaeropost.domain.Usuario

@Composable
fun UsuarioEditarScreen(
    username: String,
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { ServiceLocator.usuariosRepository(context) }

    val snackHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var loaded by remember { mutableStateOf<Usuario?>(null) }
    LaunchedEffect(username) {
        loaded = repo.obtenerPorUsername(username)
    }

    val usuario = loaded
    if (usuario == null) {
        SnackbarHost(hostState = snackHost)
        return
    }

    val initial = UsuarioFormState(
        nombre = usuario.nombre,
        cedula = usuario.cedula,
        genero = usuario.genero,
        fechaRegistro = usuario.fechaRegistro,
        estadoUsuario = usuario.estadoUsuario,
        rol = usuario.rol,
        correo = usuario.correo,
        username = usuario.username,
        password = "",
        confirmarPassword = ""
    )

    UsuarioForm(
        initial = initial,
        isEdit = true,
        usernameEditable = false,
        onCancel = onCancel,
        onSubmit = { form ->
            scope.launch {
                val result = repo.actualizar(username) { u ->
                    u.copy(
                        nombre = form.nombre,
                        cedula = form.cedula,
                        genero = form.genero,
                        fechaRegistro = form.fechaRegistro,
                        estadoUsuario = form.estadoUsuario,
                        rol = form.rol,
                        correo = form.correo,
                        password = if (form.password.isNotBlank()) form.password else u.password
                    )
                }
                if (result.isSuccess) {
                    onSaved()
                } else {
                    snackHost.showSnackbar(result.exceptionOrNull()?.message ?: "No se pudo actualizar")
                }
            }
        }
    )

    SnackbarHost(hostState = snackHost)
}


