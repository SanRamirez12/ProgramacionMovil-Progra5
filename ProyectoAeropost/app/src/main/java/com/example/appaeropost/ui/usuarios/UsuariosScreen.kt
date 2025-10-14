package com.example.appaeropost.ui.usuarios

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun UsuariosScreen(modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Usuarios") {
        // TODO: aquí va la lista/CRUD de usuarios (visual por ahora)
        Text("Pantalla de Usuarios", modifier = modifier)
    }
}