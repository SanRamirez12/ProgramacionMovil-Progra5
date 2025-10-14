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
        // Grid simple de atajos a módulos frecuentes
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { nav?.navigate(Screen.Clientes.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Clientes")
            }
            Button(onClick = { nav?.navigate(Screen.Paquetes.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Paquetes")
            }
            Button(onClick = { nav?.navigate(Screen.Facturacion.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Facturación")
            }
            Button(onClick = { nav?.navigate(Screen.Tracking.route) }, modifier = Modifier.fillMaxWidth()) {
                Text("Tracking geográfico")
            }
        }
    }
}
