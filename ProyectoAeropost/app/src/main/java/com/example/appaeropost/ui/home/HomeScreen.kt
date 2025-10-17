package com.example.appaeropost.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appaeropost.R
import com.example.appaeropost.navigation.Screen
import com.example.appaeropost.ui.components.ModulePillButton
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun HomeScreen(nav: NavController? = null, modifier: Modifier = Modifier) {
    ModuleScaffold(
        title = null,        // ocultamos AppBar
        showTopBar = false
    ) {
        val cs = MaterialTheme.colorScheme
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // HEADER AZUL
            Card(
                colors = CardDefaults.cardColors(containerColor = cs.primary),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val logo = runCatching { painterResource(R.drawable.logo_aeropost) }.getOrNull()
                    if (logo != null) {
                        Image(
                            painter = logo,
                            contentDescription = "Aeropost",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                    Text(
                        text = "Menú principal",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = cs.onPrimary
                    )
                }
            }

            // BIENVENIDA
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bienvenido a Aeropost App.\n" +
                            "Aquí puedes gestionar clientes, paquetes y facturación; " +
                            "además accedes a módulos avanzados desde la sección 'Más'.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // MÓDULOS
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModulePillButton("Clientes")     { nav?.navigate(Screen.Clientes.route) }
                    ModulePillButton("Paquetes")     { nav?.navigate(Screen.Paquetes.route) }
                    ModulePillButton("Facturación")  { nav?.navigate(Screen.Facturacion.route) }
                    ModulePillButton("Más módulos")  { nav?.navigate(Screen.More.route) }
                }
            }

            // TAGLINE
            Text(
                text = "Tu lugar para pedidos rápidos y seguros.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}


