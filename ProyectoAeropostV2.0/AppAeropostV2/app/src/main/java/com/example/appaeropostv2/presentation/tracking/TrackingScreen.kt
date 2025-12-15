package com.example.appaeropostv2.presentation.tracking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.presentation.common.components.ModuleTable
import com.example.appaeropostv2.presentation.common.components.TableColumn

@Composable
fun TrackingScreen(
    viewModel: TrackingViewModel,
    onVerEstado: (String) -> Unit
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tracking")

        OutlinedTextField(
            value = state.cedulaBusqueda,
            onValueChange = viewModel::onChangeCedula,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar por cédula") },
            singleLine = true
        )

        Button(
            onClick = { viewModel.buscarPorCedula() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        if (state.cargando) CircularProgressIndicator()
        state.error?.let { Text(it) }

        ModuleTable(
            items = state.resultados,
            columns = listOf(
                TableColumn(header = "Cédula", weight = 1f, value = { state.cedulaBusqueda }),
                TableColumn(header = "# Tracking", weight = 1.3f, value = { it.numeroTracking }),
                TableColumn(header = "Estado", weight = 1.2f, value = { it.estado.etiqueta })
            ),
            onEditClick = null,
            onDisableClick = null, // ya no se muestra Close
            onDetailsClick = { tracking -> onVerEstado(tracking.numeroTracking) } // solo Info
        )
    }
}
