package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appaeropostv2.data.local.dao.DaoBase
import com.example.appaeropostv2.data.local.entity.EntityUsuario
import com.example.appaeropostv2.domain.enums.Estados
import kotlinx.coroutines.flow.Flow

/**
 * DaoUsuario
 *  - Extiende DaoBase<EntityUsuario> para CRUD genérico (insert/update/delete/upsert).
 *  - Incluye queries típicas de autenticación y búsqueda.
 */
@Dao
interface DaoUsuario : DaoBase<EntityUsuario> {

    // --- Lecturas reactivas para listados/observación en UI ---
    @Query("SELECT * FROM usuarios ORDER BY nombreUsuario ASC")
    fun observarUsuarios(): Flow<List<EntityUsuario>>

    // --- Búsquedas puntuales ---
    @Query("SELECT * FROM usuarios WHERE idUsuario = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): EntityUsuario?

    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    suspend fun obtenerPorUsername(username: String): EntityUsuario?

    @Query("SELECT * FROM usuarios WHERE cedulaUsuario = :cedula LIMIT 1")
    suspend fun obtenerPorCedula(cedula: String): EntityUsuario?

    @Query("""
        SELECT * FROM usuarios 
        WHERE nombreUsuario LIKE :patron OR username LIKE :patron
        ORDER BY nombreUsuario ASC
    """)
    suspend fun buscarPorNombreOUsuario(patron: String): List<EntityUsuario>

    // YA NO USAMOS validarLogin POR PASSWORD PLANO

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
