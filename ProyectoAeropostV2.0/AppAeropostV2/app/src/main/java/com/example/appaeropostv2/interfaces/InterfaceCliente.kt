package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.RolesClientes
import com.example.appaeropostv2.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de alto nivel para gestionar Clientes desde la UI/casos de uso.
 * Siempre trabaja con el modelo de dominio (Cliente), nunca con Entities de Room.
 */
interface InterfaceCliente {

    // Observación reactiva (para listas en Compose)
    fun observarClientes(): Flow<List<Cliente>>

    // Búsquedas puntuales
    suspend fun obtenerPorId(idCliente: Int): Cliente?
    suspend fun obtenerPorCedula(cedula: String): Cliente?

    // Búsqueda flexible por nombre/cédula/teléfono/correo (usa LIKE)
    suspend fun buscar(texto: String): List<Cliente>

    // Filtros
    suspend fun listarPorTipo(tipo: RolesClientes): List<Cliente>
    suspend fun listarPorEstado(estado: Estados): List<Cliente>

    // CRUD
    suspend fun insertar(cliente: Cliente): Int
    suspend fun actualizar(cliente: Cliente)
    suspend fun eliminar(cliente: Cliente)
    suspend fun upsert(cliente: Cliente)

    // Utilidades
    suspend fun contar(): Int
}
