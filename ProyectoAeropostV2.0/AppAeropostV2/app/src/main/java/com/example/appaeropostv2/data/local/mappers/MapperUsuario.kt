package com.example.appaeropostv2.data.local.mappers

import com.example.appaeropostv2.data.local.entity.EntityUsuario
import com.example.appaeropostv2.domain.model.Usuario

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
    passwordHash = passwordHash,
    passwordSalt = passwordSalt,
    passwordIterations = passwordIterations
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
    passwordHash = passwordHash,
    passwordSalt = passwordSalt,
    passwordIterations = passwordIterations
)
