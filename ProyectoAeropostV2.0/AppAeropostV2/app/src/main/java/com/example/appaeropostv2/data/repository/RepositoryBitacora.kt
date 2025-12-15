package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoBitacora
import com.example.appaeropostv2.data.local.mappers.toDomain
import com.example.appaeropostv2.data.local.mappers.toEntity
import com.example.appaeropostv2.domain.model.Bitacora
import com.example.appaeropostv2.interfaces.InterfaceBitacora
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * RepositoryBitacora
 * - Implementa InterfaceBitacora usando DaoBitacora (Room).
 * - Sin Hilt: crea la instancia manualmente donde inicialices la DB.
 */
class RepositoryBitacora(
    private val dao: DaoBitacora
) : InterfaceBitacora {

    override fun observarEventos(): Flow<List<Bitacora>> =
        dao.observarEventos().map { it.map { e -> e.toDomain() } }

    override suspend fun obtenerPorId(idBitacora: Int): Bitacora? =
        dao.obtenerPorId(idBitacora)?.toDomain()

    override suspend fun listarPorUsuario(username: String): List<Bitacora> =
        dao.listarPorUsuario(username).map { it.toDomain() }

    override suspend fun listarPorRangoFechas(desdeIso: String, hastaIso: String): List<Bitacora> =
        dao.listarPorRangoFechas(desdeIso, hastaIso).map { it.toDomain() }

    override suspend fun buscar(texto: String): List<Bitacora> =
        dao.buscar("%$texto%").map { it.toDomain() }

    override suspend fun insertar(bitacora: Bitacora): Int =
        dao.insert(bitacora.toEntity()).toInt()

    override suspend fun actualizar(bitacora: Bitacora) {
        dao.update(bitacora.toEntity())
    }

    override suspend fun eliminar(bitacora: Bitacora) {
        dao.delete(bitacora.toEntity())
    }

    override suspend fun upsert(bitacora: Bitacora) {
        dao.upsert(bitacora.toEntity())
    }

    override suspend fun cerrarSesionUsuario(username: String, logoutIso: String): Boolean {
        // Busca la última sesión abierta (sin logout) y la cierra
        val abierta = dao.obtenerUltimaSesionAbierta(username) ?: return false
        val filas = dao.cerrarSesion(abierta.idBitacora, logoutIso)
        return filas > 0
    }

    override suspend fun contar(): Int = dao.contar()
}
