package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.appaeropostv2.data.local.dao.DaoBase
import com.example.appaeropostv2.data.local.entity.EntityBitacora
import kotlinx.coroutines.flow.Flow

/**
 * DaoBitacora:
 * - CRUD genérico vía DaoBase<EntityBitacora>.
 * - Consultas para listados, filtros y cierre de sesión.
 */
@Dao
interface DaoBitacora : DaoBase<EntityBitacora> {

    // Observación reactiva general
    @Query("SELECT * FROM bitacora ORDER BY idBitacora DESC")
    fun observarEventos(): Flow<List<EntityBitacora>>

    // Puntuales
    @Query("SELECT * FROM bitacora WHERE idBitacora = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): EntityBitacora?

    // Listar por usuario
    @Query("""
        SELECT * FROM bitacora 
        WHERE username = :username 
        ORDER BY idBitacora DESC
    """)
    suspend fun listarPorUsuario(username: String): List<EntityBitacora>

    // Rango de fechas (string ISO: BETWEEN funciona si YYYY-MM-DD[ HH:mm:ss])
    @Query("""
        SELECT * FROM bitacora
        WHERE fechaLogin BETWEEN :desdeIso AND :hastaIso
        ORDER BY fechaLogin DESC, idBitacora DESC
    """)
    suspend fun listarPorRangoFechas(desdeIso: String, hastaIso: String): List<EntityBitacora>

    // Búsqueda simple
    @Query("""
        SELECT * FROM bitacora
        WHERE username LIKE :patron
           OR fechaLogin LIKE :patron
           OR fechaLogout LIKE :patron
        ORDER BY idBitacora DESC
    """)
    suspend fun buscar(patron: String): List<EntityBitacora>

    // Sesiones abiertas (logout vacío)
    @Query("""
        SELECT * FROM bitacora 
        WHERE username = :username AND (fechaLogout = '' OR fechaLogout IS NULL)
        ORDER BY idBitacora DESC
        LIMIT 1
    """)
    suspend fun obtenerUltimaSesionAbierta(username: String): EntityBitacora?

    // Actualización directa del logout por id
    @Query("UPDATE bitacora SET fechaLogout = :logoutIso WHERE idBitacora = :idBitacora")
    suspend fun cerrarSesion(idBitacora: Int, logoutIso: String): Int

    @Query("SELECT COUNT(*) FROM bitacora")
    suspend fun contar(): Int
}

