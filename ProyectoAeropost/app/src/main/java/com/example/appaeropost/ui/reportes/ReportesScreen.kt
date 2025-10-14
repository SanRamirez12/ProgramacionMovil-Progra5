package com.example.appaeropost.ui.reportes

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun ReportesScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Reportes") {
        // TODO: aquí va la lista/CRUD de reportes (visual por ahora)
        Text("Pantalla de Reportes", modifier = modifier)
    }
}