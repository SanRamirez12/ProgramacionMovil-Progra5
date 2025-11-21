package com.example.appaeropostv2.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Genero
import com.example.appaeropostv2.domain.enums.RolesUsuarios
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarUsuarioScreen(
    usuarioOriginal: Usuario,
    onGuardarCambios: (Usuario) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Estados del formulario inicializados con el usuario recibido ---
    var nombre by remember(usuarioOriginal.idUsuario) {
        mutableStateOf(usuarioOriginal.nombreUsuario)
    }
    var cedula by remember(usuarioOriginal.idUsuario) {
        mutableStateOf(usuarioOriginal.cedulaUsuario)
    }
    var generoSeleccionado by remember(usuarioOriginal.idUsuario) {
        mutableStateOf<Genero?>(usuarioOriginal.generoUsuario)
    }
    var rolSeleccionado by remember(usuarioOriginal.idUsuario) {
        mutableStateOf<RolesUsuarios?>(usuarioOriginal.rolUsuario)
    }

    val fechaRegistro = usuarioOriginal.fechaRegistro
    val estadoActual = usuarioOriginal.estadoUsuario

    var correo by remember(usuarioOriginal.idUsuario) {
        mutableStateOf(usuarioOriginal.correoUsuario)
    }
    var telefono by remember(usuarioOriginal.idUsuario) {
        mutableStateOf(usuarioOriginal.telefonoUsuario)
    }
    // Username se muestra pero NO se puede editar
    val username = usuarioOriginal.username

    // Para cambio opcional de contraseña
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorForm by remember { mutableStateOf<String?>(null) }

    val fechaTexto = remember(fechaRegistro) {
        fechaRegistro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    // Dropdown states
    var expandedGenero by remember { mutableStateOf(false) }
    var expandedRol by remember { mutableStateOf(false) }

    AppScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.ScreenPadding,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {}
        },
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Editar usuario"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Cédula
            OutlinedTextField(
                value = cedula,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '-' }) {
                        cedula = input
                    }
                },
                label = { Text("Cédula") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // Género (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expandedGenero,
                onExpandedChange = { expandedGenero = !expandedGenero }
            ) {
                OutlinedTextField(
                    value = generoSeleccionado?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Género") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar género"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )
                ExposedDropdownMenu(
                    expanded = expandedGenero,
                    onDismissRequest = { expandedGenero = false }
                ) {
                    Genero.values().forEach { genero ->
                        DropdownMenuItem(
                            text = { Text(genero.name) },
                            onClick = {
                                generoSeleccionado = genero
                                expandedGenero = false
                            }
                        )
                    }
                }
            }

            // Fecha (solo lectura)
            OutlinedTextField(
                value = fechaTexto,
                onValueChange = {},
                label = { Text("Fecha de registro") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                singleLine = true
            )

            // Estado (solo lectura, se cambia en otra pantalla)
            OutlinedTextField(
                value = estadoActual.name,
                onValueChange = {},
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                singleLine = true
            )

            // Rol (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expandedRol,
                onExpandedChange = { expandedRol = !expandedRol }
            ) {
                OutlinedTextField(
                    value = rolSeleccionado?.name ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text("Rol de usuario") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Seleccionar rol"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors()
                )
                ExposedDropdownMenu(
                    expanded = expandedRol,
                    onDismissRequest = { expandedRol = false }
                ) {
                    RolesUsuarios.values().forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(rol.name) },
                            onClick = {
                                rolSeleccionado = rol
                                expandedRol = false
                            }
                        )
                    }
                }
            }

            // Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            // Teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '-' || it == ' ' }) {
                        telefono = input
                    }
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )

            // Username (NO editable)
            OutlinedTextField(
                value = username,
                onValueChange = {},
                label = { Text("Nombre de usuario (username)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = false
            )

            // Password nueva (opcional)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nueva contraseña (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar nueva contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            if (errorForm != null) {
                Text(
                    text = errorForm ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones Guardar / Cancelar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Validación básica
                        when {
                            nombre.isBlank() ||
                                    cedula.isBlank() ||
                                    generoSeleccionado == null ||
                                    rolSeleccionado == null ||
                                    correo.isBlank() ||
                                    telefono.isBlank() -> {
                                errorForm = "Por favor, complete todos los campos requeridos."
                            }

                            password.isNotBlank() || confirmPassword.isNotBlank() -> {
                                if (password != confirmPassword) {
                                    errorForm = "Las contraseñas no coinciden."
                                    return@Button
                                } else {
                                    errorForm = null
                                    val usuarioActualizado = usuarioOriginal.copy(
                                        nombreUsuario = nombre,
                                        cedulaUsuario = cedula,
                                        generoUsuario = generoSeleccionado!!,
                                        rolUsuario = rolSeleccionado!!,
                                        correoUsuario = correo,
                                        telefonoUsuario = telefono,
                                        password = password // nueva
                                    )
                                    onGuardarCambios(usuarioActualizado)
                                }
                            }

                            else -> {
                                errorForm = null
                                val usuarioActualizado = usuarioOriginal.copy(
                                    nombreUsuario = nombre,
                                    cedulaUsuario = cedula,
                                    generoUsuario = generoSeleccionado!!,
                                    rolUsuario = rolSeleccionado!!,
                                    correoUsuario = correo,
                                    telefonoUsuario = telefono,
                                    // password se mantiene igual
                                )
                                onGuardarCambios(usuarioActualizado)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Guardar cambios")
                }

                TextButton(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}


