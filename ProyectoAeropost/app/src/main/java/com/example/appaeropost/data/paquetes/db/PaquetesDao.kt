package com.example.appaeropost.data.paquetes.db

import androidx.room.*
import java.time.LocalDate

@Dao
interface PaquetesDao {

    // ——— Activos
    @Query("SELECT * FROM paquetes ORDER BY fechaRegistro DESC")
    suspend fun getAllActivos(): List<PaquetesEntity>

    @Query("""
        SELECT * FROM paquetes 
        WHERE clienteCedula = :cedula 
        ORDER BY fechaRegistro DESC
    """)
    suspend fun searchActivosByCedula(cedula: String): List<PaquetesEntity>

    @Query("SELECT * FROM paquetes WHERE id = :id LIMIT 1")
    suspend fun getActivoById(id: String): PaquetesEntity?

    @Upsert
    suspend fun upsert(entity: PaquetesEntity)

    @Query("DELETE FROM paquetes WHERE id = :id")
    suspend fun deleteActivoById(id: String)

    // ——— Cancelados
    @Query("SELECT * FROM paquetes_cancelados ORDER BY fechaCancelacion DESC")
    suspend fun getAllCancelados(): List<PaquetesCanceladoEntity>

    @Query("SELECT * FROM paquetes_cancelados WHERE paqueteId = :id LIMIT 1")
    suspend fun getCanceladoById(id: String): PaquetesCanceladoEntity?

    @Upsert
    suspend fun upsertCancelado(entity: PaquetesCanceladoEntity)

    @Query("DELETE FROM paquetes_cancelados WHERE paqueteId = :id")
    suspend fun deleteCanceladoById(id: String)

    // ——— Operaciones negocio (soft-delete / restore)
    @Transaction
    suspend fun cancelarPaquete(paqueteId: String, motivo: String) {
        val activo = getActivoById(paqueteId) ?: return
        val cancelado = PaquetesCanceladoEntity(
            paqueteId = activo.id,
            fechaRegistro = activo.fechaRegistro,
            clienteId = activo.clienteId,
            clienteNombre = activo.clienteNombre,
            clienteCedula = activo.clienteCedula,
            tracking = activo.tracking,
            pesoLb = activo.pesoLb,
            valorBruto = activo.valorBruto,
            moneda = activo.moneda,
            tiendaOrigen = activo.tiendaOrigen,
            condicionEspecial = activo.condicionEspecial,
            motivo = motivo,
            fechaCancelacion = LocalDate.now()
        )
        upsertCancelado(cancelado)
        deleteActivoById(paqueteId)
    }

    @Transaction
    suspend fun restaurarPaquete(paqueteId: String) {
        val cancelado = getCanceladoById(paqueteId) ?: return
        val activo = PaquetesEntity(
            id = cancelado.paqueteId,
            fechaRegistro = cancelado.fechaRegistro,
            clienteId = cancelado.clienteId,
            clienteNombre = cancelado.clienteNombre,
            clienteCedula = cancelado.clienteCedula,
            tracking = cancelado.tracking,
            pesoLb = cancelado.pesoLb,
            valorBruto = cancelado.valorBruto,
            moneda = cancelado.moneda,
            tiendaOrigen = cancelado.tiendaOrigen,
            condicionEspecial = cancelado.condicionEspecial
        )
        upsert(activo)
        deleteCanceladoById(paqueteId)
    }
}

