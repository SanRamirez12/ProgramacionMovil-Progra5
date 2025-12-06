package com.example.appaeropostv2.presentation.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appaeropostv2.core.designsystem.theme.AccentYellow
import com.example.appaeropostv2.core.designsystem.theme.Brushes

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
)
 {
    val uiState = viewModel.uiState
    var passwordVisible by remember { mutableStateOf(false) }

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brushes.headerGradient()) // Fondo azul degradado completo
    ) {
        // --------- Card de login (contenedor blanco) ---------
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ---------- Logo redondo ----------
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Brushes.headerGradient()),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Inventory,
                        contentDescription = null,
                        tint = colors.surface,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ---------- Título ----------
                Text(
                    text = "Aeropost",
                    color = colors.primary,
                    style = typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Inicia sesión para continuar",
                    color = colors.primary,
                    style = typography.labelLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // ---------- Usuario ----------
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = viewModel::onUsernameChange,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Usuario",
                            color = colors.primary,
                            style = typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDFE3EB),
                        unfocusedBorderColor = Color(0xFFDFE3EB),
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        cursorColor = colors.primary
                    )
                )

                Spacer(Modifier.height(16.dp))

                // ---------- Contraseña ----------
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Contraseña",
                            color = colors.primary,
                            style = typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null
                        )
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Outlined.Visibility
                                else
                                    Icons.Outlined.VisibilityOff,
                                contentDescription = null,
                                tint = colors.primary
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDFE3EB),
                        unfocusedBorderColor = Color(0xFFDFE3EB),
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        cursorColor = colors.primary
                    )
                )

                Spacer(Modifier.height(8.dp))

                // ---------- ¿Olvidaste tu contraseña? centrado ----------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { /* TODO */ },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = colors.primary,
                            style = typography.bodySmall
                        )
                    }
                }

                if (uiState.errorMessage != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = uiState.errorMessage,
                        color = colors.error,
                        style = typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(12.dp))

                // ---------- Botón Iniciar Sesión con degradado ----------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Brushes.headerGradient())
                ) {
                    Button(
                        onClick = { viewModel.login(onLoginSuccess) },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.White.copy(alpha = 0.6f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                style = typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ---------- Separador ----------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE1E5EE)
                    )
                    Text(
                        text = "  O iniciar sesión con  ",
                        style = typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE1E5EE)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ---------- Botón Google ----------
                OutlinedButton(
                    onClick = { /* TODO: Google */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, Color(0xFFDFE3EB)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colors.surface,
                        contentColor = colors.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.MailOutline,
                        contentDescription = null,
                        tint = colors.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Continuar con Google",
                        style = typography.labelLarge
                    )
                }

                Spacer(Modifier.height(10.dp))

                // ---------- Botón Facebook ----------
                OutlinedButton(
                    onClick = { /* TODO: Facebook */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, Color(0xFFDFE3EB)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colors.surface,
                        contentColor = colors.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Facebook,
                        contentDescription = null,
                        tint = colors.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Continuar con Facebook",
                        style = typography.labelLarge
                    )
                }

                Spacer(Modifier.height(22.dp))

                // ---------- Registro: línea + texto debajo ----------
                // ---------- Registro: línea + texto debajo ----------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿No tienes cuenta?",
                        color = colors.onSurfaceVariant,
                        style = typography.bodyMedium
                    )
                    TextButton(
                        onClick = onRegisterClick,   // <- aquí antes estaba /* TODO: navegación registro */
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Regístrate aquí",
                            color = colors.primary,
                            style = typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(3.dp)
                            .background(AccentYellow)
                    )
                }


                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

