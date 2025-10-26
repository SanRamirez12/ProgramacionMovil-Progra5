package com.example.appaeropost.data.paquetes.db

import com.example.appaeropost.domain.paquetes.*
import java.math.BigDecimal

// Entity (activos) -> Domain
fun PaquetesEntity.toDomain(): Paquete = Paquete(
    id = id,
    fechaRegistro = fechaRegistro,
    clienteId = clienteId,
    clienteNombre = clienteNombre,
    clienteCedula = clienteCedula,
    tracking = tracking,
    pesoLb = pesoLb,
    valorBruto = valorBruto,
    moneda = Moneda.valueOf(moneda),
    tiendaOrigen = TiendaOrigen.valueOf(tiendaOrigen),
    condicionEspecial = condicionEspecial
)

// Domain -> Entity (activos)
fun Paquete.toEntity(): PaquetesEntity = PaquetesEntity(
    id = id,
    fechaRegistro = fechaRegistro,
    clienteId = clienteId,
    clienteNombre = clienteNombre,
    clienteCedula = clienteCedula,
    tracking = tracking,
    pesoLb = pesoLb,
    valorBruto = valorBruto,
    moneda = moneda.name,
    tiendaOrigen = tiendaOrigen.name,
    condicionEspecial = condicionEspecial
)

// Cancelados -> Domain
fun PaquetesCanceladoEntity.toCanceladoDomain(): PaqueteCancelado = PaqueteCancelado(
    paqueteId = paqueteId,
    fechaRegistro = fechaRegistro,
    clienteId = clienteId,
    clienteNombre = clienteNombre,
    clienteCedula = clienteCedula,
    tracking = tracking,
    pesoLb = pesoLb,
    valorBruto = valorBruto,
    moneda = Moneda.valueOf(moneda),
    tiendaOrigen = TiendaOrigen.valueOf(tiendaOrigen),
    condicionEspecial = condicionEspecial,
    motivo = motivo,
    fechaCancelacion = fechaCancelacion
)
