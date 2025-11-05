package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appaeropostv2.data.local.dao.DaoBase
import com.example.appaeropostv2.data.local.entity.EntityFacturacion
import kotlinx.coroutines.flow.Flow

/**
 * DaoFacturacion
 * - Consultas para pantallas de facturación y reportes.
 * - fechaFacturacion es texto ISO; puedes filtrar por BETWEEN usando cadenas "YYYY-MM-DD".
 */
@Dao
interface DaoFacturacion : DaoBase<EntityFacturacion> {

    // Observación para listados/dashboards
    @Query("SELECT * FROM facturaciones ORDER BY fechaFacturacion DESC, idFacturacion DESC")
    fun observarFacturaciones(): Flow<List<EntityFacturacion>>

    // Puntuales
    @Query("SELECT * FROM facturaciones WHERE idFacturacion = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): EntityFacturacion?

    @Query("SELECT * FROM facturaciones WHERE numeroTracking = :tracking LIMIT 1")
    suspend fun obtenerPorTracking(tracking: String): EntityFacturacion?

    // Filtros
    @Query("SELECT * FROM facturaciones WHERE cedulaCliente = :cedula ORDER BY fechaFacturacion DESC")
    suspend fun listarPorCedula(cedula: String): List<EntityFacturacion>

    @Query("""
        SELECT * FROM facturaciones
        WHERE fechaFacturacion BETWEEN :desde AND :hasta
        ORDER BY fechaFacturacion DESC
    """)
    suspend fun listarPorRangoFechas(desde: String, hasta: String): List<EntityFacturacion>

    @Query("""
        SELECT IFNULL(SUM(montoTotal), 0.0) FROM facturaciones
        WHERE substr(fechaFacturacion, 1, 7) = :anioMes  -- "YYYY-MM"
    """)
    suspend fun totalPorMes(anioMes: String): Double

    // Búsqueda rápida por tracking / cédula / dirección
    @Query("""
        SELECT * FROM facturaciones
        WHERE numeroTracking LIKE :patron
           OR cedulaCliente LIKE :patron
           OR direccionEntrega LIKE :patron
        ORDER BY fechaFacturacion DESC
    """)
    suspend fun buscar(patron: String): List<EntityFacturacion>

    @Query("SELECT COUNT(*) FROM facturaciones")
    suspend fun contar(): Int
}
