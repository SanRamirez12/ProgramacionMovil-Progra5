package com.example.appaeropostv2.presentation.tracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.domain.enums.TrackingStatus
import com.example.appaeropostv2.domain.logic.TrackingSimulationLogic
import com.example.appaeropostv2.domain.model.Tracking
import com.example.appaeropostv2.interfaces.InterfaceTracking
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TrackingUiState(
    val cedulaBusqueda: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val resultados: List<Tracking> = emptyList()
)

data class TrackingDetalleUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val tracking: Tracking? = null
)

class TrackingViewModel(
    private val repoTracking: InterfaceTracking
) : ViewModel() {

    var uiState by mutableStateOf(TrackingUiState())
        private set

    var detalleState by mutableStateOf(TrackingDetalleUiState())
        private set

    private var jobActualizacion: Job? = null

    fun onChangeCedula(valor: String) {
        uiState = uiState.copy(cedulaBusqueda = valor)
    }

    fun buscarPorCedula() {
        val cedula = uiState.cedulaBusqueda.trim()
        if (cedula.isEmpty()) {
            uiState = uiState.copy(error = "Ingresa una cédula.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(cargando = true, error = null)
            try {
                val lista = repoTracking.obtenerTrackingsPorCedula(cedula)
                uiState = uiState.copy(resultados = lista, cargando = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    cargando = false,
                    error = "No se pudo cargar tracking: ${e.message}"
                )
            }
        }
    }

    fun cargarDetalle(numeroTracking: String) {
        viewModelScope.launch {
            detalleState = detalleState.copy(cargando = true, error = null, tracking = null)

            try {
                val trackingDb = repoTracking.obtenerTrackingPorNumero(numeroTracking)
                if (trackingDb == null) {
                    detalleState = detalleState.copy(
                        cargando = false,
                        error = "No se encontró tracking."
                    )
                    return@launch
                }

                // ✅ Forzar estado/posición correctos al entrar (sin esperar 30s)
                val nuevoEstado = TrackingSimulationLogic.estadoPorTiempo(
                    tiempoInicioMillis = trackingDb.tiempoInicioMillis,
                    casilleroOrigen = trackingDb.casilleroOrigen
                )

                val pos = TrackingSimulationLogic.siguientePosicion(
                    estado = nuevoEstado,
                    casilleroOrigen = trackingDb.casilleroOrigen,
                    tiempoInicioMillis = trackingDb.tiempoInicioMillis
                )

                val actualizado = trackingDb.copy(
                    estado = nuevoEstado,
                    latitud = pos.latitud,
                    longitud = pos.longitud,
                    ultimaActualizacionMillis = System.currentTimeMillis()
                )

                repoTracking.actualizarTracking(actualizado)

                detalleState = detalleState.copy(
                    cargando = false,
                    tracking = actualizado
                )

                iniciarAutoActualizacion()

            } catch (e: Exception) {
                detalleState = detalleState.copy(
                    cargando = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    private fun iniciarAutoActualizacion() {
        jobActualizacion?.cancel()

        jobActualizacion = viewModelScope.launch {
            while (true) {
                delay(30_000L)

                val actual = detalleState.tracking ?: continue
                if (actual.estado == TrackingStatus.DELIVERED) continue

                val nuevoEstado = TrackingSimulationLogic.estadoPorTiempo(
                    tiempoInicioMillis = actual.tiempoInicioMillis,
                    casilleroOrigen = actual.casilleroOrigen
                )

                val pos = TrackingSimulationLogic.siguientePosicion(
                    estado = nuevoEstado,
                    casilleroOrigen = actual.casilleroOrigen,
                    tiempoInicioMillis = actual.tiempoInicioMillis
                )

                val actualizado = actual.copy(
                    estado = nuevoEstado,
                    latitud = pos.latitud,
                    longitud = pos.longitud,
                    ultimaActualizacionMillis = System.currentTimeMillis()
                )

                try {
                    repoTracking.actualizarTracking(actualizado)
                    detalleState = detalleState.copy(tracking = actualizado)
                } catch (_: Exception) {
                    // no reventamos UI
                }
            }
        }
    }

    override fun onCleared() {
        jobActualizacion?.cancel()
        super.onCleared()
    }

    class Factory(
        private val repoTracking: InterfaceTracking
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TrackingViewModel(repoTracking) as T
        }
    }
}
