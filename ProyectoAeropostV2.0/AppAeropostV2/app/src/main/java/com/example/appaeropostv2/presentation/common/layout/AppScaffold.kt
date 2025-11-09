package com.example.appaeropostv2.presentation.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Brushes
import com.example.appaeropostv2.core.designsystem.theme.Dimens

/**
 * Scaffold base de la app.
 * - Si 'header' es null, NO se dibuja zona de header (útil para pantallas con parallax propio).
 * - Si 'header' no es null, se pinta un header degradado reutilizable + un fade sutil inferior.
 */
@Composable
fun AppScaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    header: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val hostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        snackbarHost = {
            if (snackbarHost == {}) {
                SnackbarHost(hostState = hostState)
            } else {
                snackbarHost()
            }
        }
    ) { inner ->
        Column(Modifier.fillMaxSize()) {

            // Header degradado SOLO si se provee el slot
            if (header != null) {
                // Bloque degradado principal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Brushes.headerGradient())
                        .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp)
                ) {
                    Column(content = header)
                }
                // Fade suave hacia el contenido
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.06f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // Contenido de la pantalla
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
            ) {
                content(inner)
            }
        }
    }
}



