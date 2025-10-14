package com.example.appaeropost.ui.tracking

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun TrackingScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Tracking") {
        // TODO: aquí va la lista/CRUD de tracking (visual por ahora)
        Text("Pantalla de Tracking", modifier = modifier)
    }
}