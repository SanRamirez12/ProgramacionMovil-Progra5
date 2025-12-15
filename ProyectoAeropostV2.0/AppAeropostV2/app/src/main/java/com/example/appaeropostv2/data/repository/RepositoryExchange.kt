package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.remote.services.exchange.BccrSoapExchangeService
import com.example.appaeropostv2.domain.model.ExchangeRates
import com.example.appaeropostv2.interfaces.InterfaceExchange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class RepositoryExchange(
    private val service: BccrSoapExchangeService
) : InterfaceExchange {

    private val fallbackUsdCompra = 520.0
    private val fallbackEurCompra = 560.0

    override suspend fun getRatesCompraForDate(date: LocalDate): ExchangeRates = withContext(Dispatchers.IO) {
        val usd = service.getIndicadorValor(BccrSoapExchangeService.INDICADOR_USD_COMPRA, date)
        val eur = service.getIndicadorValor(BccrSoapExchangeService.INDICADOR_EUR_COMPRA, date)

        val ok = usd != null && eur != null
        if (ok) {
            ExchangeRates(
                date = date,
                usdCompraToCrc = usd!!,
                eurCompraToCrc = eur!!,
                source = "BCCR"
            )
        } else {
            ExchangeRates(
                date = date,
                usdCompraToCrc = usd ?: fallbackUsdCompra,
                eurCompraToCrc = eur ?: fallbackEurCompra,
                source = "FALLBACK"
            )
        }
    }
}
