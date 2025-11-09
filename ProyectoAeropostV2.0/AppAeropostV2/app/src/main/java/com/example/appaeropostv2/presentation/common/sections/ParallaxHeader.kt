package com.example.appaeropostv2.presentation.common.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.graphicsLayer
import com.example.appaeropostv2.core.designsystem.theme.AeroBlue
import com.example.appaeropostv2.core.designsystem.theme.AeroBlueLight

/**
 * Header con degradado + parallax (se mueve más lento que el scroll)
 * y un fade suave hacia blanco en la parte inferior.
 */
@Composable
fun ParallaxHeader(
    listState: LazyListState,
    height: Int = 220,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val offsetPx = if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 0
    val parallax = -offsetPx / 2f  // se “mueve” la mitad del scroll

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .graphicsLayer { translationY = parallax } // efecto parallax
            .background(
                Brush.verticalGradient(listOf(AeroBlue, AeroBlueLight))
            )
            .padding(contentPadding)
    ) {
        Column(content = content)
    }

    // Fade hacia blanco (debajo del header)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(AeroBlueLight.copy(alpha = 0.8f), Color.Transparent)
                )
            )
    )
}
