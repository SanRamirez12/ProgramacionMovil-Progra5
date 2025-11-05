package com.example.appaeropostv2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.RolesClientes

/**
 * EntityCliente
 * - Columnas con los mismos nombres del modelo de dominio para mapeo directo.
 * - Índices útiles para búsquedas comunes; cédula y correo únicos para evitar duplicados.
 */
@Entity(
    tableName = "clientes",
    indices = [
        Index(value = ["cedulaCliente"], unique = true),
        Index(value = ["correoCliente"], unique = true),
        Index(value = ["nombreCliente"]),
        Index(value = ["tipoCliente"]),
        Index(value = ["estadoCliente"])
    ]
)
data class EntityCliente(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idCliente")
    val idCliente: Int = 0,

    @ColumnInfo(name = "nombreCliente")
    val nombreCliente: String,

    @ColumnInfo(name = "cedulaCliente")
    val cedulaCliente: String,

    @ColumnInfo(name = "telefonoCliente")
    val telefonoCliente: String,

    @ColumnInfo(name = "correoCliente")
    val correoCliente: String,

    @ColumnInfo(name = "tipoCliente")
    val tipoCliente: RolesClientes,

    @ColumnInfo(name = "estadoCliente")
    val estadoCliente: Estados,

    @ColumnInfo(name = "direccionEntrega")
    val direccionEntrega: String
)
