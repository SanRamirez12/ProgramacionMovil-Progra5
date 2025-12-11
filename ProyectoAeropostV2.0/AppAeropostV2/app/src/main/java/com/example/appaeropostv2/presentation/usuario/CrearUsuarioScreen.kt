package com.example.appaeropostv2.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*   // ← IMPORT GLOBAL DE M3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.Genero
import com.example.appaeropostv2.domain.enums.RolesUsuarios
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(
    onGuardarUsuario: (Usuario, String) -> Unit,   // ← ahora también la password
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
){
    // --- Estados del formulario ---
    var nombre by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var generoSeleccionado by remember { mutableStateOf<Genero?>(null) }
    var rolSeleccionado by remember { mutableStateOf<RolesUsuarios?>(null) }
    val fechaRegistro = remember { LocalDate.now() }

    val estadoPorDefecto = Estados.HABILITADO

    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
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
                subtitle = "Crear usuario"
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
                    // Permitimos dígitos y guiones, típico formato CR
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

            // Estado (solo lectura, HABILITADO)
            OutlinedTextField(
                value = estadoPorDefecto.name,
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

            // Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario (username)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            // Confirm Password (solo para validar, NO se guarda)
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
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
                        // Validación básica del formulario
                        when {
                            nombre.isBlank() ||
                                    cedula.isBlank() ||
                                    generoSeleccionado == null ||
                                    rolSeleccionado == null ||
                                    correo.isBlank() ||
                                    telefono.isBlank() ||
                                    username.isBlank() ||
                                    password.isBlank() ||
                                    confirmPassword.isBlank() -> {
                                errorForm = "Por favor, complete todos los campos requeridos."
                            }

                            password != confirmPassword -> {
                                errorForm = "Las contraseñas no coinciden."
                            }

                            else -> {
                                errorForm = null
                                val nuevoUsuario = Usuario(
                                    idUsuario = 0,
                                    nombreUsuario = nombre,
                                    cedulaUsuario = cedula,
                                    generoUsuario = generoSeleccionado!!,
                                    fechaRegistro = fechaRegistro,
                                    estadoUsuario = estadoPorDefecto,
                                    rolUsuario = rolSeleccionado!!,
                                    correoUsuario = correo,
                                    telefonoUsuario = telefono,
                                    username = username,

                                    // valores dummy, el repo los rellenará con el hash real
                                    passwordHash = "",
                                    passwordSalt = "",
                                    passwordIterations = 0
                                )
                                onGuardarUsuario(nuevoUsuario, password)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Guardar")
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


