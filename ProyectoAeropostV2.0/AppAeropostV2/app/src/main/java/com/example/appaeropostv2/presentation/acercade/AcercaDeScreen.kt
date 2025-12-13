package com.example.appaeropostv2.presentation.acercade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.core.designsystem.theme.Dimens
import com.example.appaeropostv2.presentation.common.components.GradientHeader
import com.example.appaeropostv2.presentation.common.components.InfoBlurbCard
import com.example.appaeropostv2.presentation.common.components.SectionTitle
import com.example.appaeropostv2.presentation.common.sections.ParallaxHeader

@Composable
fun AcercaDeScreen(
    viewModel: AcercaDeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    Surface(color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                ParallaxHeader(listState = listState, height = 220) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.weight(1f)) {
                            GradientHeader(
                                title = "AeropostApp",
                                subtitle = "Acerca de"
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "Créditos del proyecto • Información del sistema • Configuración (solo lectura)",
                                color = Color.White.copy(alpha = 0.88f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.95f),
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(Dimens.SectionSpacing)) }

            // ---------- Créditos ----------
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionTitle("Créditos")
                    Spacer(Modifier.height(10.dp))

                    uiState.credits.forEach { c ->
                        InfoBlurbCard(
                            text = "${c.label}: ${c.value}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // ---------- Equipo ----------
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionTitle("Integrantes del equipo")
                    Spacer(Modifier.height(10.dp))

                    uiState.team.forEach { member ->
                        InfoBlurbCard(
                            text = "${member.name} — ${member.role}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // ---------- Versión / Sistema ----------
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionTitle("Versión del sistema")
                    Spacer(Modifier.height(10.dp))

                    val sys = uiState.systemInfo
                    if (sys == null) {
                        InfoBlurbCard(
                            text = "No se pudo cargar la información del sistema.",
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        KeyValueBlock("App", sys.appName)
                        KeyValueBlock("Versión", "${sys.versionName} (${sys.versionCode})")
                        KeyValueBlock("Build type", sys.buildType)
                        KeyValueBlock("Package", sys.packageName)
                        KeyValueBlock("Dispositivo", sys.deviceModel)
                        KeyValueBlock("Android", sys.androidVersion)
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            // ---------- Config del sistema (AppConfig) ----------
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionTitle("Parámetros del sistema (solo lectura)")
                    Spacer(Modifier.height(10.dp))

                    val cfg = uiState.appConfig
                    KeyValueBlock("IVA", "${cfg.ivaPorcentaje}%")
                    KeyValueBlock("Tipo de cambio USD", cfg.tipoCambioUsd.toString())
                    KeyValueBlock("Zonas", cfg.zonasTexto)
                    KeyValueBlock("Tarifas", cfg.tarifasTexto)

                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "API Keys (placeholders)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                    )

                    cfg.apiKeys.forEach { (k, v) ->
                        val shown = if (v.isBlank()) "No configurada" else "••••••••"
                        KeyValueBlock(k, shown)
                    }
                }
            }

            item { Spacer(Modifier.height(18.dp)) }
        }
    }
}

@Composable
private fun KeyValueBlock(
    key: String,
    value: String
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            text = key,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
