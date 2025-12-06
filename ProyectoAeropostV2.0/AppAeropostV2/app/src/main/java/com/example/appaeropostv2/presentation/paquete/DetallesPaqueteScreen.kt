package com.example.appaeropostv2.presentation.paquete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import java.time.format.DateTimeFormatter

@Composable
fun DetallesPaqueteScreen(
    paquete: Paquete,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    AppScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPadding, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) { }
        },
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Módulo",
                subtitle = "Detalles de paquete"
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
                label = "Fecha de registro",
                value = paquete.fechaRegistro.format(formatter)
            )

            ReadonlyField(
                label = "Nombre del cliente",
                value = paquete.nombrecliente
            )

            ReadonlyField(
                label = "Cédula del cliente",
                value = paquete.cedulaCliente
            )

            ReadonlyField(
                label = "Número de tracking",
                value = paquete.numeroTracking
            )

            ReadonlyField(
                label = "Tienda de origen",
                value = paquete.tiendaOrigen.name
            )

            ReadonlyField(
                label = "Casillero",
                value = paquete.casillero.name
            )

            ReadonlyField(
                label = "Peso (kg)",
                value = paquete.pesoPaquete.toString()
            )

            ReadonlyField(
                label = "Valor declarado",
                value = paquete.valorBruto.toPlainString()
            )

            ReadonlyField(
                label = "Moneda",
                value = paquete.monedasPaquete.name
            )

            ReadonlyField(
                label = "Producto especial",
                value = if (paquete.condicionEspecial) "Sí" else "No"
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
