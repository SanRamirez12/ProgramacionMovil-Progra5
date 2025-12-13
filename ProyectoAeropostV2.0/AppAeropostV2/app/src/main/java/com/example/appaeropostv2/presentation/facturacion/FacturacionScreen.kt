package com.example.appaeropostv2.presentation.facturacion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.components.ModuleTable
import com.example.appaeropostv2.presentation.common.components.TableColumn
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    onConsumirCorreoEnviado: () -> Unit,
    onConsumirErrorCorreo: () -> Unit,
    onConsumirErrorGeneral: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    var mostrarPickerDesde by remember { mutableStateOf(false) }
    var mostrarPickerHasta by remember { mutableStateOf(false) }

    val fechaDesde = uiState.fechaDesde
    val fechaHasta = uiState.fechaHasta

    val snackbarHostState = remember { SnackbarHostState() }

    // ✅ Snackbars centrales (aquí SIEMPRE se ven)
    LaunchedEffect(uiState.correoEnviado) {
        if (uiState.correoEnviado) {
            snackbarHostState.showSnackbar("Factura enviada por correo correctamente")
            onConsumirCorreoEnviado()
        }
    }

    LaunchedEffect(uiState.errorCorreo) {
        uiState.errorCorreo?.let { msg ->
            snackbarHostState.showSnackbar("Error enviando correo: $msg")
            onConsumirErrorCorreo()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            onConsumirErrorGeneral()
        }
    }

    AppScaffold(
        topBar = {},
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Facturación",
                subtitle = "Gestión de facturas"
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Facturas registradas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // --- Barra de búsqueda ---
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onActualizarBusqueda,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por cédula o tracking") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { })
            )

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

            // --- Botón Crear factura ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onIrCrearFactura,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Crear factura")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                            header = "Cédula cliente",
                            weight = 1.2f
                        ) { factura -> factura.cedulaCliente },
                        TableColumn<Facturacion>(
                            header = "Tracking",
                            weight = 1.4f
                        ) { factura -> factura.numeroTracking },
                        TableColumn<Facturacion>(
                            header = "Monto total",
                            weight = 1.0f
                        ) { factura ->
                            val moneda = uiState.monedaPorTracking[factura.numeroTracking]
                            val symbol = when (moneda) {
                                Monedas.USD -> "$"
                                Monedas.CRC -> "₡"
                                Monedas.EUR -> "€"
                                null -> ""
                            }
                            symbol + String.format("%.2f", factura.montoTotal)
                        }
                    ),
                    onEditClick = null,
                    onDetailsClick = onVerDetalleFactura,
                    onDisableClick = onEliminarFactura,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Diálogo fecha DESDE
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
                    Button(onClick = { mostrarPickerDesde = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = state)
            }
        }

        // Diálogo fecha HASTA
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
                    Button(onClick = { mostrarPickerHasta = false }) { Text("Cancelar") }
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
