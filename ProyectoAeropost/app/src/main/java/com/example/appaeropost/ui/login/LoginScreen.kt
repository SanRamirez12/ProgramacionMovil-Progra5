package com.example.appaeropost.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun LoginScreen(
    onLoginOk: () -> Unit,                    // callback al loguear
    modifier: Modifier = Modifier
) {
    // Estado local (simple). En real, usarías ViewModel + validación.
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    // Reaprovecho el ModuleScaffold como “contenedor” con título.
    ModuleScaffold(title = "Acceso al sistema") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Bienvenido/a. Ingrese sus credenciales para continuar.")

            OutlinedTextField(
                value = user, onValueChange = { user = it },
                label = { Text("Usuario") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pass, onValueChange = { pass = it },
                label = { Text("Contraseña") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    // Demo: “admin / 1234”
                    if (user == "admin" && pass == "1234") onLoginOk()
                    else error = "Credenciales inválidas (usa admin/1234 en demo)"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }
        }
    }
}
