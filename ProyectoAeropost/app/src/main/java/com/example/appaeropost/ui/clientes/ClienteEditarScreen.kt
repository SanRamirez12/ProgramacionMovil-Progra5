package com.example.appaeropost.ui.clientes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropost.data.clientes.ClientesRepository
import com.example.appaeropost.data.clientes.FakeClientesRepository
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun ClienteEditarScreen(
    clienteId: Int,
    repo: ClientesRepository = FakeClientesRepository(),   // ← antes era la clase concreta
    onBack: () -> Unit = {},
    onGuardarClick: () -> Unit = {}
) {
    var loaded by remember { mutableStateOf<ClienteFormState?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(clienteId) {
        isLoading = true
        val c = repo.getById(clienteId)
        loaded = c?.let {
            ClienteFormState(
                nombre = it.nombre,
                identificacion = it.identificacion,
                telefono = it.telefono,
                correo = it.correo,
                tipo = it.tipo,
                estado = it.estado,
                direccionEntrega = it.direccionEntrega
            )
        }
        isLoading = false
    }

    ModuleScaffold(
        title = "Editar cliente",
        titleColor = MaterialTheme.colorScheme.primary,           // ← azul
        leading = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    ) {
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            ClienteForm(
                initial = loaded ?: ClienteFormState(),
                onSubmit = { /* repo.update(...) más adelante */ onGuardarClick() }
            )
        }
    }
}
