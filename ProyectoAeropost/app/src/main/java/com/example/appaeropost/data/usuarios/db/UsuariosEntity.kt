package com.example.appaeropost.data.usuarios.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.appaeropost.data.config.RoomConverters
import com.example.appaeropost.domain.*
import java.time.LocalDate

@TypeConverters(RoomConverters::class)     // ⬅️ extra por si el scope del DB no llega
@Entity(tableName = "usuarios")
data class UsuariosEntity(
    @PrimaryKey val username: String,
    val nombre: String,
    val cedula: String,
    val genero: Genero,
    val fechaRegistro: LocalDate,          // ahora con converter no-null
    val estado: EstadoUsuario,
    val rol: Rol,
    val correo: String,
    val password: String
)
