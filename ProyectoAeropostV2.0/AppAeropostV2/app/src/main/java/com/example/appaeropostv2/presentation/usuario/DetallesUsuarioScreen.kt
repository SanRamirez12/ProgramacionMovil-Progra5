package com.example.appaeropostv2.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.format.DateTimeFormatter

@Composable
fun DetallesUsuarioScreen(
    usuario: Usuario,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fechaTexto = usuario.fechaRegistro.format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )

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
                subtitle = "Detalles de usuario"
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
            ReadonlyField(
                label = "Nombre completo",
                value = usuario.nombreUsuario
            )

            // Cédula
            ReadonlyField(
                label = "Cédula",
                value = usuario.cedulaUsuario
            )

            // Género
            ReadonlyField(
                label = "Género",
                value = usuario.generoUsuario.name
            )

            // Fecha de registro
            ReadonlyField(
                label = "Fecha de registro",
                value = fechaTexto
            )

            // Estado
            ReadonlyField(
                label = "Estado",
                value = usuario.estadoUsuario.name
            )

            // Rol
            ReadonlyField(
                label = "Rol de usuario",
                value = usuario.rolUsuario.name
            )

            // Correo
            ReadonlyField(
                label = "Correo electrónico",
                value = usuario.correoUsuario
            )

            // Teléfono
            ReadonlyField(
                label = "Teléfono",
                value = usuario.telefonoUsuario
            )

            // Username (lectura)
            ReadonlyField(
                label = "Nombre de usuario (username)",
                value = usuario.username
            )

            // Importante: contraseña NO se muestra, no existe campo de password aquí
        }
    }
}



/**
 * Campo reutilizable de solo lectura con label + OutlinedTextField deshabilitado.
 */
@Composable
private fun ReadonlyField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        enabled = false,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.surface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
