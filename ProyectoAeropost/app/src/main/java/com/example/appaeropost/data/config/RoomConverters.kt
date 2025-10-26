package com.example.appaeropost.data.config

import androidx.room.TypeConverter
import com.example.appaeropost.domain.*
import com.example.appaeropost.domain.clientes.EstadoCliente
import com.example.appaeropost.domain.clientes.TipoCliente
import com.example.appaeropost.domain.paquetes.Moneda
import com.example.appaeropost.domain.paquetes.TiendaOrigen
import java.math.BigDecimal
import java.time.LocalDate

class RoomConverters {
    // LocalDate (no-null)
    @TypeConverter fun fromLocalDate(d: LocalDate): Long = d.toEpochDay()
    @TypeConverter fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    // BigDecimal (almacenado como String para evitar pérdida)
    @TypeConverter fun fromBigDecimal(bd: BigDecimal): String = bd.toPlainString()
    @TypeConverter fun toBigDecimal(s: String): BigDecimal = s.toBigDecimal()

    // Clientes
    @TypeConverter fun toTipoCliente(s: String): TipoCliente = enumValueOf(s)
    @TypeConverter fun fromTipoCliente(t: TipoCliente): String = t.name
    @TypeConverter fun toEstadoCliente(s: String): EstadoCliente = enumValueOf(s)
    @TypeConverter fun fromEstadoCliente(e: EstadoCliente): String = e.name

    // Usuarios
    @TypeConverter fun toGenero(s: String): Genero = enumValueOf(s)
    @TypeConverter fun fromGenero(g: Genero): String = g.name
    @TypeConverter fun toEstadoUsuario(s: String): EstadoUsuario = enumValueOf(s)
    @TypeConverter fun fromEstadoUsuario(e: EstadoUsuario): String = e.name
    @TypeConverter fun toRol(s: String): Rol = enumValueOf(s)
    @TypeConverter fun fromRol(r: Rol): String = r.name

    // Paquetes
    @TypeConverter fun toMoneda(s: String): Moneda = enumValueOf(s)
    @TypeConverter fun fromMoneda(m: Moneda): String = m.name
    @TypeConverter fun toTiendaOrigen(s: String): TiendaOrigen = enumValueOf(s)
    @TypeConverter fun fromTiendaOrigen(t: TiendaOrigen): String = t.name
}



