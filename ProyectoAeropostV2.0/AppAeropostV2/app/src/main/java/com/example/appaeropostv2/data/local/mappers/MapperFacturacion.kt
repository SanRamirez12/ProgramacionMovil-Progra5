package com.example.appaeropostv2.data.local.mappers

import com.example.appaeropostv2.data.local.entity.EntityFacturacion
import com.example.appaeropostv2.domain.model.Facturacion

/**
 * MapperFacturacion: Entity ↔ Domain
 */
fun EntityFacturacion.toDomain(): Facturacion = Facturacion(
    idFacturacion = idFacturacion,
    numeroTracking = numeroTracking,
    cedulaCliente = cedulaCliente,
    pesoPaquete = pesoPaquete,
    valorBrutoPaquete = valorBrutoPaquete,
    productoEspecial = productoEspecial,
    fechaFacturacion = fechaFacturacion,
    montoTotal = montoTotal,
    direccionEntrega = direccionEntrega
)

fun Facturacion.toEntity(): EntityFacturacion = EntityFacturacion(
    idFacturacion = idFacturacion,
    numeroTracking = numeroTracking,
    cedulaCliente = cedulaCliente,
    pesoPaquete = pesoPaquete,
    valorBrutoPaquete = valorBrutoPaquete,
    productoEspecial = productoEspecial,
    fechaFacturacion = fechaFacturacion,
    montoTotal = montoTotal,
    direccionEntrega = direccionEntrega
)
