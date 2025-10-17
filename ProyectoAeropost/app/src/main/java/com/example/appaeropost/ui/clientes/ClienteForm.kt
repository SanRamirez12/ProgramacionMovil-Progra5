package com.example.appaeropost.ui.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appaeropost.domain.clientes.EstadoCliente
import com.example.appaeropost.domain.clientes.TipoCliente


/** Estado del formulario compartido por Nuevo/Editar */
data class ClienteFormState(
    val nombre: String = "",
    val identificacion: String = "",
    val telefono: String = "",
    val correo: String = "",
    val tipo: TipoCliente = TipoCliente.REGULAR,
    val estado: EstadoCliente = EstadoCliente.HABILITADO,
    val direccionEntrega: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteForm(
    initial: ClienteFormState,
    onSubmit: (ClienteFormState) -> Unit
) {
    var state by remember { mutableStateOf(initial) }
    var tipoExpanded by remember { mutableStateOf(false) }
    var estadoExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { state = state.copy(nombre = it) },
            label = { Text("Nombre completo*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = state.identificacion,
            onValueChange = { state = state.copy(identificacion = it) },
            label = { Text("Cédula / Identificación*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = state.telefono,
            onValueChange = { state = state.copy(telefono = it) },
            label = { Text("Teléfono*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        OutlinedTextField(
            value = state.correo,
            onValueChange = { state = state.copy(correo = it) },
            label = { Text("Correo electrónico*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Tipo
        ExposedDropdownMenuBox(
            expanded = tipoExpanded,
            onExpandedChange = { tipoExpanded = it }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = state.tipo.name.lowercase().replaceFirstChar { it.titlecase() },
                onValueChange = {},
                label = { Text("Tipo de cliente*") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = tipoExpanded, onDismissRequest = { tipoExpanded = false }) {
                TipoCliente.values().forEach { t ->
                    DropdownMenuItem(
                        text = { Text(t.name.lowercase().replaceFirstChar { it.titlecase() }) },
                        onClick = { state = state.copy(tipo = t); tipoExpanded = false }
                    )
                }
            }
        }

        // Estado
        ExposedDropdownMenuBox(
            expanded = estadoExpanded,
            onExpandedChange = { estadoExpanded = it }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = if (state.estado == EstadoCliente.HABILITADO) "Habilitado" else "Deshabilitado",
                onValueChange = {},
                label = { Text("Estado*") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = estadoExpanded, onDismissRequest = { estadoExpanded = false }) {
                listOf(EstadoCliente.HABILITADO, EstadoCliente.DESHABILITADO).forEach { e ->
                    DropdownMenuItem(
                        text = { Text(if (e == EstadoCliente.HABILITADO) "Habilitado" else "Deshabilitado") },
                        onClick = { state = state.copy(estado = e); estadoExpanded = false }
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.direccionEntrega,
            onValueChange = { state = state.copy(direccionEntrega = it) },
            label = { Text("Dirección de entrega*") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = { /* limpiar o volver, si querés */ }) { Text("Cancelar") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onSubmit(state) }) { Text("Guardar") }
        }
    }
}
