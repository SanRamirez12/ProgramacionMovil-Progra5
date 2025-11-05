package com.example.appaeropostv2.data.mappers

import com.example.appaeropostv2.data.local.entity.EntityUsuario
import com.example.appaeropostv2.domain.model.Usuario

/**
 * Conversión explícita para no contaminar el dominio con Room.
 * Si mañana cambian nombres/columnas, solo tocas aquí.
 */
fun EntityUsuario.toDomain(): Usuario = Usuario(
    idUsuario = idUsuario,
    nombreUsuario = nombreUsuario,
    cedulaUsuario = cedulaUsuario,
    generoUsuario = generoUsuario,
    fechaRegistro = fechaRegistro,
    estadoUsuario = estadoUsuario,
    rolUsuario = rolUsuario,
    correoUsuario = correoUsuario,
    telefonoUsuario = telefonoUsuario,
    username = username,
    password = password
)

fun Usuario.toEntity(): EntityUsuario = EntityUsuario(
    idUsuario = idUsuario,
    nombreUsuario = nombreUsuario,
    cedulaUsuario = cedulaUsuario,
    generoUsuario = generoUsuario,
    fechaRegistro = fechaRegistro,
    estadoUsuario = estadoUsuario,
    rolUsuario = rolUsuario,
    correoUsuario = correoUsuario,
    telefonoUsuario = telefonoUsuario,
    username = username,
    password = password
)
