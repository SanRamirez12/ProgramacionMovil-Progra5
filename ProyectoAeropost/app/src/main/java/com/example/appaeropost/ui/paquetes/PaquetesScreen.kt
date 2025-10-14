package com.example.appaeropost.ui.paquetes

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun PaquetesScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Paquetes") {
        // TODO: aquí va la lista/CRUD de paquetes (visual por ahora)
        Text("Pantalla de Paquetes", modifier = modifier)
    }
}