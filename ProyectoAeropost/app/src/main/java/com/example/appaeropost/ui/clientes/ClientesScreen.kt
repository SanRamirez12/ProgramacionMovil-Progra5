package com.example.appaeropost.ui.clientes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun ClientesScreen(
    vm: ClientesViewModel = viewModel(),
    onNuevoClick: () -> Unit = {},
    onEditarClick: (String) -> Unit = {},
) {
    val state by vm.state.collectAsState()

    ModuleScaffold(
        title = null,              // o showTopBar = false
        showTopBar = false,        // ← oculta la appbar
        actions = {},
        floating = { /* si usas FAB aquí */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Encabezado con loguito + título (el AppBar ya muestra el title, esto es visual opcional)
            HeaderWithLogo()

            // Buscador + botón Nuevo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = vm::onQueryChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Buscar por nombre o cédula") },
                    singleLine = true
                )
                Button(
                    onClick = onNuevoClick,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nuevo")
                }
            }

            // Feedback de carga
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // Tabla
            ClientesTable(
                rows = state.rows,
                onEditarClick = onEditarClick
            )

            // Mostrar más (paginación simple)
            if (state.canLoadMore) {
                Button(
                    onClick = vm::loadMore,
                    modifier = Modifier.align(Alignment.End)
                ) { Text("Mostrar más") }
            }

            // Error
            if (state.error != null) {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Hint cuando no hay búsqueda
            if (state.rows.isEmpty() && state.query.isBlank() && !state.isLoading) {
                Text(
                    text = "Empieza buscando por nombre o cédula.",
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .alpha(0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun HeaderWithLogo() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background // ← fondo blanco
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Si no tienes aún el logo en /res/drawable, usa un ícono temporal
            // y luego reemplázalo por el asset oficial.
            Image(
                painter = painterResource(id = R.drawable.logo_clientes),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Clientes",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Buscar, crear y editar clientes",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/** Tabla con columnas: Nombre | Cédula | Estado | (Editar) */
@Composable
private fun ClientesTable(
    rows: List<ClienteUi>,
    onEditarClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        TableHeader()

        Divider()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(rows, key = { it.id }) { c ->
                TableRow(
                    nombre = c.nombre,
                    cedula = c.cedula,
                    estado = if (c.habilitado) "Habilitado" else "Deshabilitado",
                    onEdit = { onEditarClick(c.id) }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Nombre",
            modifier = Modifier.weight(0.42f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Cédula",
            modifier = Modifier.weight(0.30f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Estado",
            modifier = Modifier.weight(0.18f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(0.10f)) // espacio para el botón Editar
    }
}

@Composable
private fun TableRow(
    nombre: String,
    cedula: String,
    estado: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            nombre,
            modifier = Modifier.weight(0.42f),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            cedula,
            modifier = Modifier.weight(0.30f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            estado,
            modifier = Modifier.weight(0.18f),
            style = MaterialTheme.typography.bodyMedium,
            color = if (estado == "Habilitado") MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error
        )
        Box(modifier = Modifier.weight(0.10f), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    }
}

