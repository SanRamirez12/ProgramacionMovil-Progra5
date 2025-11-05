package com.example.appaeropostv2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * EntityBitacora
 * - Guarda login/logout como String (ISO recomendado) para alinear con el dominio.
 * - Índices: username, fechaLogin (útiles para listados/filtrado).
 */
@Entity(
    tableName = "bitacora",
    indices = [
        Index(value = ["username"]),
        Index(value = ["fechaLogin"]),
        Index(value = ["fechaLogout"])
    ]
)
data class EntityBitacora(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idBitacora")
    val idBitacora: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "fechaLogin")
    val fechaLogin: String,  // ISO recomendado

    @ColumnInfo(name = "fechaLogout")
    val fechaLogout: String  // puede ser "" si aún no cierra sesión
)
