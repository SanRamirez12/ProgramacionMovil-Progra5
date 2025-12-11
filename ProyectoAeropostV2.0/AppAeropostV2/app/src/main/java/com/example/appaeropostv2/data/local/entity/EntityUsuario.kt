package com.example.appaeropostv2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.Genero
import com.example.appaeropostv2.domain.enums.RolesUsuarios
import java.time.LocalDate

@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["cedulaUsuario"], unique = true),
        Index(value = ["correoUsuario"], unique = true),
        Index(value = ["estadoUsuario"]),
        Index(value = ["rolUsuario"])
    ]
)
data class EntityUsuario(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUsuario")
    val idUsuario: Int = 0,

    @ColumnInfo(name = "nombreUsuario")
    val nombreUsuario: String,

    @ColumnInfo(name = "cedulaUsuario")
    val cedulaUsuario: String,

    @ColumnInfo(name = "generoUsuario")
    val generoUsuario: Genero,

    @ColumnInfo(name = "fechaRegistro")
    val fechaRegistro: LocalDate,

    @ColumnInfo(name = "estadoUsuario")
    val estadoUsuario: Estados,

    @ColumnInfo(name = "rolUsuario")
    val rolUsuario: RolesUsuarios,

    @ColumnInfo(name = "correoUsuario")
    val correoUsuario: String,

    @ColumnInfo(name = "telefonoUsuario")
    val telefonoUsuario: String,

    @ColumnInfo(name = "username")
    val username: String,

    // NUEVOS CAMPOS
    @ColumnInfo(name = "passwordHash")
    val passwordHash: String,

    @ColumnInfo(name = "passwordSalt")
    val passwordSalt: String,

    @ColumnInfo(name = "passwordIterations")
    val passwordIterations: Int
)
