package com.example.appaeropost.ui.usuarios

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.appaeropost.data.config.ServiceLocator
import com.example.appaeropost.domain.Usuario
import com.example.appaeropost.domain.EstadoUsuario
import java.time.LocalDate

@Composable
fun UsuarioNuevoScreen(
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { ServiceLocator.usuariosRepository(context) }

    val snackHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val initial = remember {
        UsuarioFormState(
            fechaRegistro = LocalDate.now(),
            estadoUsuario = EstadoUsuario.HABILITADO
        )
    }

    UsuarioForm(
        initial = initial,
        isEdit = false,
        onCancel = onCancel,
        onSubmit = { form ->
            val usuario = Usuario(
                nombre = form.nombre,
                cedula = form.cedula,
                genero = form.genero,
                fechaRegistro = form.fechaRegistro,
                estadoUsuario = form.estadoUsuario,
                rol = form.rol,
                correo = form.correo,
                username = form.username,
                password = form.password
            )
            scope.launch {
                val result = repo.crear(usuario)
                if (result.isSuccess) {
                    onSaved()
                } else {
                    snackHost.showSnackbar(result.exceptionOrNull()?.message ?: "No se pudo crear")
                }
            }
        }
    )

    SnackbarHost(hostState = snackHost)
}
