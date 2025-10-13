package com.example.appaeropost.data.clientes

import com.example.appaeropost.domain.clientes.Cliente
import kotlinx.coroutines.delay

class ClientesRepository {
    private val clientes = listOf(
        Cliente(1, "Ana Morales", "1-1234-5678", "8888-1111", "Regular"),
        Cliente(2, "Beto Jiménez", "2-2222-3333", "8888-2222", "VIP"),
        Cliente(3, "Carlos Pérez", "1-9876-5432", "8888-3333", "Regular"),
        Cliente(4, "Diana Gómez", "3-1357-2468", "8888-4444", "Sucursal"),
        Cliente(5, "Esteban Vega", "1-1111-2222", "8888-5555", "Regular"),
    )

    suspend fun getClientes(): List<Cliente> { delay(300); return clientes }
    suspend fun searchClientes(query: String): List<Cliente> {
        delay(150)
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getClientes()
        return clientes.filter {
            it.nombre.lowercase().contains(q) || it.identificacion.lowercase().contains(q)
        }
    }
}
