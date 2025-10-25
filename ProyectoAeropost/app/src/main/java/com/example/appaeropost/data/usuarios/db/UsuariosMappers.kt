package com.example.appaeropost.data.usuarios.db

import com.example.appaeropost.domain.Usuario

fun UsuariosEntity.toDomain() = Usuario(
    nombre = nombre,
    cedula = cedula,
    genero = genero,
    fechaRegistro = fechaRegistro,
    estadoUsuario = estado,
    rol = rol,
    correo = correo,
    username = username,
    password = password
)

fun Usuario.toEntity() = UsuariosEntity(
    nombre = nombre,
    cedula = cedula,
    genero = genero,
    fechaRegistro = fechaRegistro,
    estado = estadoUsuario,
    rol = rol,
    correo = correo,
    username = username,
    password = password
)
