package com.example.appaeropost.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appaeropost.R

@Composable
fun LoginScreen(
    onLoginOk: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado de demo (en producción: ViewModel)
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val cs = MaterialTheme.colorScheme     // atajo a la paleta del tema
    val shapeLg = RoundedCornerShape(24.dp)

    val scroll = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(scroll),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.widthIn(max = 420.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // === Logo (más grande) ============================================================
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val painter = runCatching { painterResource(id = R.drawable.logo_aeropost) }.getOrNull()
                if (painter != null) {
                    Image(
                        painter = painter,
                        contentDescription = "Aeropost",
                        modifier = Modifier.size(72.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Flight,
                        contentDescription = "Aeropost",
                        tint = cs.primary,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }

            // === Título (usa Typography del tema) =============================================
            Text(
                text = "Ingrese sus\ncredenciales",
                style = MaterialTheme.typography.displaySmall, // define el “look” en Type.kt
                color = cs.primary
            )

            // === Usuario ======================================================================
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Nombre de Usuario", style = MaterialTheme.typography.labelLarge, color = cs.onSurface)
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    placeholder = { Text("Ingrese su usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // === Contraseña ===================================================================
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Contraseña", style = MaterialTheme.typography.labelLarge, color = cs.onSurface)
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    placeholder = { Text("Ingrese su contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // === Acciones =====================================================================
            if (error != null) {
                Text(
                    "Error: $error",
                    color = cs.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (user == "adminKing" && pass == "123456") onLoginOk()
                        else error = "Credenciales inválidas"
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primaryContainer, // tomado del tema
                        contentColor   = cs.onPrimaryContainer
                    ),
                    shape = shapeLg
                ) { Text("Ingresar") }

                OutlinedButton(
                    onClick = { user = ""; pass = ""; error = null },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = shapeLg
                ) { Text("Cancelar") }
            }

            // === Separador ====================================================================
            Divider(Modifier.padding(top = 8.dp), color = cs.outline)
            Text(
                "o continuar con",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = cs.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            // === Botones sociales con icono y paleta oficial ==================================
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Google (fondo blanco + borde claro)
                SocialButtonIcon(
                    text = "Google",
                    iconRes = R.drawable.logo_google,    // coloca tu vector/PNG
                    container = Color.White,
                    content = Color(0xFF1F1F1F),
                    borderColor = Color(0xFFE0E3EB),
                    shape = shapeLg,
                    modifier = Modifier.weight(1f).height(52.dp)
                )
                // Facebook (azul oficial)
                SocialButtonIcon(
                    text = "Facebook",
                    iconRes = R.drawable.logo_facebook, // coloca tu vector/PNG (blanco)
                    container = Color(0xFF1877F2),
                    content = Color.White,
                    borderColor = Color.Transparent,
                    shape = shapeLg,
                    modifier = Modifier.weight(1f).height(52.dp)
                )
            }
        }
    }
}

@Composable
private fun SocialButtonIcon(
    text: String,
    iconRes: Int,
    container: Color,
    content: Color,
    borderColor: Color,
    shape: Shape = RoundedCornerShape(20.dp),
    modifier: Modifier = Modifier
) {
    val painter = runCatching { painterResource(id = iconRes) }.getOrNull()
    Button(
        onClick = { /* sin acción */ },
        modifier = modifier
            .clip(shape)
            .then(if (borderColor != Color.Transparent) Modifier.border(1.dp, borderColor, shape) else Modifier),
        colors = ButtonDefaults.buttonColors(containerColor = container, contentColor = content),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = shape
    ) {
        if (painter != null) {
            Image(painter = painter, contentDescription = null, modifier = Modifier.size(20.dp).padding(end = 8.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}


