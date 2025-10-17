package com.example.appaeropost.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
private fun SimpleAppBar(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    leading: (@Composable RowScope.() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    // Fondo del appbar = surface (blanco, según tu Theme)
    Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leading != null) {
                    Row(content = leading)
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    // Estilo deseado: displaySmall + SemiBold + primary
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Row(content = actions)
        }
    }
}

/** Scaffold base para módulos */
@Composable
fun ModuleScaffold(
    title: String? = null, // si es null, no se muestra TopBar
    showTopBar: Boolean = true,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    leading: (@Composable RowScope.() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floating: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (showTopBar && title != null) {
                SimpleAppBar(
                    title = title,
                    titleColor = titleColor,
                    leading = leading,
                    actions = actions
                )
            }
        },
        floatingActionButton = floating,
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { content() }
    }
}

