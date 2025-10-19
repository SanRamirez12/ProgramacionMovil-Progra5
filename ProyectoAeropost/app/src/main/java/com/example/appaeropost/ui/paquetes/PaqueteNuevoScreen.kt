package com.example.appaeropost.ui.paquetes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun PaqueteNuevoScreen(
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {
    ModuleScaffold(title = "Registrar paquete") {
        PaqueteForm(
            onCancel = onCancel,
            onSubmit = {
                // TODO: llamar a ViewModel para transformar y guardar
                // (por ahora solo navega hacia atrás)
                onSaved()
            }
        )
    }
}

