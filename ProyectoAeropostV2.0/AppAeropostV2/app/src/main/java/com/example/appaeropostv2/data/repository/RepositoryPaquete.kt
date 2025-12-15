package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoPaquete
import com.example.appaeropostv2.data.local.mappers.toDomain
import com.example.appaeropostv2.data.local.mappers.toEntity
import com.example.appaeropostv2.domain.enums.Tiendas
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.interfaces.InterfacePaquete
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * RepositoryPaquete
 * Implementa InterfacePaquete usando DaoPaquete (Room) por debajo.
 * Sin Hilt: construye esta clase manualmente donde inicialices la DB.
 */
class RepositoryPaquete(
    private val dao: DaoPaquete
) : InterfacePaquete {

    override fun observarPaquetes(): Flow<List<Paquete>> =
        dao.observarPaquetes().map { it.map { e -> e.toDomain() } }

    override suspend fun obtenerPorId(idPaquete: String): Paquete? =
        dao.obtenerPorId(idPaquete)?.toDomain()

    override suspend fun obtenerPorTracking(tracking: String): Paquete? =
        dao.obtenerPorTracking(tracking)?.toDomain()

    override suspend fun listarPorIdCliente(idCliente: Int): List<Paquete> =
        dao.listarPorIdCliente(idCliente).map { it.toDomain() }

    override suspend fun listarPorCedula(cedula: String): List<Paquete> =
        dao.listarPorCedula(cedula).map { it.toDomain() }

    override suspend fun listarPorRangoFechas(desde: LocalDate, hasta: LocalDate): List<Paquete> =
        dao.listarPorRangoFechas(desde, hasta).map { it.toDomain() }

    override suspend fun listarPorTienda(tienda: Tiendas): List<Paquete> =
        dao.listarPorTienda(tienda).map { it.toDomain() }

    override suspend fun listarEspeciales(): List<Paquete> =
        dao.listarEspeciales().map { it.toDomain() }

    override suspend fun buscar(texto: String): List<Paquete> =
        dao.buscar("%$texto%").map { it.toDomain() }

    override suspend fun insertar(paquete: Paquete): Long =
        dao.insert(paquete.toEntity())

    override suspend fun actualizar(paquete: Paquete) {
        dao.update(paquete.toEntity())
    }

    override suspend fun eliminar(paquete: Paquete) {
        dao.delete(paquete.toEntity())
    }

    override suspend fun upsert(paquete: Paquete) {
        dao.upsert(paquete.toEntity())
    }

    override suspend fun contar(): Int = dao.contar()
}
