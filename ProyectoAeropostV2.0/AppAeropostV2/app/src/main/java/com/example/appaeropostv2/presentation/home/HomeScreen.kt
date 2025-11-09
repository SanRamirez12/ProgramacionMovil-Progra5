package com.example.appaeropostv2.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.presentation.common.components.InfoBlurbCard
import com.example.appaeropostv2.presentation.common.components.ModuleCard
import com.example.appaeropostv2.presentation.common.components.SectionTitle
import com.example.appaeropostv2.presentation.common.layout.AppScaffold
import com.example.appaeropostv2.presentation.common.sections.ParallaxHeader
import com.example.appaeropostv2.presentation.common.components.GradientHeader

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

    val listState = rememberLazyListState()

    AppScaffold(
        topBar = {},
        bottomBar = {} // o tu BottomBar global desde Main
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // — Header con parallax + fade
            item {
                ParallaxHeader(listState = listState) {
                    GradientHeader(title = "Bienvenido a", subtitle = "Aeropost")
                }
            }
            // — Info card con borde amarillo (blanca)
            item {
                InfoBlurbCard(
                    text = "Tu solución completa para gestión de envíos internacionales. Administra paquetes, clientes, facturas y más desde una sola plataforma.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            // — Título de sección en AZUL
            item {
                Row(Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("Módulos")
                }
            }
            // — Módulos (cards BLANCAS con sombra)
            items(modules) { (icon, title, onClick) ->
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
                    onClick = onClick
                )
                Spacer(Modifier.height(4.dp))
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}


