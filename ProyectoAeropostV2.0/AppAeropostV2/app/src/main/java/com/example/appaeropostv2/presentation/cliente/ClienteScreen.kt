package com.example.appaeropostv2.presentation.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.RolesClientes
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.components.ModuleTable
import com.example.appaeropostv2.presentation.common.components.TableColumn
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@Composable
fun ClientesScreen(
    clientes: List<Cliente>,
    modifier: Modifier = Modifier,
    onIrCrearCliente: () -> Unit,
    onEditarCliente: (Cliente) -> Unit,
    onVerDetallesCliente: (Cliente) -> Unit,
    onDeshabilitarCliente: (Cliente) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    // Filtro simple por nombre o cédula (como pediste)
    val clientesFiltrados = remember(clientes, query) {
        if (query.isBlank()) {
            clientes
        } else {
            val q = query.trim().lowercase()
            clientes.filter { cliente ->
                val nombre = cliente.nombreCliente.lowercase()
                val cedula = cliente.cedulaCliente.lowercase()
                nombre.contains(q) || cedula.contains(q)
            }
        }
    }

    AppScaffold(
        topBar = {},
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Clientes"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp)
        ) {
            // Título principal
            Text(
                text = "Clientes",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Barra de búsqueda (nombre o cédula)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por nombre o cédula") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { /* ocultar teclado si quieres */ }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Crear cliente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onIrCrearCliente,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Crear cliente")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tabla de clientes
            if (clientesFiltrados.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No se encontraron clientes con ese criterio.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                ModuleTable(
                    items = clientesFiltrados,
                    columns = listOf(
                        TableColumn<Cliente>(
                            header = "Nombre",
                            weight = 1.2f,
                        ) { cliente ->
                            cliente.nombreCliente
                        },
                        TableColumn<Cliente>(
                            header = "Cédula",
                            weight = 1f
                        ) { cliente ->
                            cliente.cedulaCliente
                        },
                        TableColumn<Cliente>(
                            header = "Tipo",
                            weight = 0.8f
                        ) { cliente ->
                            when (val tipo = cliente.tipoCliente) {
                                is RolesClientes -> tipo.name
                                else -> tipo.toString()
                            }
                        }
                    ),
                    onEditClick = onEditarCliente,
                    onDetailsClick = onVerDetallesCliente,
                    onDisableClick = onDeshabilitarCliente,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


