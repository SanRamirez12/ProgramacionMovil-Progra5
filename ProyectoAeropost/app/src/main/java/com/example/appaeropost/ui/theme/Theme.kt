package com.example.appaeropost.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme

// ★ imports para shapes
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// ===== Esquema CLARO (Light) =====
private val LightColorScheme = lightColorScheme(
    primary            = AeropostBlue,
    onPrimary          = Color.White,
    primaryContainer   = AeropostBlueLight,
    onPrimaryContainer = Color.White,

    secondary            = AeropostBlueLight,
    onSecondary          = Color.White,
    secondaryContainer   = Color(0xFFE9EEFF),
    onSecondaryContainer = AeropostBlue,

    background   = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface      = SurfaceLight,
    onSurface    = OnSurfaceLight,

    outline = OutlineLight
)

// ===== Esquema OSCURO (Dark) =====
private val DarkColorScheme = darkColorScheme(
    primary            = AeropostBlueLight,
    onPrimary          = Color.Black,
    primaryContainer   = AeropostBlueDark,
    onPrimaryContainer = Color.White,

    secondary            = AeropostBlue,
    onSecondary          = Color.White,
    secondaryContainer   = AeropostBlueDark,
    onSecondaryContainer = Color.White,

    background   = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface      = SurfaceDark,
    onSurface    = OnSurfaceDark,

    outline = OutlineDark
)

// ★ Shapes globales (curvatura por defecto en la app)
private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small      = RoundedCornerShape(8.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),   // ← el que usarán Cards grandes, etc.
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Tema global de la app.
 */
@Composable
fun AppAeropostTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        shapes      = AppShapes,     // ★ aplica las shapes
        content     = content
    )
}
