package com.example.appaeropostv2.presentation.paquete

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
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
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPaqueteScreen(
    paqueteOriginal: Paquete,
    clientes: List<Cliente>,
    onGuardarCambios: (Paquete) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = remember {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    // Cliente inicial (por id)
    var clienteSeleccionado by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(
            clientes.firstOrNull { it.idCliente == paqueteOriginal.idCliente }
        )
    }
    var expandedCliente by remember { mutableStateOf(false) }

    var tiendaSeleccionada by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(paqueteOriginal.tiendaOrigen)
    }
    var expandedTienda by remember { mutableStateOf(false) }

    var casilleroSeleccionado by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(paqueteOriginal.casillero)
    }
    var expandedCasillero by remember { mutableStateOf(false) }

    var monedaSeleccionada by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(paqueteOriginal.monedasPaquete)
    }
    var expandedMoneda by remember { mutableStateOf(false) }

    var pesoTexto by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(paqueteOriginal.pesoPaquete.toString())
    }
    var valorTexto by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(paqueteOriginal.valorBruto.toPlainString())
    }
    var condicionEspecial by remember(paqueteOriginal.idPaquete) {
        mutableStateOf(paqueteOriginal.condicionEspecial)
    }

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
                subtitle = "Editar paquete"
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

            // Cliente
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

            // Tienda
            ExposedDropdownMenuBox(
                expanded = expandedTienda,
                onExpandedChange = { expandedTienda = !expandedTienda }
            ) {
                OutlinedTextField(
                    value = tiendaSeleccionada.name,
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

            // Casillero
            ExposedDropdownMenuBox(
                expanded = expandedCasillero,
                onExpandedChange = { expandedCasillero = !expandedCasillero }
            ) {
                OutlinedTextField(
                    value = casilleroSeleccionado.name,
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

            // Peso
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

            // Valor
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

            // Moneda
            ExposedDropdownMenuBox(
                expanded = expandedMoneda,
                onExpandedChange = { expandedMoneda = !expandedMoneda }
            ) {
                OutlinedTextField(
                    value = monedaSeleccionada.name,
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

            // Fecha registro (solo lectura)
            OutlinedTextField(
                value = paqueteOriginal.fechaRegistro.format(formatter),
                onValueChange = {},
                label = { Text("Fecha de registro") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                singleLine = true
            )

            // Producto especial
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
                        val cliente = clienteSeleccionado
                        val moneda = monedaSeleccionada
                        val peso = pesoTexto.toDoubleOrNull()
                        val valor = valorTexto.toBigDecimalOrNull()

                        if (cliente == null ||
                            moneda == null ||
                            peso == null ||
                            valor == null
                        ) {
                            errorForm = "Revisa los datos del cliente, peso, valor y moneda."
                            return@Button
                        }

                        errorForm = null

                        val debeRegenerarTracking =
                            tiendaSeleccionada != paqueteOriginal.tiendaOrigen ||
                                    casilleroSeleccionado != paqueteOriginal.casillero

                        val nuevoTracking =
                            if (debeRegenerarTracking) {
                                PaqueteLogic.generarTracking(
                                    tienda = tiendaSeleccionada,
                                    fecha = paqueteOriginal.fechaRegistro,
                                    casillero = casilleroSeleccionado
                                )
                            } else {
                                paqueteOriginal.numeroTracking
                            }

                        val actualizado = paqueteOriginal.copy(
                            idCliente = cliente.idCliente,
                            nombrecliente = cliente.nombreCliente,
                            cedulaCliente = cliente.cedulaCliente,
                            numeroTracking = nuevoTracking,
                            pesoPaquete = peso,
                            valorBruto = valor,
                            monedasPaquete = moneda,
                            tiendaOrigen = tiendaSeleccionada,
                            casillero = casilleroSeleccionado,
                            condicionEspecial = condicionEspecial
                        )

                        onGuardarCambios(actualizado)
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }
}
