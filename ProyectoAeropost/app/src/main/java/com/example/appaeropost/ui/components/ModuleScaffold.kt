package com.example.appaeropost.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * App bar estable (no usa APIs experimentales de Material3).
 * Muestra un título a la izquierda y un área de acciones a la derecha.
 */
@Composable
private fun SimpleAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(tonalElevation = 1.dp) {
        // Respetamos la barra de estado y damos altura típica de 56.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Row(content = actions)
        }
    }
}

/**
 * Scaffold base para módulos:
 * - App bar estable con título y acciones opcionales
 * - Slot para FloatingActionButton opcional
 * - Contenido que respeta el padding del Scaffold
 */
@Composable
fun ModuleScaffold(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    floating: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { SimpleAppBar(title = title, actions = actions) },
        floatingActionButton = floating,
        contentWindowInsets = WindowInsets.systemBars // ajusta automáticamente insets del sistema
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}
