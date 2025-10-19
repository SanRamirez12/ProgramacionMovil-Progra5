package com.example.appaeropost.data.clientes

import com.example.appaeropost.domain.clientes.Cliente

interface ClientesRepository {
    suspend fun getClientes(limit: Int = 50, offset: Int = 0): List<Cliente>
    suspend fun searchClientes(query: String, limit: Int = 50, offset: Int = 0): List<Cliente>
    suspend fun getById(id: Int): Cliente?
    suspend fun create(c: Cliente)
    suspend fun update(c: Cliente): Boolean
}
