package com.example.appaeropost.data.clientes.db
//Archivo .kt que traduce entre ClienteEntity (DB) ↔ Cliente (tu modelo de dominio en domain/clientes/Cliente.kt)




import com.example.appaeropost.domain.clientes.Cliente

fun ClienteEntity.toDomain() = Cliente(
    id = id,
    nombre = nombre,
    identificacion = identificacion,
    telefono = telefono,
    correo = correo,
    tipo = tipo,
    estado = estado,
    direccionEntrega = direccionEntrega
)

fun Cliente.toEntity() = ClienteEntity(
    id = id,
    nombre = nombre,
    identificacion = identificacion,
    telefono = telefono,
    correo = correo,
    tipo = tipo,
    estado = estado,
    direccionEntrega = direccionEntrega
)

