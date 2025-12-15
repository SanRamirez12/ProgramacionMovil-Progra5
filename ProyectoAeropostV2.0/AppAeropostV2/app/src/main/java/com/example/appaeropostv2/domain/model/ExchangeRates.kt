package com.example.appaeropostv2.domain.model

import java.time.LocalDate

data class ExchangeRates(
    val date: LocalDate,
    val usdCompraToCrc: Double,
    val eurCompraToCrc: Double,
    val source: String, // "BCCR" o "FALLBACK"
)