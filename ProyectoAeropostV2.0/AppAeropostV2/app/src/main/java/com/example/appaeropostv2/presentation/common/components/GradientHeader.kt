package com.example.appaeropostv2.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.AccentYellow

@Composable
fun GradientHeader(
    title: String,
    subtitle: String
) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.9f))
        Text(subtitle, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(8.dp))
        // Tira amarilla
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AccentYellow)
        )
    }
}

