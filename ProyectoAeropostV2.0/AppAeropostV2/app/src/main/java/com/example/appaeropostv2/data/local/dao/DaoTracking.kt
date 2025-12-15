package com.example.appaeropostv2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appaeropostv2.data.local.entity.EntityTracking

@Dao
interface DaoTracking {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(tracking: EntityTracking)

    @Update
    suspend fun actualizar(tracking: EntityTracking)

    @Query("SELECT * FROM tracking WHERE numeroTracking = :numeroTracking LIMIT 1")
    suspend fun obtenerPorNumeroTracking(numeroTracking: String): EntityTracking?

    @Query(
        """
        SELECT t.* FROM tracking t
        WHERE t.idPaquete IN (
            SELECT p.idPaquete FROM paquetes p
            WHERE p.cedulaCliente = :cedula
        )
        ORDER BY t.ultimaActualizacionMillis DESC
        """
    )
    suspend fun obtenerPorCedulaCliente(cedula: String): List<EntityTracking>
}
