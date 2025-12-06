package com.example.appaeropostv2.presentation.paquete

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.enums.Tiendas
import com.example.appaeropostv2.domain.logic.PaqueteLogic
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPaqueteScreen(
    clientes: List<Cliente>,
    onGuardarPaquete: (Paquete) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = remember {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    // --- Estados de formulario ---
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }
    var expandedCliente by remember { mutableStateOf(false) }

    var tiendaSeleccionada by remember { mutableStateOf<Tiendas?>(null) }
    var expandedTienda by remember { mutableStateOf(false) }

    var casilleroSeleccionado by remember { mutableStateOf<Casilleros?>(null) }
    var expandedCasillero by remember { mutableStateOf(false) }

    var monedaSeleccionada by remember { mutableStateOf<Monedas?>(null) }
    var expandedMoneda by remember { mutableStateOf(false) }

    var pesoTexto by remember { mutableStateOf("") }
    var valorTexto by remember { mutableStateOf("") }
    var condicionEspecial by remember { mutableStateOf(false) }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    var fechaRegistro by remember { mutableStateOf(LocalDate.now()) }

    var errorForm by remember { mutableStateOf<String?>(null) }

    AppScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPadding, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) { }
        },
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Crear paquete"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // --- Cliente ---
            ExposedDropdownMenuBox(
                expanded = expandedCliente,
                onExpandedChange = { expandedCliente = !expandedCliente }
            ) {
                OutlinedTextField(
                    value = clienteSeleccionado?.let {
                        "${it.nombreCliente} (${it.cedulaCliente})"
                    } ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Cliente") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar cliente"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )

                ExposedDropdownMenu(
                    expanded = expandedCliente,
                    onDismissRequest = { expandedCliente = false }
                ) {
                    clientes.forEach { cliente ->
                        DropdownMenuItem(
                            text = { Text("${cliente.nombreCliente} (${cliente.cedulaCliente})") },
                            onClick = {
                                clienteSeleccionado = cliente
                                expandedCliente = false
                            }
                        )
                    }
                }
            }

            // --- Tienda de origen ---
            ExposedDropdownMenuBox(
                expanded = expandedTienda,
                onExpandedChange = { expandedTienda = !expandedTienda }
            ) {
                OutlinedTextField(
                    value = tiendaSeleccionada?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Tienda de origen") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar tienda"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )

                ExposedDropdownMenu(
                    expanded = expandedTienda,
                    onDismissRequest = { expandedTienda = false }
                ) {
                    Tiendas.values().forEach { tienda ->
                        DropdownMenuItem(
                            text = { Text(tienda.name) },
                            onClick = {
                                tiendaSeleccionada = tienda
                                expandedTienda = false
                            }
                        )
                    }
                }
            }

            // --- Casillero ---
            ExposedDropdownMenuBox(
                expanded = expandedCasillero,
                onExpandedChange = { expandedCasillero = !expandedCasillero }
            ) {
                OutlinedTextField(
                    value = casilleroSeleccionado?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Casillero") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar casillero"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )

                ExposedDropdownMenu(
                    expanded = expandedCasillero,
                    onDismissRequest = { expandedCasillero = false }
                ) {
                    Casilleros.values().forEach { casillero ->
                        DropdownMenuItem(
                            text = { Text(casillero.name) },
                            onClick = {
                                casilleroSeleccionado = casillero
                                expandedCasillero = false
                            }
                        )
                    }
                }
            }

            // --- Peso ---
            OutlinedTextField(
                value = pesoTexto,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' || it == ',' }) {
                        pesoTexto = input.replace(',', '.')
                    }
                },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // --- Valor declarado ---
            OutlinedTextField(
                value = valorTexto,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' || it == ',' }) {
                        valorTexto = input.replace(',', '.')
                    }
                },
                label = { Text("Valor declarado") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // --- Moneda ---
            ExposedDropdownMenuBox(
                expanded = expandedMoneda,
                onExpandedChange = { expandedMoneda = !expandedMoneda }
            ) {
                OutlinedTextField(
                    value = monedaSeleccionada?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Moneda") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar moneda"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )

                ExposedDropdownMenu(
                    expanded = expandedMoneda,
                    onDismissRequest = { expandedMoneda = false }
                ) {
                    Monedas.values().forEach { moneda ->
                        DropdownMenuItem(
                            text = { Text(moneda.name) },
                            onClick = {
                                monedaSeleccionada = moneda
                                expandedMoneda = false
                            }
                        )
                    }
                }
            }

            // --- Fecha de registro ---
            OutlinedTextField(
                value = fechaRegistro.format(formatter),
                onValueChange = {},
                label = { Text("Fecha de registro") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { mostrarDatePicker = true },
                readOnly = true,
                enabled = true,
                singleLine = true
            )

            // --- Producto especial ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Producto especial")
                Switch(
                    checked = condicionEspecial,
                    onCheckedChange = { condicionEspecial = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            if (errorForm != null) {
                Text(
                    text = errorForm!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // --- Botones ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        // Validaciones
                        val cliente = clienteSeleccionado
                        val tienda = tiendaSeleccionada
                        val casillero = casilleroSeleccionado
                        val moneda = monedaSeleccionada

                        if (cliente == null ||
                            tienda == null ||
                            casillero == null ||
                            moneda == null ||
                            pesoTexto.isBlank() ||
                            valorTexto.isBlank()
                        ) {
                            errorForm = "Por favor completa todos los campos obligatorios."
                            return@Button
                        }

                        val peso = pesoTexto.toDoubleOrNull()
                        val valor = valorTexto.toBigDecimalOrNull()

                        if (peso == null || valor == null) {
                            errorForm = "Revisa el formato de peso y valor declarado."
                            return@Button
                        }

                        errorForm = null

                        val tracking = PaqueteLogic.generarTracking(tienda, fechaRegistro, casillero)

                        val nuevoPaquete = Paquete(
                            idPaquete = PaqueteLogic.generarIdPaquete(),
                            fechaRegistro = fechaRegistro,
                            idCliente = cliente.idCliente,
                            nombrecliente = cliente.nombreCliente,
                            cedulaCliente = cliente.cedulaCliente,
                            numeroTracking = tracking,
                            pesoPaquete = peso,
                            valorBruto = valor,
                            monedasPaquete = moneda,
                            tiendaOrigen = tienda,
                            casillero = casillero,
                            condicionEspecial = condicionEspecial
                        )

                        onGuardarPaquete(nuevoPaquete)
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Guardar paquete")
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
                        val fechaSeleccionada = millis?.let { m ->
                            Instant.ofEpochMilli(m)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        } ?: LocalDate.now()
                        fechaRegistro = fechaSeleccionada
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
