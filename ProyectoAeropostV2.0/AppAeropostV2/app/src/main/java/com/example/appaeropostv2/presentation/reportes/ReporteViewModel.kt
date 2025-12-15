package com.example.appaeropostv2.presentation.reportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.domain.reportes.ReporteMaestroConfig
import com.example.appaeropostv2.interfaces.InterfaceReportes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ReporteUiState(
    val config: ReporteMaestroConfig = ReporteMaestroConfig(),
    val loading: Boolean = false,
    val mensaje: String? = null
)

class ReporteViewModel(
    private val repo: InterfaceReportes
) : ViewModel() {

    private val _state = MutableStateFlow(ReporteUiState())
    val state: StateFlow<ReporteUiState> = _state

    fun toggleResumen(v: Boolean) = update { it.copy(config = it.config.copy(incluirResumenEjecutivo = v)) }
    fun toggleClientes(v: Boolean) = update { it.copy(config = it.config.copy(incluirClientes = v)) }
    fun togglePaquetes(v: Boolean) = update { it.copy(config = it.config.copy(incluirPaquetes = v)) }
    fun toggleFacturacion(v: Boolean) = update { it.copy(config = it.config.copy(incluirFacturacion = v)) }
    fun toggleStats(v: Boolean) = update { it.copy(config = it.config.copy(incluirEstadisticasBasicas = v)) }

    fun generarYEnviar() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, mensaje = null)
            try {
                val ok = repo.enviarReporteMaestroPorCorreo(_state.value.config)
                _state.value = _state.value.copy(
                    loading = false,
                    mensaje = if (ok) "Reporte enviado al correo del usuario actual ✅" else "No se pudo enviar el reporte ❌"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    mensaje = "Error enviando reporte: ${e.message ?: "desconocido"}"
                )
            }
        }
    }


    private fun update(block: (ReporteUiState) -> ReporteUiState) {
        _state.value = block(_state.value)
    }
}

/**
 * Factory para crear ReporteViewModel con dependencia manual (sin Hilt).
 */
class ReporteViewModelFactory(
    private val repo: InterfaceReportes
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReporteViewModel::class.java)) {
            return ReporteViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
