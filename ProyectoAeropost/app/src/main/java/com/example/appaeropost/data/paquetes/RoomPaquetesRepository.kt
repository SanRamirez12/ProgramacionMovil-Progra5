package com.example.appaeropost.data.paquetes

import com.example.appaeropost.data.paquetes.db.PaquetesDao
import com.example.appaeropost.data.paquetes.db.toCanceladoDomain
import com.example.appaeropost.data.paquetes.db.toDomain
import com.example.appaeropost.data.paquetes.db.toEntity
import com.example.appaeropost.domain.paquetes.Paquete
import com.example.appaeropost.domain.paquetes.PaqueteCancelado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomPaquetesRepository(
    private val dao: PaquetesDao
) : PaquetesRepository {

    override suspend fun getAll(): List<Paquete> = withContext(Dispatchers.IO) {
        dao.getAllActivos().map { it.toDomain() }
    }

    override suspend fun searchByCedula(cedula: String): List<Paquete> = withContext(Dispatchers.IO) {
        dao.searchActivosByCedula(cedula).map { it.toDomain() }
    }

    override suspend fun upsert(paquete: Paquete) = withContext(Dispatchers.IO) {
        dao.upsert(paquete.toEntity())
    }

    override suspend fun getById(id: String): Paquete? = withContext(Dispatchers.IO) {
        dao.getActivoById(id)?.toDomain()
    }

    override suspend fun cancelar(paqueteId: String, motivo: String) = withContext(Dispatchers.IO) {
        dao.cancelarPaquete(paqueteId, motivo)
    }

    override suspend fun restaurar(paqueteId: String) = withContext(Dispatchers.IO) {
        dao.restaurarPaquete(paqueteId)
    }

    override suspend fun getCancelados(): List<PaqueteCancelado> = withContext(Dispatchers.IO) {
        dao.getAllCancelados().map { it.toCanceladoDomain() }
    }
}

