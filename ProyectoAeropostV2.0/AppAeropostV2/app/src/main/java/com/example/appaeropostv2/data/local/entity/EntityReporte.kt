package com.example.appaeropostv2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reportes",
    indices = [
        Index(value = ["generatedAtMillis"]),
        Index(value = ["userId"])
    ]
)
data class EntityReporte(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idReporte")
    val idReporte: Int = 0,

    @ColumnInfo(name = "userId")
    val userId: Int,

    @ColumnInfo(name = "reporteNombre")
    val reporteNombre: String,

    @ColumnInfo(name = "generatedAtMillis")
    val generatedAtMillis: Long,

    @ColumnInfo(name = "configJson")
    val configJson: String,

    @ColumnInfo(name = "tcUsdCompra")
    val tcUsdCompra: Double,

    @ColumnInfo(name = "tcEurCompra")
    val tcEurCompra: Double,

    @ColumnInfo(name = "tcSource")
    val tcSource: String,

    @ColumnInfo(name = "pdfUri")
    val pdfUri: String?,

    @ColumnInfo(name = "xlsxUri")
    val xlsxUri: String?
)
