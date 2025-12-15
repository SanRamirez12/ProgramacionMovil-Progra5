package com.example.appaeropostv2.data.local.mappers

import com.example.appaeropostv2.data.local.entity.EntityCliente
import com.example.appaeropostv2.domain.model.Cliente

/**
 * MapperCliente: Entity ↔ Domain
 * Mantiene aislado Room del dominio.
 */
fun EntityCliente.toDomain(): Cliente = Cliente(
    idCliente = idCliente,
    nombreCliente = nombreCliente,
    cedulaCliente = cedulaCliente,
    telefonoCliente = telefonoCliente,
    correoCliente = correoCliente,
    tipoCliente = tipoCliente,
    estadoCliente = estadoCliente,
    direccionEntrega = direccionEntrega
)

fun Cliente.toEntity(): EntityCliente = EntityCliente(
    idCliente = idCliente,
    nombreCliente = nombreCliente,
    cedulaCliente = cedulaCliente,
    telefonoCliente = telefonoCliente,
    correoCliente = correoCliente,
    tipoCliente = tipoCliente,
    estadoCliente = estadoCliente,
    direccionEntrega = direccionEntrega
)
