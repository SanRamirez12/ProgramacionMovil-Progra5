package com.example.appaeropostv2.presentation.facturacion

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearFacturacionScreen(
    uiState: FacturacionUiState,
    modifier: Modifier = Modifier,
    onActualizarFechaFacturacion: (String) -> Unit,
    onActualizarCedula: (String) -> Unit,
    onSeleccionarPaquete: (Paquete) -> Unit,
    onCargarCliente: () -> Unit,
    onGenerarFactura: () -> Unit,
    onVolver: () -> Unit,
    onConsumirError: () -> Unit,
    onConsumirPdf: () -> Unit
) {
    val context = LocalContext.current
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    var mostrarDatePicker by remember { mutableStateOf(false) }

    val fechaIso = uiState.fechaFacturacion
    val fechaDisplay = remember(fechaIso) {
        if (fechaIso.isBlank()) "" else LocalDate.parse(fechaIso).format(formatter)
    }

    val pdfUri: Uri? = uiState.pdfUri
    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
            onConsumirPdf()
        }
    }

    val error = uiState.errorMessage
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        if (!error.isNullOrBlank()) {
            snackbarHostState.showSnackbar(error)
            onConsumirError()
        }
    }

    AppScaffold(
        topBar = {},
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Facturación",
                subtitle = "Nueva factura"
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Generar nueva factura",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            // --- Fecha de facturación ---
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Fecha de facturación",
                    style = MaterialTheme.typography.labelMedium
                )

                Button(
                    onClick = { mostrarDatePicker = true },
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(text = if (fechaDisplay.isBlank()) "Seleccionar fecha" else fechaDisplay)
                }
            }

            // --- Cédula del cliente ---
            OutlinedTextField(
                value = uiState.cedulaInput,
                onValueChange = onActualizarCedula,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Cédula del cliente") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Button(
                onClick = onCargarCliente,
                shape = MaterialTheme.shapes.large
            ) {
                Text("Cargar datos del cliente")
            }

            uiState.clienteCargado?.let { cliente ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Cliente cargado:", style = MaterialTheme.typography.labelMedium)
                        Text("Nombre: ${cliente.nombreCliente}")
                        Text("Cédula: ${cliente.cedulaCliente}")
                        Text("Teléfono: ${cliente.telefonoCliente}")
                        Text("Correo: ${cliente.correoCliente}")
                        Text("Dirección entrega: ${cliente.direccionEntrega}")
                    }
                }
            }

            // --- Dropdown de paquetes sin facturar ---
            if (uiState.clienteCargado != null) {
                Text(
                    text = "Paquete a facturar",
                    style = MaterialTheme.typography.labelMedium
                )

                if (uiState.paquetesDisponibles.isEmpty()) {
                    Text(
                        text = "Este cliente no tiene paquetes pendientes de facturar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.paqueteCargado?.numeroTracking
                                ?: "Seleccionar paquete",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            label = { Text("Paquetes sin facturar") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            uiState.paquetesDisponibles.forEach { paquete ->
                                val symbol = when (paquete.monedasPaquete) {
                                    Monedas.USD -> "$"
                                    Monedas.CRC -> "₡"
                                    Monedas.EUR -> "€"
                                }

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "${paquete.numeroTracking}  ·  $symbol${paquete.valorBruto}"
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        onSeleccionarPaquete(paquete)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // --- Paquete seleccionado ---
            uiState.paqueteCargado?.let { paquete ->
                val symbol = when (paquete.monedasPaquete) {
                    Monedas.USD -> "$"
                    Monedas.CRC -> "₡"
                    Monedas.EUR -> "€"
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Paquete seleccionado:", style = MaterialTheme.typography.labelMedium)
                        Text("Tracking: ${paquete.numeroTracking}")
                        Text("Fecha registro: ${paquete.fechaRegistro}")
                        Text("Peso: ${paquete.pesoPaquete} kg")
                        Text("Valor bruto: $symbol${paquete.valorBruto}")
                        Text("Tienda / Casillero: ${paquete.tiendaOrigen} / ${paquete.casillero}")
                        Text("Producto especial: ${if (paquete.condicionEspecial) "Sí" else "No"}")
                    }
                }
            }

            // --- Monto total ---
            uiState.montoTotalCalculado?.let { total ->
                val symbol = uiState.paqueteCargado?.let { paquete ->
                    when (paquete.monedasPaquete) {
                        Monedas.USD -> "$"
                        Monedas.CRC -> "₡"
                        Monedas.EUR -> "€"
                    }
                } ?: ""

                Text(
                    text = "Monto total calculado: $symbol${String.format("%.2f", total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = onGenerarFactura,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Generar factura")
                }
            }
        }

        if (mostrarDatePicker) {
            val state = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    Button(onClick = {
                        val millis = state.selectedDateMillis
                        val fechaSeleccionada = millis?.let { toLocalDate(it) }
                        if (fechaSeleccionada != null) {
                            onActualizarFechaFacturacion(fechaSeleccionada.toString())
                        }
                        mostrarDatePicker = false
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    Button(onClick = { mostrarDatePicker = false }) {
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
