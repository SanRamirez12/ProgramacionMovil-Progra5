package com.example.appaeropost.data.paquetes.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.appaeropost.data.config.RoomConverters
import java.math.BigDecimal
import java.time.LocalDate

@TypeConverters(RoomConverters::class)
@Entity(tableName = "paquetes")
data class PaquetesEntity(
    @PrimaryKey val id: String,
    val fechaRegistro: LocalDate,
    val clienteId: String,
    val clienteNombre: String,
    val clienteCedula: String,
    val tracking: String,
    val pesoLb: Double,
    val valorBruto: BigDecimal,
    val moneda: String,         // enum Moneda via converter (name)
    val tiendaOrigen: String,   // enum TiendaOrigen via converter (name)
    val condicionEspecial: Boolean
)

@TypeConverters(RoomConverters::class)
@Entity(tableName = "paquetes_cancelados")
data class PaquetesCanceladoEntity(
    @PrimaryKey val paqueteId: String,   // mismo id del Paquete original
    val fechaRegistro: LocalDate,
    val clienteId: String,
    val clienteNombre: String,
    val clienteCedula: String,
    val tracking: String,
    val pesoLb: Double,
    val valorBruto: BigDecimal,
    val moneda: String,
    val tiendaOrigen: String,
    val condicionEspecial: Boolean,
    val motivo: String,
    val fechaCancelacion: LocalDate
)
