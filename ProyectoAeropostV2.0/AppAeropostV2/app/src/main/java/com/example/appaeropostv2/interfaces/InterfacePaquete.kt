package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.enums.Tiendas
import com.example.appaeropostv2.domain.model.Paquete
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Contrato de alto nivel para gestionar Paquetes.
 * Trabaja con el modelo de dominio Paquete.
 */
interface InterfacePaquete {

    fun observarPaquetes(): Flow<List<Paquete>>

    suspend fun obtenerPorId(idPaquete: String): Paquete?
    suspend fun obtenerPorTracking(tracking: String): Paquete?

    suspend fun listarPorIdCliente(idCliente: Int): List<Paquete>
    suspend fun listarPorCedula(cedula: String): List<Paquete>
    suspend fun listarPorRangoFechas(desde: LocalDate, hasta: LocalDate): List<Paquete>
    suspend fun listarPorTienda(tienda: Tiendas): List<Paquete>
    suspend fun listarEspeciales(): List<Paquete>

    suspend fun buscar(texto: String): List<Paquete>

    suspend fun insertar(paquete: Paquete): Long
    suspend fun actualizar(paquete: Paquete)
    suspend fun eliminar(paquete: Paquete)
    suspend fun upsert(paquete: Paquete)

    suspend fun contar(): Int
}
