package com.example.appaeropostv2.data.mappers

import com.example.appaeropostv2.data.local.entity.EntityPaquete
import com.example.appaeropostv2.domain.model.Paquete

/**
 * MapperPaquete: Entity ↔ Domain
 * Mantiene al dominio independiente de Room.
 */
fun EntityPaquete.toDomain(): Paquete = Paquete(
    idPaquete = idPaquete,
    fechaRegistro = fechaRegistro,
    idCliente = idCliente,
    nombrecliente = nombrecliente,
    cedulaCliente = cedulaCliente,
    numeroTracking = numeroTracking,
    pesoPaquete = pesoPaquete,
    valorBruto = valorBruto,
    monedasPaquete = monedasPaquete,
    tiendaOrigen = tiendaOrigen,
    casillero = casillero,
    condicionEspecial = condicionEspecial
)

fun Paquete.toEntity(): EntityPaquete = EntityPaquete(
    idPaquete = idPaquete,
    fechaRegistro = fechaRegistro,
    idCliente = idCliente,
    nombrecliente = nombrecliente,
    cedulaCliente = cedulaCliente,
    numeroTracking = numeroTracking,
    pesoPaquete = pesoPaquete,
    valorBruto = valorBruto,
    monedasPaquete = monedasPaquete,
    tiendaOrigen = tiendaOrigen,
    casillero = casillero,
    condicionEspecial = condicionEspecial
)
