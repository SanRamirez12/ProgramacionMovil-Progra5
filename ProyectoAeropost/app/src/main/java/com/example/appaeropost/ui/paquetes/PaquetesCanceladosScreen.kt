package com.example.appaeropost.ui.paquetes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropost.domain.paquetes.PaqueteCancelado
import com.example.appaeropost.ui.components.ModuleScaffold
import kotlinx.coroutines.launch

@Composable
fun PaquetesCanceladosScreen(
    onRestaurado: () -> Unit,
    vm: PaquetesViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snack = remember { SnackbarHostState() }
    var items by remember { mutableStateOf<List<PaqueteCancelado>>(emptyList()) }

    LaunchedEffect(Unit) { items = vm.getCancelados() }

    ModuleScaffold(
        title = "Paquetes cancelados",
        showTopBar = true
    ) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.paqueteId }) { it ->
                    ElevatedCard {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Tracking: ${it.tracking}", style = MaterialTheme.typography.titleMedium)
                            Text("Cliente: ${it.clienteNombre} · ${it.clienteCedula}")
                            Text("Motivo: ${it.motivo}")
                            OutlinedButton(onClick = {
                                scope.launch {
                                    vm.restaurarPaquete(it.paqueteId) {
                                        scope.launch { snack.showSnackbar("Paquete restaurado.") }
                                        scope.launch { items = vm.getCancelados() }
                                        onRestaurado()
                                    }
                                }
                            }) { Text("Restaurar") }
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snack,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

