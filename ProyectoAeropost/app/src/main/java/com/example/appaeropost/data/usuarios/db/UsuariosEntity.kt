package com.example.appaeropost.data.usuarios.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appaeropost.domain.EstadoUsuario
import com.example.appaeropost.domain.Genero
import com.example.appaeropost.domain.Rol
import java.time.LocalDate

@Entity(tableName = "usuarios")
data class UsuariosEntity(
    @PrimaryKey val username: String,
    val nombre: String,
    val cedula: String,
    val genero: Genero,
    val fechaRegistro: LocalDate,
    val estado: EstadoUsuario,
    val rol: Rol,
    val correo: String,
    val password: String
)
