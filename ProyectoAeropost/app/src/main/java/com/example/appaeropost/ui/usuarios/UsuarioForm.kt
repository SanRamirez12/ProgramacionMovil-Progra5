package com.example.appaeropost.ui.usuarios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appaeropost.domain.EstadoUsuario
import com.example.appaeropost.domain.Genero
import com.example.appaeropost.domain.Rol
import java.time.LocalDate
import java.util.regex.Pattern

/**
 * Estado del formulario de Usuarios, compartido por Nuevo/Editar.
 * En edición, password puede omitirse (no cambiar).
 */
data class UsuarioFormState(
    val nombre: String = "",
    val cedula: String = "",
    val genero: Genero = Genero.NO_DECLARA,
    val fechaRegistro: LocalDate = LocalDate.now(),
    val estado: EstadoUsuario = EstadoUsuario.HABILITADO,
    val rol: Rol = Rol.OPERADOR,
    val correo: String = "",
    val username: String = "",
    val password: String = "",
    val confirmarPassword: String = ""
) {
    fun isEmailValid(): Boolean {
        val p = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return p.matcher(correo).matches()
    }
    fun requiredFilled(isEdit: Boolean): Boolean {
        val base = nombre.isNotBlank() && cedula.isNotBlank() && correo.isNotBlank() && username.isNotBlank()
        val passOk = if (isEdit) (password.isBlank() || password == confirmarPassword) else
            (password.isNotBlank() && password == confirmarPassword)
        return base && passOk && isEmailValid()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioForm(
    initial: UsuarioFormState,
    isEdit: Boolean,
    usernameEditable: Boolean = !isEdit,
    onSubmit: (UsuarioFormState) -> Unit,
    onCancel: () -> Unit
) {
    var state by remember { mutableStateOf(initial) }
    var generoExpanded by remember { mutableStateOf(false) }
    var estadoExpanded by remember { mutableStateOf(false) }
    var rolExpanded by remember { mutableStateOf(false) }

    val canSubmit = state.requiredFilled(isEdit)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { state = state.copy(nombre = it) },
            label = { Text("Nombre completo*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = state.cedula,
            onValueChange = { state = state.copy(cedula = it) },
            label = { Text("Cédula*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        // Género
        ExposedDropdownMenuBox(
            expanded = generoExpanded,
            onExpandedChange = { generoExpanded = it }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = state.genero.name.lowercase().replaceFirstChar { it.titlecase() },
                onValueChange = {},
                label = { Text("Género") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = generoExpanded, onDismissRequest = { generoExpanded = false }) {
                Genero.values().forEach { g ->
                    DropdownMenuItem(
                        text = { Text(g.name.lowercase().replaceFirstChar { it.titlecase() }) },
                        onClick = { state = state.copy(genero = g); generoExpanded = false }
                    )
                }
            }
        }
        // Estado
        ExposedDropdownMenuBox(
            expanded = estadoExpanded,
            onExpandedChange = { estadoExpanded = it }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = if (state.estado == EstadoUsuario.HABILITADO) "Habilitado" else "Deshabilitado",
                onValueChange = {},
                label = { Text("Estado*") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = estadoExpanded, onDismissRequest = { estadoExpanded = false }) {
                listOf(EstadoUsuario.HABILITADO, EstadoUsuario.DESHABILITADO).forEach { e ->
                    DropdownMenuItem(
                        text = { Text(if (e == EstadoUsuario.HABILITADO) "Habilitado" else "Deshabilitado") },
                        onClick = { state = state.copy(estado = e); estadoExpanded = false }
                    )
                }
            }
        }
        // Rol
        ExposedDropdownMenuBox(
            expanded = rolExpanded,
            onExpandedChange = { rolExpanded = it }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = state.rol.name.lowercase().replaceFirstChar { it.titlecase() },
                onValueChange = {},
                label = { Text("Rol*") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rolExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = rolExpanded, onDismissRequest = { rolExpanded = false }) {
                Rol.values().forEach { r ->
                    DropdownMenuItem(
                        text = { Text(r.name.lowercase().replaceFirstChar { it.titlecase() }) },
                        onClick = { state = state.copy(rol = r); rolExpanded = false }
                    )
                }
            }
        }
        OutlinedTextField(
            value = state.correo,
            onValueChange = { state = state.copy(correo = it) },
            label = { Text("Correo*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.correo.isNotBlank() && !state.isEmailValid(),
            supportingText = {
                if (state.correo.isNotBlank() && !state.isEmailValid()) Text("Correo no válido")
            }
        )
        OutlinedTextField(
            value = state.username,
            onValueChange = { if (usernameEditable) state = state.copy(username = it) },
            label = { Text("Username*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = usernameEditable
        )

        // Contraseña (obligatoria solo en Nuevo)
        OutlinedTextField(
            value = state.password,
            onValueChange = { state = state.copy(password = it) },
            label = { Text(if (isEdit) "Nueva contraseña (opcional)" else "Contraseña*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = state.confirmarPassword,
            onValueChange = { state = state.copy(confirmarPassword = it) },
            label = { Text(if (isEdit) "Confirmar nueva contraseña" else "Confirmar contraseña*") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = state.confirmarPassword.isNotBlank() && state.password != state.confirmarPassword,
            supportingText = {
                if (state.confirmarPassword.isNotBlank() && state.password != state.confirmarPassword)
                    Text("Las contraseñas no coinciden")
            }
        )

        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = onCancel) { Text("Cancelar") }
            Spacer(Modifier.width(8.dp))
            Button(
                enabled = canSubmit,
                onClick = { onSubmit(state) }
            ) { Text("Guardar") }
        }
    }
}
