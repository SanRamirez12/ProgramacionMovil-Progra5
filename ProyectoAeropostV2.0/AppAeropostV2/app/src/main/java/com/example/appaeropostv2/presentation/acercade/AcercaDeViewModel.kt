package com.example.appaeropostv2.presentation.acercade

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appaeropostv2.domain.model.AppConfig
import com.example.appaeropostv2.domain.model.SystemInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AboutCredit(
    val label: String,
    val value: String
)

data class TeamMember(
    val name: String,
    val role: String
)

data class AcercaDeUiState(
    val appConfig: AppConfig = AppConfig(),
    val systemInfo: SystemInfo? = null,
    val credits: List<AboutCredit> = emptyList(),
    val team: List<TeamMember> = emptyList()
)

class AcercaDeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(AcercaDeUiState())
    val uiState: StateFlow<AcercaDeUiState> = _uiState.asStateFlow()

    init {
        val ctx = getApplication<Application>()

        // 🔧 Datos “humanos” (créditos). Ajustalos a tu equipo real.
        val credits = listOf(
            AboutCredit("Universidad", "Universidad Latina de Costa Rica"),
            AboutCredit("Curso", "Programación V (Android)"),
            AboutCredit("Proyecto", "AeropostApp"),
            AboutCredit("Docente asesor", "Marlon Esteban Obando Cordero"),
            AboutCredit("Año / Periodo", "2025")
        )

        val team = listOf(
            TeamMember("Santiago Ramírez Elizondo", "Desarrollo & Documentación"),
            TeamMember("Luis Felipe Méndez Navarro", "Desarrollo & Documentación"),
            TeamMember("José Alberto Álvarez Navarro", "Desarrollo & Documentación"),
            TeamMember("Luis Alonso Matarrita Obregón", "Desarrollo & Documentación")
        )

        _uiState.value = _uiState.value.copy(
            credits = credits,
            team = team,
            systemInfo = buildSystemInfo(ctx),
            // AppConfig por ahora es default (manual). Luego lo conectamos a DataStore/BD si querés.
            appConfig = AppConfig()
        )
    }

    private fun buildSystemInfo(ctx: Application): SystemInfo {
        val pm = ctx.packageManager
        val pkg = ctx.packageName

        val pi = pm.getPackageInfo(pkg, 0)
        val versionName = pi.versionName ?: "N/A"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (pi.longVersionCode).toInt()
        } else {
            @Suppress("DEPRECATION")
            pi.versionCode
        }

        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"
        val androidVersion = Build.VERSION.RELEASE ?: "N/A"
        val isDebuggable = (ctx.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0

        return SystemInfo(
            appName = "AeropostApp",
            versionName = versionName,
            versionCode = versionCode,
            buildType = if (isDebuggable) "debug" else "release",
            packageName = pkg,
            deviceModel = deviceModel,
            androidVersion = androidVersion
        )
    }
}

class AcercaDeViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcercaDeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AcercaDeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
