package com.example.appaeropost.data.paquetes

import com.example.appaeropost.domain.paquetes.Paquete
import com.example.appaeropost.domain.paquetes.PaqueteCancelado

interface PaquetesRepository {
    // Activos
    suspend fun getAll(): List<Paquete>
    suspend fun searchByCedula(cedula: String): List<Paquete>
    suspend fun upsert(paquete: Paquete)
    suspend fun getById(id: String): Paquete?

    // Cancelación (soft-delete) y papelera
    suspend fun cancelar(paqueteId: String, motivo: String)
    suspend fun restaurar(paqueteId: String)
    suspend fun getCancelados(): List<PaqueteCancelado>
}

