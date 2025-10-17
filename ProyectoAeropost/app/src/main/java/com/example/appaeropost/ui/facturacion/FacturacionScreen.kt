package com.example.appaeropost.ui.facturacion

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun FacturacionScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Facturación") {
        // TODO: aquí va la lista/CRUD de facturacion (visual por ahora)
        Text("Pantalla de Facturacion", modifier = modifier)
    }
}