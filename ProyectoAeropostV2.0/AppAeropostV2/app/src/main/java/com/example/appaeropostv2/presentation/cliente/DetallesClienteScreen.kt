package com.example.appaeropostv2.presentation.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@Composable
fun DetallesClienteScreen(
    cliente: Cliente,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                subtitle = "Detalles de cliente"
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
            ReadonlyField(
                label = "Nombre completo",
                value = cliente.nombreCliente
            )

            ReadonlyField(
                label = "Cédula",
                value = cliente.cedulaCliente
            )

            ReadonlyField(
                label = "Teléfono",
                value = cliente.telefonoCliente
            )

            ReadonlyField(
                label = "Correo electrónico",
                value = cliente.correoCliente
            )

            ReadonlyField(
                label = "Tipo de cliente",
                value = cliente.tipoCliente.name
            )

            ReadonlyField(
                label = "Estado",
                value = cliente.estadoCliente.name
            )

            ReadonlyField(
                label = "Dirección de entrega",
                value = cliente.direccionEntrega
            )
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
        singleLine = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.surface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
