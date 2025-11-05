package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoFacturacion
import com.example.appaeropostv2.data.mappers.toDomain
import com.example.appaeropostv2.data.mappers.toEntity
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.interfaces.InterfaceFacturacion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * RepositoryFacturacion
 * - Implementa InterfaceFacturacion usando DaoFacturacion.
 * - Sin Hilt: construye esta clase pasando el dao desde donde creas la DB.
 */
class RepositoryFacturacion(
    private val dao: DaoFacturacion
) : InterfaceFacturacion {

    override fun observarFacturaciones(): Flow<List<Facturacion>> =
        dao.observarFacturaciones().map { it.map { e -> e.toDomain() } }

    override suspend fun obtenerPorId(idFacturacion: Int): Facturacion? =
        dao.obtenerPorId(idFacturacion)?.toDomain()

    override suspend fun obtenerPorTracking(tracking: String): Facturacion? =
        dao.obtenerPorTracking(tracking)?.toDomain()

    override suspend fun listarPorCedula(cedula: String): List<Facturacion> =
        dao.listarPorCedula(cedula).map { it.toDomain() }

    override suspend fun listarPorRangoFechas(desdeIso: String, hastaIso: String): List<Facturacion> =
        dao.listarPorRangoFechas(desdeIso, hastaIso).map { it.toDomain() }

    override suspend fun totalPorMes(anioMes: String): Double =
        dao.totalPorMes(anioMes)

    override suspend fun buscar(texto: String): List<Facturacion> =
        dao.buscar("%$texto%").map { it.toDomain() }

    override suspend fun insertar(facturacion: Facturacion): Int =
        dao.insert(facturacion.toEntity()).toInt()

    override suspend fun actualizar(facturacion: Facturacion) {
        dao.update(facturacion.toEntity())
    }

    override suspend fun eliminar(facturacion: Facturacion) {
        dao.delete(facturacion.toEntity())
    }

    override suspend fun upsert(facturacion: Facturacion) {
        dao.upsert(facturacion.toEntity())
    }

    override suspend fun contar(): Int = dao.contar()
}

