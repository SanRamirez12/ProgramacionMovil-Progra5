// ui/acercade/AcercaDeScreen.kt
package com.example.appaeropost.ui.acercade

import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PackageInfoCompat
import com.example.appaeropost.R
import com.example.appaeropost.data.acercade.InMemoryConfigRepository
import com.example.appaeropost.domain.config.SystemInfo
import com.example.appaeropost.ui.components.ModuleScaffold

@Composable
fun AcercaDeScreen() {
    val context = LocalContext.current
    val pm = context.packageManager
    val pkgName = context.packageName
    val pInfo = remember { pm.getPackageInfo(pkgName, 0) }
    val versionName = pInfo.versionName ?: "1.0"
    val versionCode = PackageInfoCompat.getLongVersionCode(pInfo).toInt()
    val isDebug = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    val buildType = if (isDebug) "debug" else "release"

    val systemInfo = remember {
        SystemInfo(
            appName = context.applicationInfo.loadLabel(pm).toString(),
            versionName = versionName,
            versionCode = versionCode,
            buildType = buildType,
            packageName = pkgName,
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            androidVersion = Build.VERSION.RELEASE ?: "N/A"
        )
    }

    val repo = remember { InMemoryConfigRepository() }
    val vm = remember { AcercaDeViewModel(repo = repo, initialSystemInfo = systemInfo) }

    AcercaDeScreen(vm = vm)
}

@Composable
fun AcercaDeScreen(vm: AcercaDeViewModel) {
    val ui by vm.state.collectAsState()
    val snackHost = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.events.collect { msg -> snackHost.showSnackbar(message = msg) }
    }

    // Igual que Clientes: sin top bar, con Header propio
    ModuleScaffold(
        title = null,
        showTopBar = false
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HeaderWithLogoAcerca()

            TarjetaAcercaDe(
                nombre = ui.systemInfo.appName,
                version = "${ui.systemInfo.versionName} (${ui.systemInfo.versionCode}) • ${ui.systemInfo.buildType}",
                paquete = ui.systemInfo.packageName,
                dispositivo = ui.systemInfo.deviceModel,
                android = ui.systemInfo.androidVersion,
                equipo = listOf(
                    "Luis Alonso Matarrita Obregón \n",
                    "Luis Felipe Mendez Navarro \n",
                    "José Alberto Álvarez Navarro \n",
                    "Santiago Ramírez Elizondo"
                ),
                curso = "Programación V — Universidad Latina"
            )

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            TarjetaParametros(
                iva = ui.config.ivaPorcentaje,
                tipoCambio = ui.config.tipoCambioUsd,
                zonas = ui.config.zonasTexto,
                tarifas = ui.config.tarifasTexto,
                apiMaps = ui.config.apiKeys["maps"].orEmpty(),
                apiExchange = ui.config.apiKeys["exchange"].orEmpty(),
                apiEmail = ui.config.apiKeys["email"].orEmpty(),
                onIvaChange = vm::updateIva,
                onTipoCambioChange = vm::updateTipoCambio,
                onZonasChange = vm::updateZonas,
                onTarifasChange = vm::updateTarifas,
                onApiMapsChange = { vm.updateApiKey("maps", it) },
                onApiExchangeChange = { vm.updateApiKey("exchange", it) },
                onApiEmailChange = { vm.updateApiKey("email", it) },
                onBackupClick = vm::backupNow,
                onResetClick = vm::resetDefaults
            )
        }

        // snackbar local (si manejas uno global, puedes omitir)
        SnackbarHost(hostState = snackHost, modifier = Modifier.padding(16.dp))
    }
}

/* ----------------- UI helpers ----------------- */

@Composable
private fun HeaderWithLogoAcerca() {
    // Mismo patrón de Clientes: card con fondo primary y textos blancos
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_configuracion), // ← coloca tu asset
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Acerca de & Configuración",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.background // blanco del tema
                )
                Text(
                    "Información del sistema y parámetros maestros",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun TarjetaAcercaDe(
    nombre: String,
    version: String,
    paquete: String,
    dispositivo: String,
    android: String,
    equipo: List<String>,
    curso: String
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                nombre,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text("Versión: $version", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Paquete: $paquete", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Dispositivo: $dispositivo", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Android: $android", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text(
                "Créditos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(equipo.joinToString(" · "))
            Text(curso)
        }
    }
}

@Composable
private fun TarjetaParametros(
    iva: Int,
    tipoCambio: Double,
    zonas: String,
    tarifas: String,
    apiMaps: String,
    apiExchange: String,
    apiEmail: String,
    onIvaChange: (Int) -> Unit,
    onTipoCambioChange: (Double) -> Unit,
    onZonasChange: (String) -> Unit,
    onTarifasChange: (String) -> Unit,
    onApiMapsChange: (String) -> Unit,
    onApiExchangeChange: (String) -> Unit,
    onApiEmailChange: (String) -> Unit,
    onBackupClick: () -> Unit,
    onResetClick: () -> Unit
) {
    var ivaText by remember { mutableStateOf(iva.toString()) }
    var tcText by remember { mutableStateOf(tipoCambio.toString()) }
    var zonasText by remember { mutableStateOf(zonas) }
    var tarifasText by remember { mutableStateOf(tarifas) }
    var mapsText by remember { mutableStateOf(apiMaps) }
    var exText by remember { mutableStateOf(apiExchange) }
    var emailText by remember { mutableStateOf(apiEmail) }

    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Parámetros del sistema",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = ivaText,
                onValueChange = {
                    ivaText = it.filter { ch -> ch.isDigit() }
                    ivaText.toIntOrNull()?.let(onIvaChange)
                },
                label = { Text("IVA (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = tcText,
                onValueChange = {
                    tcText = it.replace(',', '.')
                    tcText.toDoubleOrNull()?.let(onTipoCambioChange)
                },
                label = { Text("Tipo de cambio USD (manual)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            OutlinedTextField(
                value = zonasText,
                onValueChange = { zonasText = it; onZonasChange(it) },
                label = { Text("Zonas (texto)") },
                minLines = 2
            )

            OutlinedTextField(
                value = tarifasText,
                onValueChange = { tarifasText = it; onTarifasChange(it) },
                label = { Text("Tarifas (texto)") },
                minLines = 2
            )

            Text(
                "API Keys (placeholders)",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = mapsText,
                onValueChange = { mapsText = it; onApiMapsChange(it) },
                label = { Text("Maps API Key") },
                singleLine = true
            )
            OutlinedTextField(
                value = exText,
                onValueChange = { exText = it; onApiExchangeChange(it) },
                label = { Text("Exchange API Key") },
                singleLine = true
            )
            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it; onApiEmailChange(it) },
                label = { Text("Email/SMTP API Key") },
                singleLine = true
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
            ) {
                OutlinedButton(onClick = onResetClick) { Text("Restaurar por defecto") }
                Button(onClick = onBackupClick) { Text("Respaldar configuración") }
            }
        }
    }
}

