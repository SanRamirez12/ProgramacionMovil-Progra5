package com.example.appaeropostv2.domain.model

import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.Genero
import com.example.appaeropostv2.domain.enums.RolesUsuarios
import java.time.LocalDate


data class Usuario(
    val idUsuario: Int,
    val nombreUsuario: String,
    val cedulaUsuario: String,
    val generoUsuario: Genero,
    val fechaRegistro: LocalDate,
    val estadoUsuario: Estados,
    val rolUsuario: RolesUsuarios,
    val correoUsuario: String,
    val telefonoUsuario: String,
    val username: String,
    val password: String
)