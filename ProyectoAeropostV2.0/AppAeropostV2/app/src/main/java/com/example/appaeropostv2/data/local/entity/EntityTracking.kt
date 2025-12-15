package com.example.appaeropostv2.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.TrackingStatus

@Entity(
    tableName = "tracking",
    indices = [
        Index(value = ["numeroTracking"], unique = true),
        Index(value = ["idPaquete"])
    ]
)
data class EntityTracking(
    @PrimaryKey(autoGenerate = true)
    val idTracking: Long = 0,

    val idPaquete: String,
    val numeroTracking: String,

    // ahora enum (RoomConverters ya lo soporta)
    val casilleroOrigen: Casilleros,

    val estado: TrackingStatus,
    val latitud: Double,
    val longitud: Double,
    val tiempoInicioMillis: Long,
    val ultimaActualizacionMillis: Long
)
