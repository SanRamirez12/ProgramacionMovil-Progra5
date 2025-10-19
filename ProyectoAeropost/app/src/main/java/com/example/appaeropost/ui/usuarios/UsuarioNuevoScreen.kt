package com.example.appaeropost.ui.usuarios

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import com.example.appaeropost.data.repository.FakeUsuariosRepository
import com.example.appaeropost.data.repository.UsuariosRepository
import com.example.appaeropost.domain.Usuario
import com.example.appaeropost.domain.EstadoUsuario
import java.time.LocalDate

@Composable
fun UsuarioNuevoScreen(
    repo: UsuariosRepository = FakeUsuariosRepository(),
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {
    val snackHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val initial = remember {
        UsuarioFormState(
            fechaRegistro = LocalDate.now(),
            estado = EstadoUsuario.HABILITADO
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
                estado = form.estado,
                rol = form.rol,
                correo = form.correo,
                username = form.username,
                password = form.password
            )
            // Lanzar corrutina desde callback
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

    // Muestra el host (si usas Scaffold global, pásale este host allá)
    SnackbarHost(hostState = snackHost)
}
