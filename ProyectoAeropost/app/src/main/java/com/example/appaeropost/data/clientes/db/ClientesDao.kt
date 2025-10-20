package com.example.appaeropost.data.clientes.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientesDao {

    @Query("""
        SELECT * FROM clientes 
        ORDER BY id 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getClientes(limit: Int, offset: Int): List<ClienteEntity>

    @Query("""
        SELECT * FROM clientes 
        WHERE LOWER(nombre) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(identificacion) LIKE '%' || LOWER(:query) || '%'
        ORDER BY id 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun searchClientes(query: String, limit: Int, offset: Int): List<ClienteEntity>

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ClienteEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(c: ClienteEntity): Long

    @Update
    suspend fun update(c: ClienteEntity): Int
}
