package com.example.appaeropostv2.data.local.db

import androidx.room.TypeConverter
import com.example.appaeropostv2.domain.enums.*
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Converters centralizados para Room.
 * - LocalDate  <-> Long (epochDay)      → robusto para rangos/orden.
 * - BigDecimal <-> String               → sin pérdida de precisión.
 * - Enums      <-> String (name)        → legible y estable.
 */
class RoomConverters {

    // -------- LocalDate --------
    @TypeConverter
    fun fromLocalDate(d: LocalDate?): Long? = d?.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? = epochDay?.let(LocalDate::ofEpochDay)

    // -------- BigDecimal --------
    @TypeConverter
    fun fromBigDecimal(bd: BigDecimal?): String? = bd?.toPlainString()

    @TypeConverter
    fun toBigDecimal(txt: String?): BigDecimal? = txt?.let { BigDecimal(it) }

    // -------- Enums: Usuarios --------
    @TypeConverter
    fun fromGenero(value: Genero?): String? = value?.name

    @TypeConverter
    fun toGenero(name: String?): Genero? = name?.let { enumValueOf<Genero>(it) }

    @TypeConverter
    fun fromRolesUsuarios(value: RolesUsuarios?): String? = value?.name

    @TypeConverter
    fun toRolesUsuarios(name: String?): RolesUsuarios? = name?.let { enumValueOf<RolesUsuarios>(it) }

    // -------- Enums: Clientes --------
    @TypeConverter
    fun fromRolesClientes(value: RolesClientes?): String? = value?.name

    @TypeConverter
    fun toRolesClientes(name: String?): RolesClientes? = name?.let { enumValueOf<RolesClientes>(it) }

    // -------- Enums: Estado genérico --------
    @TypeConverter
    fun fromEstados(value: Estados?): String? = value?.name

    @TypeConverter
    fun toEstados(name: String?): Estados? = name?.let { enumValueOf<Estados>(it) }

    // -------- Enums: Paquetes --------
    @TypeConverter
    fun fromMonedas(value: Monedas?): String? = value?.name

    @TypeConverter
    fun toMonedas(name: String?): Monedas? = name?.let { enumValueOf<Monedas>(it) }

    @TypeConverter
    fun fromTiendas(value: Tiendas?): String? = value?.name

    @TypeConverter
    fun toTiendas(name: String?): Tiendas? = name?.let { enumValueOf<Tiendas>(it) }

    // -------- Enums: Casilleros --------
    @TypeConverter
    fun fromCasilleros(value: Casilleros?): String? = value?.name

    @TypeConverter
    fun toCasilleros(name: String?): Casilleros? = name?.let { enumValueOf<Casilleros>(it) }

    // -------- Enums: Tracking --------
    @TypeConverter
    fun fromTrackingStatus(value: TrackingStatus?): String? = value?.name

    @TypeConverter
    fun toTrackingStatus(name: String?): TrackingStatus? =
        name?.let { enumValueOf<TrackingStatus>(it) }

}

