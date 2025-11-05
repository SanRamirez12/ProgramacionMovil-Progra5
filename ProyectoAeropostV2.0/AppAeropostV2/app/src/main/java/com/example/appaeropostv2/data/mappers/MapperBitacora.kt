package com.example.appaeropostv2.data.mappers

import com.example.appaeropostv2.data.local.entity.EntityBitacora
import com.example.appaeropostv2.domain.model.Bitacora

/**
 * MapperBitacora: Entity ↔ Domain
 */
fun EntityBitacora.toDomain(): Bitacora = Bitacora(
    idBitacora = idBitacora,
    username = username,
    fechaLogin = fechaLogin,
    fechaLogout = fechaLogout
)

fun Bitacora.toEntity(): EntityBitacora = EntityBitacora(
    idBitacora = idBitacora,
    username = username,
    fechaLogin = fechaLogin,
    fechaLogout = fechaLogout
)
