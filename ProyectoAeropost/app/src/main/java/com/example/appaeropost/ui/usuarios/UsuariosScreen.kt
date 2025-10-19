package com.example.appaeropost.ui.usuarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
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
fun UsuariosScreen(
    vm: UsuarioViewModel = viewModel(),
    onNuevoClick: () -> Unit = {},
    onEditarClick: (String) -> Unit = {}, // username
) {
    val state by vm.state.collectAsState()

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
            HeaderWithLogoUsuarios()

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

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            UsuariosTable(
                rows = state.rows,
                onEditarClick = onEditarClick
            )

            if (state.canLoadMore) {
                Button(
                    onClick = vm::loadMore,
                    modifier = Modifier.align(Alignment.End)
                ) { Text("Mostrar más") }
            }

            if (state.error != null) {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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
private fun HeaderWithLogoUsuarios() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Reemplaza por tu asset; usa un placeholder si aún no existe
            Image(
                painter = painterResource(id = R.drawable.logo_usuarios),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Usuarios",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.background
                )
                Text(
                    "Buscar, crear y editar usuarios",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/** Tabla: Nombre | Cédula | Rol | (Editar) */
@Composable
private fun UsuariosTable(
    rows: List<UsuarioUi>,
    onEditarClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        TableHeaderUsuarios()
        Divider()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(rows, key = { it.id }) { u ->
                UsuarioRow(
                    nombre = u.nombre,
                    cedula = u.cedula,
                    rol = u.rol,
                    onEdit = { onEditarClick(u.id) }
                )
                Divider()
            }
        }
    }
}

@Composable
private fun TableHeaderUsuarios() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Nombre",
            modifier = Modifier.weight(0.45f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Cédula",
            modifier = Modifier.weight(0.27f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Rol",
            modifier = Modifier.weight(0.18f),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(0.10f)) // espacio del botón Editar
    }
}

@Composable
private fun UsuarioRow(
    nombre: String,
    cedula: String,
    rol: String,
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
            modifier = Modifier.weight(0.45f),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            cedula,
            modifier = Modifier.weight(0.27f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            rol,
            modifier = Modifier.weight(0.18f),
            style = MaterialTheme.typography.bodyMedium
        )
        Box(modifier = Modifier.weight(0.10f), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    }
}
