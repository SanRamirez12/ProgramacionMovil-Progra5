package com.example.appaeropost.ui.facturacion

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
import com.example.appaeropost.domain.Factura
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun FacturacionScreen(
    vm: FacturacionViewModel = viewModel(),
    onNuevoClick: () -> Unit = {},
    onEditarClick: (String) -> Unit = {},
    onFacturarPendientes: () -> Unit = {}
) {
    val state by vm.ui.collectAsState()

    ModuleScaffold(
        title = null,
        showTopBar = false,
        actions = {},
        floating = {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderFacturacion()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onFacturarPendientes) { Text("Facturar pendientes") }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onNuevoClick,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nueva factura")
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            FacturasTable(
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

            if (state.items.isEmpty() && !state.isLoading) {
                Text(
                    text = "No hay facturas registradas.",
                    modifier = Modifier.padding(top = 18.dp).alpha(0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun HeaderFacturacion() {
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
                painter = painterResource(id = R.drawable.logo_facturacion), // pon tu asset
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Facturación",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.background
                )
                Text("Crear, editar y revisar facturas", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** Tabla: Tracking | Entrega | Monto | (lápiz) */
@Composable
private fun FacturasTable(
    rows: List<FacturaUi>,
    onEditarClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
        TableHeader()
        Divider()
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            items(rows, key = { it.id }) { f ->
                TableRow(
                    tracking = f.tracking,
                    entrega = f.fechaEntrega,
                    monto = f.monto,
                    onEdit = { onEditarClick(f.id) }
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
        Text("Tracking", modifier = Modifier.weight(0.46f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Entrega",  modifier = Modifier.weight(0.28f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Monto",    modifier = Modifier.weight(0.20f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(0.06f)) // espacio para el lapicito
    }
}

@Composable
private fun TableRow(
    tracking: String,
    entrega: String,
    monto: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(tracking, modifier = Modifier.weight(0.46f), style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(entrega,  modifier = Modifier.weight(0.28f), style = MaterialTheme.typography.bodyMedium)
        Text(monto,    modifier = Modifier.weight(0.20f), style = MaterialTheme.typography.bodyMedium)
        Box(modifier = Modifier.weight(0.06f), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
        }
    }
}

/* --------- Mappers UI --------- */
private data class FacturaUi(
    val id: String,
    val tracking: String,
    val fechaEntrega: String,
    val monto: String
)

private fun Factura.toUi(): FacturaUi =
    FacturaUi(
        id = id,
        tracking = tracking,
        fechaEntrega = fechaEntrega,
        monto = String.format("%,.2f", montoTotal)
    )



