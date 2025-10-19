package com.example.appaeropost.ui.facturacion

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.appaeropost.domain.Factura

@Composable
fun FacturacionForm(
    vm: FacturacionViewModel,
    modoEdicion: Boolean,                    // <- lo dejamos por si lo usas en el botón
    facturaInicial: Factura? = null,
    onSubmit: (Factura) -> Unit,
    onCancel: () -> Unit
) {
    var cedula by remember { mutableStateOf(TextFieldValue(facturaInicial?.cedulaCliente ?: "")) }
    var fechaEntrega by remember { mutableStateOf(TextFieldValue(facturaInicial?.fechaEntrega ?: "")) }

    val cliente = vm.buscarClientePorCedula(cedula.text)
    val paquetes = remember(cedula.text) { vm.paquetesPorCliente(cedula.text) }

    var trackingSeleccionado by remember {
        mutableStateOf(facturaInicial?.tracking ?: paquetes.firstOrNull()?.tracking.orElse())
    }
    val paquete = vm.paquetePorTracking(trackingSeleccionado)

    val peso = paquete?.pesoKg ?: facturaInicial?.pesoKg ?: 0.0
    val valorPaquete = paquete?.valorEstimado ?: facturaInicial?.valorTotalPaquete ?: 0.0
    val esEspecial = paquete?.esEspecial ?: facturaInicial?.productoEspecial ?: false
    val direccion = cliente?.direccion ?: facturaInicial?.direccionEntrega ?: ""

    val monto = remember(peso, valorPaquete, esEspecial) {
        (valorPaquete + if (esEspecial) 5600.0 else 0.0)
    }

    val scroll = rememberScrollState()

    // Quitar el título interno y hacer scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = cedula, onValueChange = { cedula = it },
            label = { Text("Cédula del Cliente") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fechaEntrega, onValueChange = { fechaEntrega = it },
            label = { Text("Fecha de Entrega (yyyy-MM-dd)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        Divider()

        // Subtítulos con color secundario del tema
        Text(
            "Datos del Cliente",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        ReadonlyField("Nombre", cliente?.nombre.orEmpty())
        ReadonlyField("Correo", cliente?.correo.orEmpty())
        ReadonlyField("Teléfono", cliente?.telefono.orEmpty())
        ReadonlyField("Dirección", direccion)

        Spacer(Modifier.height(8.dp))

        Text(
            "Datos del Paquete",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        if (paquetes.isNotEmpty()) {
            ExposedDropdownMenuBoxSample(
                label = "Número de Tracking",
                opciones = paquetes.map { it.tracking },
                seleccionado = trackingSeleccionado,
                onSeleccion = { trackingSeleccionado = it }
            )
        } else {
            ReadonlyField("Número de Tracking", trackingSeleccionado)
        }

        ReadonlyField("Peso (lb)", peso.toString())
        ReadonlyField("Valor Total del Paquete", valorPaquete.toString())
        ReadonlyField("Producto Especial", if (esEspecial) "Sí" else "No")
        ReadonlyField("Monto Total a Pagar", String.format("%,.2f", monto))

        Spacer(Modifier.height(4.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val f = Factura(
                    id = facturaInicial?.id ?: java.util.UUID.randomUUID().toString(),
                    tracking = trackingSeleccionado,
                    cedulaCliente = cedula.text,
                    pesoKg = peso,
                    valorTotalPaquete = valorPaquete,
                    productoEspecial = esEspecial,
                    fechaEntrega = fechaEntrega.text,
                    montoTotal = monto,
                    direccionEntrega = direccion
                )
                onSubmit(f)
            }) { Text(if (modoEdicion) "Guardar Cambios" else "Registrar Factura") }

            OutlinedButton(onClick = onCancel) { Text("Volver a la Lista") }
        }

        // Padding extra al final para no chocar con la bottom bar
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ReadonlyField(label: String, value: String) {
    OutlinedTextField(
        value = value, onValueChange = {}, label = { Text(label) },
        readOnly = true, modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenuBoxSample(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onSeleccion: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = seleccionado, onValueChange = {}, readOnly = true,
            label = { Text(label) }, modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opciones.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = {
                    onSeleccion(opt); expanded = false
                })
            }
        }
    }
}

private fun String?.orElse() = this ?: ""

