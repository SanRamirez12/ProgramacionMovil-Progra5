package com.example.appaeropost.data.acercade

import com.example.appaeropost.data.acercade.IConfigRepository
import com.example.appaeropost.domain.config.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class InMemoryConfigRepository(initial: AppConfig = AppConfig()) : IConfigRepository {
    private val _config = MutableStateFlow(initial)
    override val configFlow: StateFlow<AppConfig> = _config

    override fun setIva(pct: Int) =
        _config.update { it.copy(ivaPorcentaje = pct.coerceIn(0, 100)) }

    override fun setTipoCambio(valor: Double) =
        _config.update { it.copy(tipoCambioUsd = valor.coerceAtLeast(0.0)) }

    override fun setZonas(texto: String) =
        _config.update { it.copy(zonasTexto = texto) }

    override fun setTarifas(texto: String) =
        _config.update { it.copy(tarifasTexto = texto) }

    override fun setApiKey(key: String, value: String) =
        _config.update { it.copy(apiKeys = it.apiKeys + (key to value)) }

    override fun resetDefaults() { _config.value = AppConfig()
    }

    override fun snapshot(): AppConfig = _config.value
}