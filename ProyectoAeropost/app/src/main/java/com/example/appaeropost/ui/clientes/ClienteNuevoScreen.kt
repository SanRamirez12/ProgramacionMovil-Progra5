package com.example.appaeropost.ui.clientes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun ClienteNuevoScreen(
    vm: ClientesViewModel,
    onBack: () -> Unit = {},
    onGuardado: () -> Unit = {}
) {
    ModuleScaffold(
        title = "Nuevo cliente",
        titleColor = MaterialTheme.colorScheme.primary,
        leading = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    ) {
        ClienteForm(
            initial = ClienteFormState(),
            onSubmit = { formState ->
                vm.crearCliente(formState) { onGuardado() }
            }
        )
    }
}



