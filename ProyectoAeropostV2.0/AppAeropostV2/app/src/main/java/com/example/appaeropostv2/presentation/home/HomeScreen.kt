package com.example.appaeropostv2.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.components.InfoCard
import com.example.appaeropostv2.presentation.common.components.ModuleCard
import com.example.appaeropostv2.presentation.common.layout.AppScaffold

@Composable
fun HomeScreen(
    onOpenAcercaDe: () -> Unit,
    onOpenUsuarios: () -> Unit,
    onOpenBitacora: () -> Unit,
    onOpenClientes: () -> Unit,
    onOpenPaquetes: () -> Unit,
    onOpenFacturacion: () -> Unit,
    onOpenReportes: () -> Unit,
    onOpenTracking: () -> Unit,
) {
    val modules = listOf(
        Triple(Icons.Filled.Info,        "Acerca De",    onOpenAcercaDe),
        Triple(Icons.Filled.Group,       "Usuarios",     onOpenUsuarios),
        Triple(Icons.Filled.Book,        "Bitácora",     onOpenBitacora),
        Triple(Icons.Filled.Person,      "Clientes",     onOpenClientes),
        Triple(Icons.Filled.Inventory,   "Paquete",      onOpenPaquetes),
        Triple(Icons.Filled.ReceiptLong, "Facturación",  onOpenFacturacion),
        Triple(Icons.Filled.BarChart,    "Reportes",     onOpenReportes),
        Triple(Icons.Filled.LocationOn,  "Tracking",     onOpenTracking),
    )

    AppScaffold(
        header = { GradientHeader(title = "Bienvenido a", subtitle = "Aeropost") }
    ) { _ ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Dimens.ScreenPadding, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                InfoCard("Tu solución completa para gestión de envíos internacionales. Administra paquetes, clientes, facturas y más desde una sola plataforma.")
            }
            item { Text("Módulos", style = MaterialTheme.typography.titleMedium) }
            items(modules) { (icon, title, action) ->
                ModuleCard(
                    icon = icon,
                    title = title,
                    description = when (title) {
                        "Acerca De"   -> "Información sobre Aeropost y nuestros servicios"
                        "Usuarios"    -> "Gestión de usuarios y permisos del sistema"
                        "Bitácora"    -> "Historial de actividades y eventos del sistema"
                        "Clientes"    -> "Administración de clientes y sus datos"
                        "Paquete"     -> "Gestión de paquetes y envíos internacionales"
                        "Facturación" -> "Manejo de facturas y pagos de clientes"
                        "Reportes"    -> "Estadísticas y visualizaciones del negocio"
                        "Tracking"    -> "Rastreo en tiempo real de tus paquetes"
                        else -> ""
                    },
                    onClick = action
                )
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}


