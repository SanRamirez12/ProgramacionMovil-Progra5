package com.example.appaeropost.ui.analisis

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun AnalisisScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Analisis") {
        // TODO: aquí va la lista/CRUD de analisis (visual por ahora)
        Text("Pantalla de Analisis", modifier = modifier)
    }
}