package com.example.appaeropostv2.presentation.paquete

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
fun EliminarPaqueteScreen(
    paquete: Paquete,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
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
                subtitle = "Eliminar paquete"
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

            OutlinedTextField(
                value = paquete.nombrecliente,
                onValueChange = {},
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors()
            )

            OutlinedTextField(
                value = paquete.cedulaCliente,
                onValueChange = {},
                label = { Text("Cédula del cliente") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors()
            )

            OutlinedTextField(
                value = paquete.numeroTracking,
                onValueChange = {},
                label = { Text("Número de tracking") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors()
            )

            OutlinedTextField(
                value = paquete.fechaRegistro.format(formatter),
                onValueChange = {},
                label = { Text("Fecha de registro") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Esta acción eliminará el paquete de forma definitiva. ¿Deseas continuar?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Sí, eliminar")
                }
            }
        }
    }
}
