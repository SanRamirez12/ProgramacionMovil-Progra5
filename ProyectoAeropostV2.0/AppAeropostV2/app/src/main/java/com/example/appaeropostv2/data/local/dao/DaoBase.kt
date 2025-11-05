package com.example.appaeropostv2.data.local.dao

import androidx.room.*

/**
 * DAO genérico para operaciones CRUD comunes.
 * - @Upsert: inserta o actualiza por PK automáticamente (2.6+).
 */
interface DaoBase<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(entities: List<T>): List<Long>

    @Update
    suspend fun update(entity: T)

    @Update
    suspend fun updateAll(entities: List<T>)

    @Delete
    suspend fun delete(entity: T)

    @Upsert
    suspend fun upsert(entity: T)

    @Upsert
    suspend fun upsertAll(entities: List<T>)
}