package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appaeropostv2.data.local.dao.DaoBase
import com.example.appaeropostv2.data.local.entity.EntityPaquete
import com.example.appaeropostv2.domain.enums.Tiendas
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * DaoPaquete
 * - Extiende DaoBase<EntityPaquete> para CRUD genérico.
 * - Incluye queries para pantallas: listar por cliente, por cédula, por tracking,
 *   por rango de fechas y por tienda, además de búsqueda flexible.
 */
@Dao
interface DaoPaquete : DaoBase<EntityPaquete> {

    // Listado reactivo (para UI)
    @Query("SELECT * FROM paquetes ORDER BY fechaRegistro DESC")
    fun observarPaquetes(): Flow<List<EntityPaquete>>

    // Búsquedas puntuales
    @Query("SELECT * FROM paquetes WHERE idPaquete = :idPaquete LIMIT 1")
    suspend fun obtenerPorId(idPaquete: String): EntityPaquete?

    @Query("SELECT * FROM paquetes WHERE numeroTracking = :tracking LIMIT 1")
    suspend fun obtenerPorTracking(tracking: String): EntityPaquete?

    // Filtros frecuentes
    @Query("SELECT * FROM paquetes WHERE idCliente = :idCliente ORDER BY fechaRegistro DESC")
    suspend fun listarPorIdCliente(idCliente: Int): List<EntityPaquete>

    @Query("SELECT * FROM paquetes WHERE cedulaCliente = :cedula ORDER BY fechaRegistro DESC")
    suspend fun listarPorCedula(cedula: String): List<EntityPaquete>

    @Query("""
        SELECT * FROM paquetes 
        WHERE fechaRegistro BETWEEN :desde AND :hasta
        ORDER BY fechaRegistro DESC
    """)
    suspend fun listarPorRangoFechas(desde: LocalDate, hasta: LocalDate): List<EntityPaquete>

    @Query("SELECT * FROM paquetes WHERE tiendaOrigen = :tienda ORDER BY fechaRegistro DESC")
    suspend fun listarPorTienda(tienda: Tiendas): List<EntityPaquete>

    @Query("SELECT * FROM paquetes WHERE condicionEspecial = 1 ORDER BY fechaRegistro DESC")
    suspend fun listarEspeciales(): List<EntityPaquete>

    // Búsqueda flexible: nombre, cédula, tracking
    @Query("""
        SELECT * FROM paquetes
        WHERE nombrecliente LIKE :patron
           OR cedulaCliente LIKE :patron
           OR numeroTracking LIKE :patron
        ORDER BY fechaRegistro DESC
    """)
    suspend fun buscar(patron: String): List<EntityPaquete>

    @Query("SELECT COUNT(*) FROM paquetes")
    suspend fun contar(): Int
}
