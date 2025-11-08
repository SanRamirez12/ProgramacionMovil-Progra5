package com.example.appaeropostv2.presentation.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Brushes
import com.example.appaeropostv2.core.designsystem.theme.Dimens

/**
 * AppScaffold con header degradado reutilizable.
 * El slot 'header' se dibuja sobre el área degradada.
 */
@Composable
fun AppScaffold(
    header: (@Composable ColumnScope.() -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        snackbarHost = snackbarHost
    ) { inner ->
        Column(Modifier.fillMaxSize()) {
            // — Header degradado (alto fijo; se hereda a todas las screens que lo necesiten)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Brushes.headerGradient())
                    .padding(horizontal = Dimens.ScreenPadding, vertical = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column { header?.invoke(this) }
            }

            // — Contenido de la pantalla
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


