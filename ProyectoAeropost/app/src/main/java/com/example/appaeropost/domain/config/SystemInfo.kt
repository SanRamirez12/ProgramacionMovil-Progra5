package com.example.appaeropost.domain.config

data class SystemInfo(
    val appName: String,
    val versionName: String,
    val versionCode: Int,
    val buildType: String,
    val packageName: String,
    val deviceModel: String,
    val androidVersion: String
)

data class AppConfig(
    val ivaPorcentaje: Int = 13,           // CR por defecto
    val tipoCambioUsd: Double = 540.0,     // solo manual por ahora
    val zonasTexto: String = "MIA, GAM, Provincial",
    val tarifasTexto: String = "Base: ₡1200/kg; Especial: +10% valor",
    val apiKeys: Map<String, String> = mapOf( // placeholders
        "maps" to "",
        "exchange" to "",
        "email" to ""
    )
)
