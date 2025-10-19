package com.example.appaeropost.data.paquetes

import com.example.appaeropost.domain.paquetes.Paquete

interface PaquetesRepository {
    suspend fun getAll(): List<Paquete>
    suspend fun searchByCedula(cedula: String): List<Paquete>
    suspend fun upsert(paquete: Paquete)
    suspend fun getById(id: String): Paquete?
}
