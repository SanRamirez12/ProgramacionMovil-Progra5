package com.example.appaeropost.data.clientes

import com.example.appaeropost.domain.clientes.Cliente
import com.example.appaeropost.domain.clientes.EstadoCliente
import com.example.appaeropost.domain.clientes.TipoCliente
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.min

class ClientesRepository {

    // Mock en memoria (reemplazable por Room/Firebase)
    private val clientes = mutableListOf(
        Cliente(1, "Ana Morales", "1-1234-5678", "8888-1111", "ana@correo.com",
            TipoCliente.REGULAR, EstadoCliente.HABILITADO, "San José, Centro"),
        Cliente(2, "Beto Jiménez", "2-2222-3333", "8888-2222", "beto@correo.com",
            TipoCliente.VIP, EstadoCliente.HABILITADO, "Heredia, Belén"),
        Cliente(3, "Carlos Pérez", "1-9876-5432", "8888-3333", "carlos@correo.com",
            TipoCliente.REGULAR, EstadoCliente.DESHABILITADO, "Cartago, Paraíso"),
        Cliente(4, "Diana Gómez", "3-1357-2468", "8888-4444", "diana@correo.com",
            TipoCliente.SUCURSAL, EstadoCliente.HABILITADO, "Alajuela, Centro"),
        Cliente(5, "Esteban Vega", "1-1111-2222", "8888-5555", "esteban@correo.com",
            TipoCliente.REGULAR, EstadoCliente.HABILITADO, "San José, Escazú"),
    )

    // Simular latencias
    private suspend fun slow(ms: Long = 250) { delay(ms) }

    suspend fun getClientes(limit: Int = 50, offset: Int = 0): List<Cliente> {
        slow()
        val end = min(offset + limit, clientes.size)
        return if (offset >= clientes.size) emptyList() else clientes.subList(offset, end)
    }

    suspend fun searchClientes(query: String, limit: Int = 50, offset: Int = 0): List<Cliente> {
        slow(180)
        val q = query.trim().lowercase(Locale.getDefault())
        val base = if (q.isEmpty()) clientes
        else clientes.filter {
            it.nombre.lowercase().contains(q) || it.identificacion.lowercase().contains(q)
        }
        val end = min(offset + limit, base.size)
        return if (offset >= base.size) emptyList() else base.subList(offset, end)
    }

    suspend fun getById(id: Int): Cliente? {
        slow(150); return clientes.firstOrNull { it.id == id }
    }

    suspend fun create(c: Cliente) {
        slow()
        // En Room usarías @Insert; aquí aseguramos id único simple
        val nextId = (clientes.maxOfOrNull { it.id } ?: 0) + 1
        clientes.add(c.copy(id = nextId))
    }

    suspend fun update(c: Cliente): Boolean {
        slow()
        val idx = clientes.indexOfFirst { it.id == c.id }
        return if (idx >= 0) { clientes[idx] = c; true } else false
    }
}

