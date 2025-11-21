package com.example.appaeropostv2.presentation.usuario

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
import com.example.appaeropostv2.domain.enums.RolesUsuarios
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.presentation.common.components.ModuleTable
import com.example.appaeropostv2.presentation.common.components.TableColumn
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

/**
 * Pantalla principal del módulo de Usuarios.
 */
@Composable
fun UsuarioScreen(
    usuarios: List<Usuario>,
    modifier: Modifier = Modifier,
    onIrCrearUsuario: () -> Unit,              // Navega a CrearUsuarioScreen
    onEditarUsuario: (Usuario) -> Unit,        // Navega a EditarUsuarioScreen
    onVerDetallesUsuario: (Usuario) -> Unit,   // Navega a DetallesUsuarioScreen
    onDeshabilitarUsuario: (Usuario) -> Unit   // Navega a DeshabilitarUsuarioScreen
) {
    var query by rememberSaveable { mutableStateOf("") }

    // Filtro simple por cédula, username o nombre
    val usuariosFiltrados = remember(usuarios, query) {
        if (query.isBlank()) {
            usuarios
        } else {
            val q = query.trim().lowercase()
            usuarios.filter { usuario ->
                val nombre = usuario.nombreUsuario.lowercase()
                val cedula = usuario.cedulaUsuario.lowercase()
                val username = usuario.username.lowercase()

                nombre.contains(q) || cedula.contains(q) || username.contains(q)
            }
        }
    }

    AppScaffold(
        topBar = {},
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Usuarios"
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
                text = "Usuarios",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Barra de búsqueda
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por cédula, usuario o nombre") },
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

            // Botón Crear usuario
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onIrCrearUsuario,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Crear usuario")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tabla de usuarios
            if (usuariosFiltrados.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No se encontraron usuarios con ese criterio.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                ModuleTable(
                    items = usuariosFiltrados,
                    columns = listOf(
                        TableColumn<Usuario>(
                            header = "Nombre",
                            weight = 1.2f,
                        ) { usuario ->
                            usuario.nombreUsuario
                        },
                        TableColumn<Usuario>(
                            header = "Cédula",
                            weight = 1f
                        ) { usuario ->
                            usuario.cedulaUsuario
                        },
                        TableColumn<Usuario>(
                            header = "Rol",
                            weight = 0.8f
                        ) { usuario ->
                            when (val rol = usuario.rolUsuario) {
                                is RolesUsuarios -> rol.name
                                else -> rol.toString()
                            }
                        }
                    ),
                    onEditClick = onEditarUsuario,
                    onDetailsClick = onVerDetallesUsuario,
                    onDisableClick = onDeshabilitarUsuario,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

