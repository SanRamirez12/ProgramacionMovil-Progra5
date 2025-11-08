package com.example.appaeropostv2.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = AeroBlue,
    onPrimary = Color.White,
    primaryContainer = AeroBlueLight,
    onPrimaryContainer = Color.White,
    background = Color.White,
    onBackground = OnSurfaceHigh,
    surface = SurfaceSoft,
    onSurface = OnSurfaceHigh,
)

private val Dark = darkColorScheme(
    primary = AeroBlueLight,
    onPrimary = Color.Black,
    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE6EAF5),
    surface = Color(0xFF101828),
    onSurface = Color(0xFFE6EAF5),
)

@Composable
fun AeropostTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) Dark else Light,
        typography = AeropostTypography,
        shapes = AeropostShapes,
        content = content
    )
}


