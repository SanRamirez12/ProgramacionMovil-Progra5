package com.example.appaeropostv2.domain.logic

import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.Tiendas
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

object PaqueteLogic {

    fun generarTracking(
        tienda: Tiendas,
        fecha: LocalDate,
        casillero: Casilleros
    ): String {
        val prefijoTienda = when (tienda) {
            Tiendas.AMAZON     -> "AM"
            Tiendas.EBAY       -> "EB"
            Tiendas.ALIEXPRESS -> "AL"
            Tiendas.TEMU       -> "TE"
            Tiendas.SHEIN      -> "SH"
            Tiendas.OTRO       -> "OT"
        }

        val mes = "%02d".format(fecha.monthValue)
        val anno = (fecha.year % 100).toString().padStart(2, '0')

        val codigoCasillero = when (casillero) {
            Casilleros.MIA -> "MIA"
            Casilleros.NYK -> "NYK"
            Casilleros.LAS -> "LAS"
        }

        val random = Random.nextInt(0, 100_000)
        val sufijo = random.toString().padStart(5, '0')

        return "$prefijoTienda$mes$anno$codigoCasillero$sufijo"
    }

    /**
     * ID interno del paquete: NO depende del tracking.
     * Se genera una sola vez al crear el paquete y nunca se vuelve a cambiar.
     */
    fun generarIdPaquete(): String = UUID.randomUUID().toString()
}
