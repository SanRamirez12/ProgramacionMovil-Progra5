package com.example.appaeropost.data.usuarios.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuariosDao {

    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<UsuariosEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: UsuariosEntity)

    @Update
    suspend fun actualizar(usuario: UsuariosEntity)

    @Query("DELETE FROM usuarios WHERE username = :username")
    suspend fun eliminar(username: String)

    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    suspend fun obtenerPorUsername(username: String): UsuariosEntity?

    @Query("SELECT * FROM usuarios WHERE cedula = :cedula LIMIT 1")
    suspend fun obtenerPorCedula(cedula: String): UsuariosEntity?

    @Query("""
        SELECT * FROM usuarios 
        WHERE LOWER(username) LIKE '%' || LOWER(:query) || '%' 
           OR LOWER(nombre) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(cedula) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(correo) LIKE '%' || LOWER(:query) || '%'
    """)
    suspend fun buscar(query: String): List<UsuariosEntity>
}
