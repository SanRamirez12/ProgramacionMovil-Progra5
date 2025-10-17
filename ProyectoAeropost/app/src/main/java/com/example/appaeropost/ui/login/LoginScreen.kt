package com.example.appaeropost.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appaeropost.R

@Composable
fun LoginScreen(
    onLoginOk: () -> Unit,
    modifier: Modifier = Modifier
) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val cs = MaterialTheme.colorScheme
    val shapeCard = RoundedCornerShape(20.dp)
    val shapeBtn  = RoundedCornerShape(24.dp)
    val scroll = rememberScrollState()

    // Fondo azul (primary)
    Surface(
        color = cs.primary,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 28.dp)
                .verticalScroll(scroll),
            contentAlignment = Alignment.TopCenter
        ) {
            // Card blanca centrada
            Card(
                shape = shapeCard,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .widthIn(max = 460.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    val painter = runCatching { painterResource(id = R.drawable.logo_aeropost) }.getOrNull()
                    if (painter != null) {
                        Image(
                            painter = painter,
                            contentDescription = "Aeropost",
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 8.dp)
                                .size(56.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Flight,
                            contentDescription = "Aeropost",
                            tint = cs.primary,
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 8.dp)
                                .size(56.dp)
                        )
                    }

                    // Título
                    Text(
                        text = "Acceder a tu cuenta",
                        style = MaterialTheme.typography.displaySmall,
                        color = cs.primary
                    )

                    Spacer(Modifier.height(4.dp))

                    // Campo: usuario
                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        placeholder = { Text("Usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cs.primary,
                            unfocusedBorderColor = cs.outline,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = cs.primary
                        )
                    )

                    // Campo: contraseña (con toggle)
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        placeholder = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPass = !showPass }) {
                                Icon(
                                    imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = cs.primary
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cs.primary,
                            unfocusedBorderColor = cs.outline,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = cs.primary
                        )
                    )

                    // Error
                    if (error != null) {
                        Text(
                            text = error!!,
                            color = cs.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Botón principal
                    Button(
                        onClick = {
                            if (user == "adminKing" && pass == "123456") onLoginOk()
                            else error = "Credenciales inválidas"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = shapeBtn,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary
                        )
                    ) {
                        Text("Iniciar sesión")
                    }

                    // Separador con líneas “—  o  —”
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = cs.outlineVariant)
                        Text(
                            "  O inicia sesión con  ",
                            style = MaterialTheme.typography.labelLarge,
                            color = cs.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Divider(modifier = Modifier.weight(1f), color = cs.outlineVariant)
                    }

                    // Botones sociales
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SocialButtonIcon(
                            text = "Google",
                            iconRes = R.drawable.logo_google,
                            container = Color.White,
                            content = Color(0xFF1F1F1F),
                            borderColor = Color(0xFFE0E3EB),
                            shape = shapeBtn,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        )
                        SocialButtonIcon(
                            text = "Facebook",
                            iconRes = R.drawable.logo_facebook,
                            container = Color(0xFF1877F2),
                            content = Color.White,
                            borderColor = Color.Transparent,
                            shape = shapeBtn,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        )
                    }

                    // Acciones secundarias (opcional: Cancelar)
                    OutlinedButton(
                        onClick = { user = ""; pass = ""; error = null; showPass = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = shapeBtn
                    ) { Text("Cancelar") }

                    Spacer(Modifier.height(4.dp))
                }
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
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    val painter = runCatching { painterResource(id = iconRes) }.getOrNull()
    FilledTonalButton(
        onClick = { /* TODO: OAuth */ },
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = container,
            contentColor = content
        ),
        border = if (borderColor != Color.Transparent)
            ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp, brush = SolidColor(borderColor))
        else null,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}



