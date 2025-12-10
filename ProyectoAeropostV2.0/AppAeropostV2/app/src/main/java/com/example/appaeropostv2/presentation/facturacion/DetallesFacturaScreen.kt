package com.example.appaeropostv2.presentation.facturacion

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@Composable
fun DetallesFacturaScreen(
    factura: Facturacion,
    uiState: FacturacionUiState,
    modifier: Modifier = Modifier,
    onGenerarPdfDesdeDetalle: () -> Unit,
    onConsumirPdf: () -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    val pdfUri: Uri? = uiState.pdfUri
    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
            onConsumirPdf()
        }
    }

    // determinar símbolo según la moneda asociada al tracking
    val moneda = uiState.monedaPorTracking[factura.numeroTracking]
    val symbol = when (moneda) {
        Monedas.USD -> "$"
        Monedas.CRC -> "₡"
        Monedas.EUR -> "€"
        null -> ""
    }

    AppScaffold(
        topBar = {},
        bottomBar = {},
        header = {
            GradientHeader(
                title = "Facturación",
                subtitle = "Detalle de factura"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Factura #${factura.idFacturacion}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Cédula cliente: ${factura.cedulaCliente}")
                    Text("Tracking: ${factura.numeroTracking}")
                    Text("Fecha facturación: ${factura.fechaFacturacion}")
                    Text("Peso paquete: ${factura.pesoPaquete} kg")
                    Text("Valor bruto: $symbol${String.format("%.2f", factura.valorBrutoPaquete)}")
                    Text("Producto especial: ${if (factura.productoEspecial) "Sí" else "No"}")
                    Text("Dirección entrega: ${factura.direccionEntrega}")
                    Text(
                        text = "Monto total: $symbol${String.format("%.2f", factura.montoTotal)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Volver")
                }

                Button(
                    onClick = onGenerarPdfDesdeDetalle,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Ver PDF")
                }
            }
        }
    }
}
