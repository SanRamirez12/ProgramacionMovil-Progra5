package com.example.appaeropost.ui.more

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appaeropost.navigation.Screen
import com.example.appaeropost.ui.components.ModuleScaffold
import com.example.appaeropost.ui.components.ModulePillButton

@Composable
fun MoreScreen(nav: NavController? = null, modifier: Modifier = Modifier) {
    val items = listOf(
        Screen.Usuarios  to "Usuarios",
        Screen.Bitacora  to "Bitácora y Auditoría",
        Screen.Tracking  to "Tracking geográfico",
        Screen.Reportes  to "Análisis y reportes",
        Screen.AcercaDe  to "Acerca de / Configuración"
    )

    ModuleScaffold(title = "Más") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { (dest, label) ->
                ModulePillButton(label) { nav?.navigate(dest.route) }
            }
        }
    }
}


