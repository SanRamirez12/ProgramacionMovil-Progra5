package com.example.appaeropost.ui.facturacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.domain.Factura
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun FacturacionNuevaScreen(
    vm: FacturacionViewModel = viewModel(),
    onCancel: () -> Unit = {},
    onSaved: () -> Unit = {}
) {
    ModuleScaffold(
        title = "Registrar factura",
        titleColor = MaterialTheme.colorScheme.primary,
        leading = {
            IconButton(onClick = onCancel) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    ) {
        FacturacionForm(
            vm = vm,
            modoEdicion = false,
            facturaInicial = null,
            onSubmit = { f: Factura ->
                // Guarda con el VM; si falla lo manejaremos luego (toast/dialog).
                vm.crear(f, onOk = { onSaved() }, onErr = { /* TODO: mostrar error */ })
            },
            onCancel = onCancel
        )
    }
}

