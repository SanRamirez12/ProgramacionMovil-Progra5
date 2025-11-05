package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoCliente
import com.example.appaeropostv2.data.mappers.toDomain
import com.example.appaeropostv2.data.mappers.toEntity
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.RolesClientes
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.interfaces.InterfaceCliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Implementación concreta que usa Room (DaoCliente) por debajo.
 * - Convierte Entity ↔ Domain con MapperCliente para mantener el dominio limpio.
 */
class RepositoryCliente(
    private val dao: DaoCliente
) : InterfaceCliente {

    override fun observarClientes(): Flow<List<Cliente>> =
        dao.observarClientes().map { list -> list.map { it.toDomain() } }

    override suspend fun obtenerPorId(idCliente: Int): Cliente? =
        dao.obtenerPorId(idCliente)?.toDomain()

    override suspend fun obtenerPorCedula(cedula: String): Cliente? =
        dao.obtenerPorCedula(cedula)?.toDomain()

    override suspend fun buscar(texto: String): List<Cliente> =
        dao.buscar("%$texto%").map { it.toDomain() }

    override suspend fun listarPorTipo(tipo: RolesClientes): List<Cliente> =
        dao.listarPorTipo(tipo).map { it.toDomain() }

    override suspend fun listarPorEstado(estado: Estados): List<Cliente> =
        dao.listarPorEstado(estado).map { it.toDomain() }

    override suspend fun insertar(cliente: Cliente): Int =
        dao.insert(cliente.toEntity()).toInt()

    override suspend fun actualizar(cliente: Cliente) {
        dao.update(cliente.toEntity())
    }

    override suspend fun eliminar(cliente: Cliente) {
        dao.delete(cliente.toEntity())
    }

    override suspend fun upsert(cliente: Cliente) {
        dao.upsert(cliente.toEntity())
    }

    override suspend fun contar(): Int = dao.contar()
}
