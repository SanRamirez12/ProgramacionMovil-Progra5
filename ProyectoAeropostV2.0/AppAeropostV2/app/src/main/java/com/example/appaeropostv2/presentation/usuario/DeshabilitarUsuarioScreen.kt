package com.example.appaeropostv2.presentation.usuario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@Composable
fun DeshabilitarUsuarioScreen(
    usuario: Usuario,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val esHabilitado = usuario.estadoUsuario == Estados.HABILITADO

    val tituloTopBar = if (esHabilitado) {
        "Deshabilitar usuario"
    } else {
        "Habilitar usuario"
    }

    val subtituloHeader = if (esHabilitado) {
        "Deshabilitar usuario"
    } else {
        "Habilitar usuario"
    }

    val textoPregunta = if (esHabilitado) {
        "¿Seguro que deseas deshabilitar este usuario?"
    } else {
        "¿Seguro que deseas volver a habilitar este usuario?"
    }

    val textoBotonConfirmar = if (esHabilitado) {
        "Sí, deshabilitar"
    } else {
        "Sí, habilitar"
    }

    val colorPregunta = if (esHabilitado) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

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
                subtitle = subtituloHeader
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

            Text(
                text = textoPregunta,
                style = MaterialTheme.typography.bodyLarge,
                color = colorPregunta
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReadonlyField(
                label = "Nombre completo",
                value = usuario.nombreUsuario
            )
            ReadonlyField(
                label = "Cédula",
                value = usuario.cedulaUsuario
            )
            ReadonlyField(
                label = "Rol",
                value = usuario.rolUsuario.name
            )
            ReadonlyField(
                label = "Username",
                value = usuario.username
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(textoBotonConfirmar)
                }

                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

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
        singleLine = true
    )
}
