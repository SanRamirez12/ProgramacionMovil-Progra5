package com.example.appaeropost.data.config

import androidx.room.TypeConverter
import com.example.appaeropost.domain.clientes.EstadoCliente
import com.example.appaeropost.domain.clientes.TipoCliente

class RoomConverters {
    @TypeConverter
    fun toTipoCliente(name: String): TipoCliente = enumValueOf(name)

    @TypeConverter
    fun fromTipoCliente(t: TipoCliente): String = t.name

    @TypeConverter
    fun toEstadoCliente(name: String): EstadoCliente = enumValueOf(name)

    @TypeConverter
    fun fromEstadoCliente(e: EstadoCliente): String = e.name
}
