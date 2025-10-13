package com.example.appaeropost.ui.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.domain.clientes.Cliente
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardActions


/**
 * Pantalla de Clientes:
 * - Caja de búsqueda (nombre o cédula)
 * - Lista de resultados
 * - Estados de carga y error
 *
 * El ViewModel (ClientesViewModel) expone un StateFlow<ClientesUiState>.
 * Aquí “leemos” ese flujo con collectAsState() para recomponer la UI.
 */
@Composable
fun ClientesScreen(
    modifier: Modifier = Modifier,
    vm: ClientesViewModel = viewModel() // crea/recuerda el VM para esta pantalla
) {
    // Suscribimos la UI al estado del ViewModel (reactivo).
    val ui by vm.state.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize()) {

        // === BARRA DE BÚSQUEDA ==============================================
        OutlinedTextField(
            value = ui.query,
            onValueChange = { vm.onQueryChange(it) }, // delega en el VM (debounce interno)
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            label = { Text("Buscar por nombre o cédula") },
            placeholder = { Text("Ej: Ana, 1-1234-5678") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { keyboard?.hide() } // opcional: cerrar teclado al buscar
            )
        )

        // === CUERPO: LOADING / ERROR / LISTA ================================
        when {
            ui.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            ui.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) { Text("Error: ${ui.error}") }
            }

            else -> {
                ClientesList(items = ui.items)
            }
        }
    }
}

/**
 * Lista simple de clientes. Cada item muestra datos básicos.
 * Más adelante puedes agregar onClick para ir al detalle/edición.
 */
@Composable
private fun ClientesList(items: List<Cliente>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { c ->
            Card {
                Column(Modifier.padding(12.dp)) {
                    Text(text = c.nombre, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(2.dp))
                    Text(text = "Cédula: ${c.identificacion}")
                    Text(text = "Tel: ${c.telefono}")
                    Text(text = "Tipo: ${c.tipo}")
                }
            }
        }
    }
}
