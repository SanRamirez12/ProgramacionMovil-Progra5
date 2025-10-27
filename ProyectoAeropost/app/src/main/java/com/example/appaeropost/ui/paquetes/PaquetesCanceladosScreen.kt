package com.example.appaeropost.ui.paquetes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.domain.paquetes.PaqueteCancelado
import com.example.appaeropost.ui.components.ModuleScaffold
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PaquetesCanceladosScreen(
    onRestaurado: () -> Unit,
    vm: PaquetesViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
    var rows by remember { mutableStateOf<List<RowCancelado>>(emptyList()) }

    LaunchedEffect(Unit) {
        val df = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        rows = vm.getCancelados().map {
            RowCancelado(
                id = it.paqueteId,
                fecha = it.fechaCancelacion.format(df), // Fecha (cancelación)
                cliente = it.clienteNombre,
                cedula = it.clienteCedula,
                tracking = it.tracking
            )
        }
    }

    ModuleScaffold(
        title = "Paquetes cancelados",
        showTopBar = true
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderCancelados()
                Divider()
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(rows, key = { it.id }) { r ->
                        RowCancelados(r)
                        Divider()
                    }
                }
            }
            SnackbarHost(
                hostState = snack,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

private data class RowCancelado(
    val id: String,
    val fecha: String,
    val cliente: String,
    val cedula: String,
    val tracking: String
)

@Composable
private fun HeaderCancelados() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Fecha",   modifier = Modifier.weight(0.24f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Cliente", modifier = Modifier.weight(0.34f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Cédula",  modifier = Modifier.weight(0.20f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Tracking",modifier = Modifier.weight(0.22f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun RowCancelados(
    r: RowCancelado
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(r.fecha,    modifier = Modifier.weight(0.24f), style = MaterialTheme.typography.bodyMedium)
        Text(r.cliente,  modifier = Modifier.weight(0.34f), style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(r.cedula,   modifier = Modifier.weight(0.20f), style = MaterialTheme.typography.bodyMedium)
        Text(r.tracking, modifier = Modifier.weight(0.22f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}


