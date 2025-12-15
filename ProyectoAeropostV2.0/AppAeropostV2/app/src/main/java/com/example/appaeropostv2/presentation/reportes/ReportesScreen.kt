package com.example.appaeropostv2.presentation.reportes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@Composable
fun ReporteScreen(
    vm: ReporteViewModel
) {
    val state by vm.state.collectAsState()

    AppScaffold(
        header = {
            GradientHeader(
                title = "Reporte Maestro",
                subtitle = "PDF + XLSX (Excel) en un solo envío"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Seleccioná qué secciones incluir:",
                style = MaterialTheme.typography.titleMedium
            )

            ReporteCheck(
                label = "Resumen ejecutivo (incluye tipo de cambio)",
                checked = state.config.incluirResumenEjecutivo,
                onChange = { vm.toggleResumen(it) }
            )
            ReporteCheck(
                label = "Clientes",
                checked = state.config.incluirClientes,
                onChange = { vm.toggleClientes(it) }
            )
            ReporteCheck(
                label = "Paquetes",
                checked = state.config.incluirPaquetes,
                onChange = { vm.togglePaquetes(it) }
            )
            ReporteCheck(
                label = "Facturación",
                checked = state.config.incluirFacturacion,
                onChange = { vm.toggleFacturacion(it) }
            )
            ReporteCheck(
                label = "Estadísticas básicas (CRC unificado)",
                checked = state.config.incluirEstadisticasBasicas,
                onChange = { vm.toggleStats(it) }
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { vm.generarYEnviar() },
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Generando & enviando…")
                } else {
                    Text("Generar y enviar al correo del usuario actual")
                }
            }

            state.mensaje?.let { msg ->
                Spacer(Modifier.height(6.dp))
                AssistChip(
                    onClick = {},
                    label = { Text(msg) }
                )
            }
        }
    }
}

@Composable
private fun ReporteCheck(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
