package com.example.appaeropost.data.config

import com.example.appaeropost.domain.config.AppConfig
import kotlinx.coroutines.flow.StateFlow

interface IConfigRepository {
    val configFlow: StateFlow<AppConfig>
    fun setIva(pct: Int)
    fun setTipoCambio(valor: Double)
    fun setZonas(texto: String)
    fun setTarifas(texto: String)
    fun setApiKey(key: String, value: String)
    fun resetDefaults()
    fun snapshot(): AppConfig
}
