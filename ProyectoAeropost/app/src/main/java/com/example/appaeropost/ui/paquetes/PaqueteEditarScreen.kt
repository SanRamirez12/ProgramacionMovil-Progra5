package com.example.appaeropost.ui.paquetes

import androidx.compose.runtime.Composable
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun PaqueteEditarScreen(
    paqueteId: String,
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {
    // TODO: cargar datos por paqueteId desde el VM/repositorio y prellenar el form
    ModuleScaffold(title = "Editar paquete") {
        PaqueteForm(
            // initial = estado cargado,
            onCancel = onCancel,
            onSubmit = { onSaved() }
        )
    }
}


