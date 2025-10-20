package com.example.appaeropost.data.config

import android.content.Context
import com.example.appaeropost.data.clientes.ClientesRepository
import com.example.appaeropost.data.clientes.RoomClientesRepository

object ServiceLocator {
    @Volatile private var clientesRepo: ClientesRepository? = null

    fun clientesRepository(context: Context): ClientesRepository {
        val db = AppDatabase.get(context)
        return clientesRepo ?: synchronized(this) {
            clientesRepo ?: RoomClientesRepository(db.clientesDao()).also { clientesRepo = it }
        }
    }
}
