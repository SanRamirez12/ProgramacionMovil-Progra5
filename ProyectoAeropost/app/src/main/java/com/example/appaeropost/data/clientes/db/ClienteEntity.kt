package com.example.appaeropost.data.clientes.db

import androidx.room.*
import com.example.appaeropost.domain.clientes.EstadoCliente
import com.example.appaeropost.domain.clientes.TipoCliente

@Entity(
    tableName = "clientes",
    indices = [
        Index(value = ["identificacion"], unique = true),
        Index(value = ["nombre"])
    ]
)
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val identificacion: String,
    val telefono: String,
    val correo: String,
    val tipo: TipoCliente,
    val estado: EstadoCliente,
    val direccionEntrega: String
)
