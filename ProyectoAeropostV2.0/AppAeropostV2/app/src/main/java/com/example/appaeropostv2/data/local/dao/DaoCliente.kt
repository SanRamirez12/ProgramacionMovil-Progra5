package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appaeropostv2.data.local.dao.DaoBase
import com.example.appaeropostv2.data.local.entity.EntityCliente
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.RolesClientes
import kotlinx.coroutines.flow.Flow

/**
 * DaoCliente:
 * - CRUD genérico via DaoBase<EntityCliente>
 * - Queries específicas para la pantalla de Clientes y reportes
 */
@Dao
interface DaoCliente : DaoBase<EntityCliente> {

    // Observación reactiva para listas en UI
    @Query("SELECT * FROM clientes ORDER BY nombreCliente ASC")
    fun observarClientes(): Flow<List<EntityCliente>>

    // Búsquedas puntuales
    @Query("SELECT * FROM clientes WHERE idCliente = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): EntityCliente?

    @Query("SELECT * FROM clientes WHERE cedulaCliente = :cedula LIMIT 1")
    suspend fun obtenerPorCedula(cedula: String): EntityCliente?

    // Búsqueda flexible por nombre/cédula/teléfono/correo
    @Query("""
        SELECT * FROM clientes
        WHERE nombreCliente   LIKE :patron
           OR cedulaCliente   LIKE :patron
           OR telefonoCliente LIKE :patron
           OR correoCliente   LIKE :patron
        ORDER BY nombreCliente ASC
    """)
    suspend fun buscar(patron: String): List<EntityCliente>

    // Filtros por tipo/estado (para chips o tabs)
    @Query("SELECT * FROM clientes WHERE tipoCliente = :tipo ORDER BY nombreCliente ASC")
    suspend fun listarPorTipo(tipo: RolesClientes): List<EntityCliente>

    @Query("SELECT * FROM clientes WHERE estadoCliente = :estado ORDER BY nombreCliente ASC")
    suspend fun listarPorEstado(estado: Estados): List<EntityCliente>

    @Query("SELECT COUNT(*) FROM clientes")
    suspend fun contar(): Int
}
