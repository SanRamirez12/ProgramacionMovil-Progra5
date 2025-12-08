package com.example.appaeropostv2.presentation.facturacion

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearFacturacionScreen(
    uiState: FacturacionUiState,
    modifier: Modifier = Modifier,
    onActualizarFechaFacturacion: (String) -> Unit,
    onActualizarCedula: (String) -> Unit,
    onActualizarTracking: (String) -> Unit,
    onCargarCliente: () -> Unit,
    onCargarPaquete: () -> Unit,
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

    // Abrir PDF cuando se genere
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

    // Mostrar error simple con Snackbar
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
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp),
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

            // --- Tracking del paquete ---
            OutlinedTextField(
                value = uiState.trackingInput,
                onValueChange = onActualizarTracking,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Número de tracking del paquete") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Button(
                onClick = onCargarPaquete,
                shape = MaterialTheme.shapes.large
            ) {
                Text("Cargar datos del paquete")
            }

            uiState.paqueteCargado?.let { paquete ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Paquete cargado:", style = MaterialTheme.typography.labelMedium)
                        Text("Tracking: ${paquete.numeroTracking}")
                        Text("Fecha registro: ${paquete.fechaRegistro}")
                        Text("Peso: ${paquete.pesoPaquete} kg")
                        Text("Valor bruto: ₡${paquete.valorBruto}")
                        Text("Tienda / Casillero: ${paquete.tiendaOrigen} / ${paquete.casillero}")
                        Text("Producto especial: ${if (paquete.condicionEspecial) "Sí" else "No"}")
                    }
                }
            }

            // --- Monto total ---
            uiState.montoTotalCalculado?.let { total ->
                Text(
                    text = "Monto total calculado: ₡${String.format("%.2f", total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Acciones finales ---
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
                            onActualizarFechaFacturacion(fechaSeleccionada.toString()) // ISO yyyy-MM-dd
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
