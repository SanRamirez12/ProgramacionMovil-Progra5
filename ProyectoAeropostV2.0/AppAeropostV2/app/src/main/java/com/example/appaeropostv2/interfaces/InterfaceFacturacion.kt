package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.model.Facturacion
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de alto nivel para Facturación (trabaja con el modelo de dominio).
 */
interface InterfaceFacturacion {

    fun observarFacturaciones(): Flow<List<Facturacion>>

    suspend fun obtenerPorId(idFacturacion: Int): Facturacion?
    suspend fun obtenerPorTracking(tracking: String): Facturacion?

    suspend fun listarPorCedula(cedula: String): List<Facturacion>
    suspend fun listarPorRangoFechas(desdeIso: String, hastaIso: String): List<Facturacion>

    suspend fun totalPorMes(anioMes: String): Double  // "YYYY-MM"

    suspend fun buscar(texto: String): List<Facturacion>

    suspend fun insertar(facturacion: Facturacion): Int
    suspend fun actualizar(facturacion: Facturacion)
    suspend fun eliminar(facturacion: Facturacion)
    suspend fun upsert(facturacion: Facturacion)

    suspend fun contar(): Int
}
