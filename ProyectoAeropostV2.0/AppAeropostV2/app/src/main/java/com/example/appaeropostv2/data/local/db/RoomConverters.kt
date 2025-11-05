// data/db/RoomConverters.kt
package com.example.appaeropostv2.data.db

import androidx.room.TypeConverter
import com.example.appaeropostv2.domain.enums.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class RoomConverters {

    // ---------- java.time ----------
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): Long? = value?.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? = epochDay?.let(LocalDate::ofEpochDay)

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? =
        value?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    @TypeConverter
    fun toLocalDateTime(epochMillis: Long?): LocalDateTime? =
        epochMillis?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) }

    // ---------- BigDecimal ----------
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(str: String?): BigDecimal? = str?.let { BigDecimal(it) }

    // ---------- Enums (guarda el name para robustez) ----------
    @TypeConverter fun fromEstados(v: Estados?): String? = v?.name
    @TypeConverter fun toEstados(name: String?): Estados? = name?.let { enumValueOf<Estados>(it) }

    @TypeConverter fun fromRolesClientes(v: RolesClientes?): String? = v?.name
    @TypeConverter fun toRolesClientes(name: String?): RolesClientes? = name?.let { enumValueOf<RolesClientes>(it) }

    @TypeConverter fun fromRolesUsuarios(v: RolesUsuarios?): String? = v?.name
    @TypeConverter fun toRolesUsuarios(name: String?): RolesUsuarios? = name?.let { enumValueOf<RolesUsuarios>(it) }

    @TypeConverter fun fromTiendas(v: Tiendas?): String? = v?.name
    @TypeConverter fun toTiendas(name: String?): Tiendas? = name?.let { enumValueOf<Tiendas>(it) }

    @TypeConverter fun fromMonedas(v: Monedas?): String? = v?.name
    @TypeConverter fun toMonedas(name: String?): Monedas? = name?.let { enumValueOf<Monedas>(it) }

    @TypeConverter fun fromGenero(v: Genero?): String? = v?.name
    @TypeConverter fun toGenero(name: String?): Genero? = name?.let { enumValueOf<Genero>(it) }
}
