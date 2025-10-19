package com.example.appaeropost.ui.paquetes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.R
import com.example.appaeropost.ui.components.ModuleScaffold
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PaquetesScreen(
    vm: PaquetesViewModel = viewModel(),
    onNuevoClick: () -> Unit = {},
    onEditarClick: (String) -> Unit = {}
) {
    val state by vm.ui.collectAsState()

    ModuleScaffold(
        title = null,
        showTopBar = false,
        actions = {},
        floating = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderPaquetes()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.cedulaQuery,
                    onValueChange = vm::onCedulaChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Buscar por cédula") },
                    singleLine = true
                )
                Button(
                    onClick = onNuevoClick,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Registrar nuevo paquete")
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            PaquetesTable(
                rows = state.items.map { it.toUi() },
                onEditarClick = onEditarClick
            )

            state.error?.let {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (state.items.isEmpty() && state.cedulaQuery.isBlank() && !state.isLoading) {
                Text(
                    text = "Escribe una cédula para buscar paquetes",
                    modifier = Modifier.padding(top = 18.dp).alpha(0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun HeaderPaquetes() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_paquetes),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Paquetes",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.background
                )
                Text("Buscar, registrar y editar paquetes", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** Tabla: Fecha | Cliente | Cédula | Tracking | (lápiz) */
@Composable
private fun PaquetesTable(
    rows: List<PaqueteUi>,
    onEditarClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
        TableHeader()
        Divider()
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            items(rows, key = { it.id }) { p ->
                TableRow(
                    fecha = p.fecha,
                    cliente = p.cliente,
                    cedula = p.cedula,
                    tracking = p.tracking,
                    onEdit = { onEditarClick(p.id) }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Fecha",   modifier = Modifier.weight(0.26f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Cliente", modifier = Modifier.weight(0.34f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Cédula",  modifier = Modifier.weight(0.22f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Tracking",modifier = Modifier.weight(0.14f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(0.04f)) // espacio para lapicito
    }
}

@Composable
private fun TableRow(
    fecha: String,
    cliente: String,
    cedula: String,
    tracking: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(fecha, modifier = Modifier.weight(0.26f), style = MaterialTheme.typography.bodyMedium)
        Text(cliente, modifier = Modifier.weight(0.34f), style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(cedula,  modifier = Modifier.weight(0.22f), style = MaterialTheme.typography.bodyMedium)
        Text(tracking,modifier = Modifier.weight(0.14f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Box(modifier = Modifier.weight(0.04f), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
        }
    }
}

/* --------- Mappers UI --------- */
private data class PaqueteUi(
    val id: String,
    val fecha: String,
    val cliente: String,
    val cedula: String,
    val tracking: String
)

private fun com.example.appaeropost.domain.paquetes.Paquete.toUi(): PaqueteUi {
    val df = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return PaqueteUi(
        id = id,
        fecha = fechaRegistro.format(df),
        cliente = clienteNombre,
        cedula = clienteCedula,
        tracking = tracking
    )
}



