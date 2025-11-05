package com.example.appaeropostv2.data.local.entity

import androidx.room.*
import java.lang.Double as JDouble

/**
 * EntityFacturacion
 * - fechaFacturacion: se guarda como String ISO "yyyy-MM-dd" (igual que el dominio).
 * - Relación con Paquete por numeroTracking (único en paquetes).
 */
@Entity(
    tableName = "facturaciones",
    foreignKeys = [
        ForeignKey(
            entity = EntityPaquete::class,
            parentColumns = ["numeroTracking"],
            childColumns = ["numeroTracking"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["numeroTracking"], unique = true), // una factura por tracking
        Index(value = ["cedulaCliente"]),
        Index(value = ["fechaFacturacion"])
    ]
)
data class EntityFacturacion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idFacturacion")
    val idFacturacion: Int = 0,

    @ColumnInfo(name = "numeroTracking")
    val numeroTracking: String,

    @ColumnInfo(name = "cedulaCliente")
    val cedulaCliente: String,

    @ColumnInfo(name = "pesoPaquete")
    val pesoPaquete: Double,

    @ColumnInfo(name = "valorBrutoPaquete")
    val valorBrutoPaquete: Double,

    @ColumnInfo(name = "productoEspecial")
    val productoEspecial: Boolean,

    @ColumnInfo(name = "fechaFacturacion")
    val fechaFacturacion: String, // ISO yyyy-MM-dd

    @ColumnInfo(name = "montoTotal")
    val montoTotal: Double,

    @ColumnInfo(name = "direccionEntrega")
    val direccionEntrega: String
)
