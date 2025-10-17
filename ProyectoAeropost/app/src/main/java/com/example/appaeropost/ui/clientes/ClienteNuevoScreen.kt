package com.example.appaeropost.ui.clientes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.appaeropost.ui.components.ModuleScaffold
import androidx.compose.material3.MaterialTheme

@Composable
fun ClienteNuevoScreen(
    onBack: () -> Unit = {},
    onGuardarClick: () -> Unit = {}
) {
    ModuleScaffold(
        title = "Nuevo cliente",
        titleColor = MaterialTheme.colorScheme.primary,        // ← azul corporativo
        leading = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    ) {
        ClienteForm(initial = ClienteFormState(), onSubmit = { onGuardarClick() })
    }
}

