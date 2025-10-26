package com.example.appaeropost.ui.paquetes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.ui.components.ModuleScaffold
import kotlinx.coroutines.launch

@Composable
fun PaquetesCancelarScreen(
    paqueteId: String,
    onCancelDone: () -> Unit,
    onCancelAbort: () -> Unit,
    vm: PaquetesViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
    var motivo by remember { mutableStateOf(TextFieldValue("")) }

    ModuleScaffold(
        title = "Cancelar paquete",
        showTopBar = true
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Esto deshabilita el envío dentro de la app. No se elimina; se archiva como cancelado.")
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo de cancelación") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        scope.launch {
                            if (motivo.text.isBlank()) {
                                snack.showSnackbar("Indica un motivo.")
                                return@launch
                            }
                            vm.cancelarPaquete(paqueteId, motivo.text) {
                                scope.launch { snack.showSnackbar("Paquete cancelado.") }
                                onCancelDone()
                            }
                        }
                    }) { Text("Confirmar cancelación") }

                    OutlinedButton(onClick = onCancelAbort) { Text("Volver") }
                }
            }

            // Snackbar anclado abajo
            SnackbarHost(
                hostState = snack,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}
