package com.example.appaeropostv2.presentation.facturacion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.components.ModuleTable
import com.example.appaeropostv2.presentation.common.components.TableColumn
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturacionScreen(
    uiState: FacturacionUiState,
    modifier: Modifier = Modifier,
    onIrCrearFactura: () -> Unit,
    onVerDetalleFactura: (Facturacion) -> Unit,
    onEliminarFactura: (Facturacion) -> Unit,
    onActualizarBusqueda: (String) -> Unit,
    onActualizarFechaDesde: (LocalDate?) -> Unit,
    onActualizarFechaHasta: (LocalDate?) -> Unit,
    onLimpiarFechas: () -> Unit,
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    var mostrarPickerDesde by remember { mutableStateOf(false) }
    var mostrarPickerHasta by remember { mutableStateOf(false) }

    val fechaDesde = uiState.fechaDesde
    val fechaHasta = uiState.fechaHasta

    AppScaffold(
        topBar = {},
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Facturación"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp)
        ) {

            Text(
                text = "Facturación",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- Buscar por tracking o cédula ---
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onActualizarBusqueda,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por tracking o cédula") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* cerrar teclado si quieres */ })
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Filtro por rango de fechas ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { mostrarPickerDesde = true },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = fechaDesde?.format(formatter) ?: "Fecha inicial")
                }

                Button(
                    onClick = { mostrarPickerHasta = true },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = fechaHasta?.format(formatter) ?: "Fecha final")
                }
            }

            if (fechaDesde != null || fechaHasta != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtrando por rango de fechas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Limpiar rango",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onLimpiarFechas() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón Facturar ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onIrCrearFactura,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Facturar")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Tabla de facturas ---
            if (uiState.facturas.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No se encontraron facturas con ese criterio.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                ModuleTable(
                    items = uiState.facturas,
                    columns = listOf(
                        TableColumn<Facturacion>(
                            header = "Cédula",
                            weight = 1.2f
                        ) { factura -> factura.cedulaCliente },
                        TableColumn<Facturacion>(
                            header = "Tracking",
                            weight = 1.4f
                        ) { factura -> factura.numeroTracking },
                        TableColumn<Facturacion>(
                            header = "Monto total",
                            weight = 1.0f
                        ) { factura -> "₡${String.format("%.2f", factura.montoTotal)}" }
                    ),
                    onEditClick = { /* no se edita factura */ },
                    onDetailsClick = onVerDetalleFactura,
                    onDisableClick = onEliminarFactura,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ---- DatePickers ----
        if (mostrarPickerDesde) {
            val state = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { mostrarPickerDesde = false },
                confirmButton = {
                    Button(onClick = {
                        val millis = state.selectedDateMillis
                        val fechaSeleccionada = millis?.let { toLocalDate(it) }
                        onActualizarFechaDesde(fechaSeleccionada)
                        mostrarPickerDesde = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    Button(onClick = { mostrarPickerDesde = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = state)
            }
        }

        if (mostrarPickerHasta) {
            val state = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { mostrarPickerHasta = false },
                confirmButton = {
                    Button(onClick = {
                        val millis = state.selectedDateMillis
                        val fechaSeleccionada = millis?.let { toLocalDate(it) }
                        onActualizarFechaHasta(fechaSeleccionada)
                        mostrarPickerHasta = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    Button(onClick = { mostrarPickerHasta = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = state)
            }
        }
    }
}

private fun toLocalDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
