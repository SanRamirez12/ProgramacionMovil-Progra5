package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appaeropostv2.data.local.dto.ClientesPorTipoDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorClienteDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorMesDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorTrackingDto
import com.example.appaeropostv2.data.local.dto.PaquetesEspecialesDto
import com.example.appaeropostv2.data.local.dto.PaquetesPorTiendaDto
import com.example.appaeropostv2.data.local.entity.EntityReporte
import kotlinx.coroutines.flow.Flow

/**
 * DaoReporte:
 * - Queries agregadas para el Reporte Maestro (KPIs y tablas).
 * - Historial de reportes generados (EntityReporte).
 *
 * Nota: Facturacion.fechaFacturacion es ISO String "YYYY-MM-DD" en DB :contentReference[oaicite:1]{index=1},
 * por eso usamos substr() para agrupar por mes.
 */
@Dao
interface DaoReporte {

    // -----------------------------
    // HISTORIAL
    // -----------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReporte(entity: EntityReporte): Long

    @Query("SELECT * FROM reportes ORDER BY generatedAtMillis DESC")
    fun observarHistorial(): Flow<List<EntityReporte>>

    @Query("SELECT * FROM reportes WHERE userId = :userId ORDER BY generatedAtMillis DESC")
    fun observarHistorialPorUsuario(userId: Int): Flow<List<EntityReporte>>

    @Query("DELETE FROM reportes WHERE idReporte = :id")
    suspend fun eliminarReporte(id: Int)

    // -----------------------------
    // CLIENTES
    // -----------------------------
    @Query("""
        SELECT tipoCliente AS tipoCliente, COUNT(*) AS cantidad
        FROM clientes
        GROUP BY tipoCliente
        ORDER BY cantidad DESC
    """)
    suspend fun clientesPorTipo(): List<ClientesPorTipoDto>

    // -----------------------------
    // PAQUETES
    // -----------------------------
    @Query("""
        SELECT tiendaOrigen AS tiendaOrigen,
               COUNT(*) AS cantidad,
               IFNULL(SUM(pesoPaquete), 0.0) AS pesoTotal,
               IFNULL(AVG(pesoPaquete), 0.0) AS pesoPromedio
        FROM paquetes
        GROUP BY tiendaOrigen
        ORDER BY cantidad DESC
    """)
    suspend fun paquetesPorTienda(): List<PaquetesPorTiendaDto>

    @Query("""
        SELECT condicionEspecial AS esEspecial,
               COUNT(*) AS cantidad,
               (COUNT(*) * 1.0 / (SELECT COUNT(*) FROM paquetes)) * 100.0 AS porcentaje
        FROM paquetes
        GROUP BY condicionEspecial
        ORDER BY esEspecial DESC
    """)
    suspend fun paquetesEspecialesVsNormales(): List<PaquetesEspecialesDto>

    // -----------------------------
    // FACTURACIÓN
    // -----------------------------
    @Query("""
        SELECT substr(fechaFacturacion, 1, 7) AS anioMes,
               IFNULL(SUM(montoTotal), 0.0) AS totalMonto,
               COUNT(*) AS cantidadFacturas,
               IFNULL(AVG(montoTotal), 0.0) AS ticketPromedio
        FROM facturaciones
        GROUP BY substr(fechaFacturacion, 1, 7)
        ORDER BY anioMes ASC
    """)
    suspend fun facturacionPorMes(): List<FacturacionPorMesDto>

    @Query("""
        SELECT cedulaCliente AS cedulaCliente,
               IFNULL(SUM(montoTotal), 0.0) AS totalMonto,
               COUNT(*) AS cantidadFacturas,
               IFNULL(AVG(montoTotal), 0.0) AS ticketPromedio
        FROM facturaciones
        GROUP BY cedulaCliente
        ORDER BY totalMonto DESC
    """)
    suspend fun facturacionPorCliente(): List<FacturacionPorClienteDto>

    @Query("""
        SELECT numeroTracking AS numeroTracking,
               cedulaCliente AS cedulaCliente,
               fechaFacturacion AS fechaFacturacion,
               montoTotal AS montoTotal
        FROM facturaciones
        ORDER BY fechaFacturacion DESC
    """)
    suspend fun facturacionPorTracking(): List<FacturacionPorTrackingDto>
}
