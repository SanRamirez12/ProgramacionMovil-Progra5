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

// ===== Esquema CLARO (Light) =====
// Nota: on* = color del texto/ícono encima de la superficie correspondiente.
private val LightColorScheme = lightColorScheme(
    primary            = AeropostBlue,
    onPrimary          = Color.White,
    primaryContainer   = AeropostBlueLight,
    onPrimaryContainer = Color.White,

    secondary          = AeropostBlueLight,
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFFE9EEFF),
    onSecondaryContainer = AeropostBlue,

    background         = BackgroundLight,
    onBackground       = OnSurfaceLight,
    surface            = SurfaceLight,
    onSurface          = OnSurfaceLight,

    outline            = OutlineLight
)

// ===== Esquema OSCURO (Dark) =====
private val DarkColorScheme = darkColorScheme(
    primary            = AeropostBlueLight,
    onPrimary          = Color.Black,
    primaryContainer   = AeropostBlueDark,
    onPrimaryContainer = Color.White,

    secondary          = AeropostBlue,
    onSecondary        = Color.White,
    secondaryContainer = AeropostBlueDark,
    onSecondaryContainer = Color.White,

    background         = BackgroundDark,
    onBackground       = OnSurfaceDark,
    surface            = SurfaceDark,
    onSurface          = OnSurfaceDark,

    outline            = OutlineDark
)

/**
 * Tema global de la app.
 * - Por defecto **NO** usa colores dinámicos para mantener la marca (puedes cambiarlo con dynamicColor = true).
 * - Si activas dynamicColor en Android 12+, se respetará el modo claro/oscuro pero con tonos del sistema.
 */
@Composable
fun AppAeropostTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // ← mantenemos branding por defecto
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
        typography = Typography,
        content = content
    )
}
