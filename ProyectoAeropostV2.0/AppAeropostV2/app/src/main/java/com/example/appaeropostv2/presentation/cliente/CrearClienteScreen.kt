package com.example.appaeropostv2.presentation.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.RolesClientes
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearClienteScreen(
    onGuardarCliente: (Cliente) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Estados del formulario ---
    var nombre by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccionEntrega by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf<RolesClientes?>(null) }

    val estadoPorDefecto = Estados.HABILITADO

    var errorForm by remember { mutableStateOf<String?>(null) }

    // Dropdown state
    var expandedTipo by remember { mutableStateOf(false) }

    AppScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.ScreenPadding,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {}
        },
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Crear cliente"
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

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Cédula
            OutlinedTextField(
                value = cedula,
                onValueChange = { input ->
                    // Dígitos y guiones, típico CR
                    if (input.all { it.isDigit() || it == '-' }) {
                        cedula = input
                    }
                },
                label = { Text("Cédula") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // Teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '-' || it == ' ' }) {
                        telefono = input
                    }
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )

            // Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            // Tipo de cliente (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = tipoSeleccionado?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Tipo de cliente") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar tipo de cliente"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    RolesClientes.values().forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.name) },
                            onClick = {
                                tipoSeleccionado = tipo
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            // Estado (solo lectura, HABILITADO)
            OutlinedTextField(
                value = estadoPorDefecto.name,
                onValueChange = {},
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                singleLine = true
            )

            // Dirección de entrega
            OutlinedTextField(
                value = direccionEntrega,
                onValueChange = { direccionEntrega = it },
                label = { Text("Dirección de entrega") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 2
            )

            // Mensaje de error
            if (errorForm != null) {
                Text(
                    text = errorForm!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Botones
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
                        // Validaciones básicas
                        if (nombre.isBlank() ||
                            cedula.isBlank() ||
                            telefono.isBlank() ||
                            correo.isBlank() ||
                            direccionEntrega.isBlank() ||
                            tipoSeleccionado == null
                        ) {
                            errorForm = "Por favor completa todos los campos obligatorios."
                            return@Button
                        }

                        errorForm = null

                        val nuevoCliente = Cliente(
                            idCliente = 0, // Room autogenerará
                            nombreCliente = nombre.trim(),
                            cedulaCliente = cedula.trim(),
                            telefonoCliente = telefono.trim(),
                            correoCliente = correo.trim(),
                            tipoCliente = tipoSeleccionado!!,
                            estadoCliente = estadoPorDefecto,
                            direccionEntrega = direccionEntrega.trim()
                        )

                        onGuardarCliente(nuevoCliente)
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Guardar cliente")
                }
            }
        }
    }
}


