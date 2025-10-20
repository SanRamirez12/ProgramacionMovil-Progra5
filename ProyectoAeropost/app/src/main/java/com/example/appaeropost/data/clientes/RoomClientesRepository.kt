package com.example.appaeropost.data.clientes

import com.example.appaeropost.data.clientes.db.ClientesDao
import com.example.appaeropost.data.clientes.db.toDomain
import com.example.appaeropost.data.clientes.db.toEntity
import com.example.appaeropost.domain.clientes.Cliente

//Implementa tu interface ClientesRepository (la que ya tenías en data/clientes/ClientesRepository.kt) usando el DAO.
class RoomClientesRepository(
    private val dao: ClientesDao
) : ClientesRepository {

    override suspend fun getClientes(limit: Int, offset: Int): List<Cliente> =
        dao.getClientes(limit, offset).map { it.toDomain() }

    override suspend fun searchClientes(query: String, limit: Int, offset: Int): List<Cliente> =
        dao.searchClientes(query, limit, offset).map { it.toDomain() }

    override suspend fun getById(id: Int): Cliente? =
        dao.getById(id)?.toDomain()

    override suspend fun create(c: Cliente) {
        dao.insert(c.toEntity())
    }

    override suspend fun update(c: Cliente): Boolean =
        dao.update(c.toEntity()) > 0
}
