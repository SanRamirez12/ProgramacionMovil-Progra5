package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.model.ExchangeRates
import java.time.LocalDate

interface InterfaceExchange {
    suspend fun getRatesCompraForDate(date: LocalDate): ExchangeRates
}
