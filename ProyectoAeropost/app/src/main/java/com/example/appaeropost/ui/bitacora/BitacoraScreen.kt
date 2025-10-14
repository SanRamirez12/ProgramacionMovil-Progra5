package com.example.appaeropost.ui.bitacora

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun BitacoraScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Bitacora") {
        // TODO: aquí va la lista/CRUD de facturacion (visual por ahora)
        Text("Pantalla de Bitacora", modifier = modifier)
    }
}