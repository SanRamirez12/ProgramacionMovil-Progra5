package com.example.appaeropost.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appaeropost.navigation.Screen
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun HomeScreen(nav: NavController? = null, modifier: Modifier = Modifier) {
    ModuleScaffold(title = "Menú principal") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bienvenida / descripción
            Text(
                "Bienvenido a Aeropost App.\n" +
                        "Aquí puedes gestionar clientes, paquetes y facturación; " +
                        "además de acceso a módulos avanzados desde la sección 'Más'.",
                style = MaterialTheme.typography.bodyLarge
            )

            // Accesos rápidos a módulos principales
            Button(onClick = { nav?.navigate(Screen.Clientes.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Clientes")
            }
            Button(onClick = { nav?.navigate(Screen.Paquetes.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Paquetes")
            }
            Button(onClick = { nav?.navigate(Screen.Facturacion.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Facturación")
            }
            Button(onClick = { nav?.navigate(Screen.More.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Más módulos")
            }
        }
    }
}

