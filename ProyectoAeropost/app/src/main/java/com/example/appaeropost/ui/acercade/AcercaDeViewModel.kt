package com.example.appaeropost.ui.acercade

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.config.IConfigRepository
import com.example.appaeropost.domain.config.AppConfig
import com.example.appaeropost.domain.config.SystemInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AcercaDeUiState(
    val systemInfo: SystemInfo,
    val config: AppConfig,
    val isSaving: Boolean = false,
    val message: String? = null
)

// AcercaDeViewModel.kt
class AcercaDeViewModel(
    private val repo: IConfigRepository,
    private val initialSystemInfo: SystemInfo
) : ViewModel() {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events.asSharedFlow()

    val state: StateFlow<AcercaDeUiState> =
        repo.configFlow.map { cfg ->
            AcercaDeUiState(
                systemInfo = initialSystemInfo,
                config = cfg
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AcercaDeUiState(systemInfo = initialSystemInfo, config = repo.snapshot())
        )

    fun updateIva(pct: Int) = viewModelScope.launch { repo.setIva(pct); toast("IVA actualizado") }
    fun updateTipoCambio(valor: Double) = viewModelScope.launch { repo.setTipoCambio(valor); toast("Tipo de cambio guardado") }
    fun updateZonas(texto: String) = viewModelScope.launch { repo.setZonas(texto); toast("Zonas guardadas") }
    fun updateTarifas(texto: String) = viewModelScope.launch { repo.setTarifas(texto); toast("Tarifas guardadas") }
    fun updateApiKey(key: String, value: String) = viewModelScope.launch { repo.setApiKey(key, value); toast("$key actualizada") }
    fun resetDefaults() = viewModelScope.launch { repo.resetDefaults(); toast("Valores por defecto restaurados") }
    fun backupNow() = viewModelScope.launch { toast("Respaldo creado (simulado)") }

    private fun toast(msg: String) { _events.tryEmit(msg) }
}

