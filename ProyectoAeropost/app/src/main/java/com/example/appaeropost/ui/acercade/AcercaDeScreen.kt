package com.example.appaeropost.ui.acercade

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun AcercaDeScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Acerca de") {
        // TODO: aquí va la lista/CRUD de acerca de (visual por ahora)
        Text("Pantalla de Acerca De", modifier = modifier)
    }
}