package com.example.appaeropostv2.presentation.loadingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.AccentYellow
import com.example.appaeropostv2.core.designsystem.theme.AeroBlue
import com.example.appaeropostv2.core.designsystem.theme.AeroBlueDark
import com.example.appaeropostv2.core.designsystem.theme.Brushes

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brushes.headerGradient())
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Icon container (gris/blanco) + puntito amarillo ---
            Box(
                modifier = Modifier
                    .size(110.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.55f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Inventory,
                        contentDescription = "Aeropost Logo",
                        tint = AeroBlueDark,
                        modifier = Modifier.size(56.dp)
                    )
                }

                // Puntito amarillo (abajo-derecha)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 10.dp, y = 10.dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(AccentYellow)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Aeropost",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Gestión de Envíos Internacionales",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.80f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // --- “tirillo” de 3 puntos ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Dot(isActive = false)
                Dot(isActive = true)  // el del centro resaltado como en muchas pantallas
                Dot(isActive = false)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Cargando...",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.70f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator(
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.25f)
            )
        }
    }
}

@Composable
private fun Dot(isActive: Boolean) {
    val color = if (isActive) Color.White else Color.White.copy(alpha = 0.35f)
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}
