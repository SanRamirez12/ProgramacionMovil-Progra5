package com.example.appaeropost.ui.facturacion

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.domain.Factura
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun FacturacionEditarScreen(
    facturaId: String,
    vm: FacturacionViewModel = viewModel(),
    onCancel: () -> Unit = {},
    onSaved: () -> Unit = {}
) {
    val ui by vm.ui.collectAsState()
    val factura = ui.items.firstOrNull { it.id == facturaId }

    ModuleScaffold(
        title = "Editar factura",
        titleColor = MaterialTheme.colorScheme.primary,
        leading = {
            IconButton(onClick = onCancel) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
    ) {
        when {
            factura != null -> {
                FacturacionForm(
                    vm = vm,
                    modoEdicion = true,
                    facturaInicial = factura,
                    onSubmit = { f: Factura ->
                        vm.actualizar(
                            f,
                            onOk = { onSaved() },
                            onErr = { /* TODO: mostrar error */ }
                        )
                    },
                    onCancel = onCancel
                )
            }
            ui.isLoading -> {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            else -> {
                // No se encontró la factura (o aún no carga): regresar
                onCancel()
            }
        }
    }
}

