package com.example.appaeropost.ui.paquetes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.appaeropost.domain.paquetes.Moneda
import com.example.appaeropost.domain.paquetes.TiendaOrigen

@Composable
fun PaqueteForm(
    modifier: Modifier = Modifier,
    initial: PaqueteFormState = PaqueteFormState(),
    onSubmit: (PaqueteFormState) -> Unit,
    onCancel: () -> Unit
) {
    var state by remember { mutableStateOf(initial) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cliente asociado (por ahora texto simple)
        OutlinedTextField(
            value = state.clienteNombre,
            onValueChange = { state = state.copy(clienteNombre = it) },
            label = { Text("Cliente") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.clienteCedula,
            onValueChange = { state = state.copy(clienteCedula = it) },
            label = { Text("Cédula") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Tracking (por ahora manual)
        OutlinedTextField(
            value = state.tracking,
            onValueChange = { state = state.copy(tracking = it) },
            label = { Text("Número de tracking") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Peso
        OutlinedTextField(
            value = state.pesoLb,
            onValueChange = { state = state.copy(pesoLb = it) },
            label = { Text("Peso (lb)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        // Valor + Moneda
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.valorBruto,
                onValueChange = { state = state.copy(valorBruto = it) },
                label = { Text("Valor bruto") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            MonedaDropdown(
                selected = state.moneda,
                onSelected = { state = state.copy(moneda = it) },
                modifier = Modifier.weight(1f)
            )
        }

        // Tienda de origen
        TiendaDropdown(
            selected = state.tiendaOrigen,
            onSelected = { state = state.copy(tiendaOrigen = it) },
            modifier = Modifier.fillMaxWidth()
        )

        // Condición especial
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Switch(
                checked = state.condicionEspecial,
                onCheckedChange = { state = state.copy(condicionEspecial = it) }
            )
            Spacer(Modifier.width(8.dp))
            Text("Condición especial")
        }

        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large
            ) { Text("Cancelar") }

            Button(
                onClick = { onSubmit(state) },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large
            ) { Text("Guardar") }
        }
    }
}

data class PaqueteFormState(
    val clienteNombre: String = "",
    val clienteCedula: String = "",
    val tracking: String = "",
    val pesoLb: String = "",
    val valorBruto: String = "",
    val moneda: Moneda = Moneda.USD,
    val tiendaOrigen: TiendaOrigen = TiendaOrigen.AMAZON,
    val condicionEspecial: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonedaDropdown(
    selected: Moneda,
    onSelected: (Moneda) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Moneda") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Moneda.values().forEach { m ->
                DropdownMenuItem(text = { Text(m.name) }, onClick = {
                    onSelected(m); expanded = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TiendaDropdown(
    selected: TiendaOrigen,
    onSelected: (TiendaOrigen) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier) {
        OutlinedTextField(
            value = selected.name.lowercase().replaceFirstChar { it.uppercase() },
            onValueChange = {},
            readOnly = true,
            label = { Text("Tienda de origen") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            TiendaOrigen.values().forEach { t ->
                DropdownMenuItem(text = { Text(t.name.lowercase().replaceFirstChar { it.uppercase() }) }, onClick = {
                    onSelected(t); expanded = false
                })
            }
        }
    }
}


