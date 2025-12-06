package com.example.appaeropostv2.data.local.entity

import androidx.room.*
import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.enums.Tiendas
import java.math.BigDecimal
import java.time.LocalDate

/**
 * EntityPaquete
 * - PrimaryKey: idPaquete (String) para mantener compatibilidad con tu dominio.
 * - FK: idCliente → EntityCliente.idCliente (CASCADE al borrar el cliente).
 * - Denormalizamos cedulaCliente y nombrecliente para reportes/filtrado rápido.
 * - Converters ya manejan LocalDate, BigDecimal, Enums.
 */
@Entity(
    tableName = "paquetes",
    foreignKeys = [
        ForeignKey(
            entity = EntityCliente::class,
            parentColumns = ["idCliente"],
            childColumns = ["idCliente"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["idCliente"]),
        Index(value = ["cedulaCliente"]),
        Index(value = ["numeroTracking"], unique = true),
        Index(value = ["fechaRegistro"]),
        Index(value = ["tiendaOrigen"]),
        Index(value = ["casillero"])
    ]
)
data class EntityPaquete(
    @PrimaryKey
    @ColumnInfo(name = "idPaquete")
    val idPaquete: String,

    @ColumnInfo(name = "fechaRegistro")
    val fechaRegistro: LocalDate,

    @ColumnInfo(name = "idCliente")
    val idCliente: Int,

    @ColumnInfo(name = "nombrecliente")
    val nombrecliente: String,

    @ColumnInfo(name = "cedulaCliente")
    val cedulaCliente: String,

    @ColumnInfo(name = "numeroTracking")
    val numeroTracking: String,

    @ColumnInfo(name = "pesoPaquete")
    val pesoPaquete: Double,

    @ColumnInfo(name = "valorBruto")
    val valorBruto: BigDecimal,

    @ColumnInfo(name = "monedasPaquete")
    val monedasPaquete: Monedas,

    @ColumnInfo(name = "tiendaOrigen")
    val tiendaOrigen: Tiendas,

    @ColumnInfo(name = "casillero")
    val casillero: Casilleros,

    @ColumnInfo(name = "condicionEspecial")
    val condicionEspecial: Boolean
)
