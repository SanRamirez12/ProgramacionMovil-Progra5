package com.example.appaeropost.data.config

import android.content.Context
import com.example.appaeropost.data.clientes.ClientesRepository
import com.example.appaeropost.data.clientes.RoomClientesRepository
import com.example.appaeropost.data.paquetes.PaquetesRepository
import com.example.appaeropost.data.paquetes.RoomPaquetesRepository
import com.example.appaeropost.data.repository.UsuariosRepository
import com.example.appaeropost.data.usuarios.repository.RoomUsuariosRepository

object ServiceLocator {

    @Volatile private var clientesRepo: ClientesRepository? = null
    @Volatile private var usuariosRepo: UsuariosRepository? = null
    @Volatile private var paquetesRepo: PaquetesRepository? = null

    fun clientesRepository(context: Context): ClientesRepository {
        val db = AppDatabase.get(context)
        return clientesRepo ?: synchronized(this) {
            clientesRepo ?: RoomClientesRepository(db.clientesDao()).also { clientesRepo = it }
        }
    }

    fun usuariosRepository(context: Context): UsuariosRepository {
        val db = AppDatabase.get(context)
        return usuariosRepo ?: synchronized(this) {
            usuariosRepo ?: RoomUsuariosRepository(db.usuariosDao()).also { usuariosRepo = it }
        }
    }

    fun paquetesRepository(context: Context): PaquetesRepository {
        val db = AppDatabase.get(context)
        return paquetesRepo ?: synchronized(this) {
            paquetesRepo ?: RoomPaquetesRepository(db.paquetesDao()).also { paquetesRepo = it }
        }
    }
}


