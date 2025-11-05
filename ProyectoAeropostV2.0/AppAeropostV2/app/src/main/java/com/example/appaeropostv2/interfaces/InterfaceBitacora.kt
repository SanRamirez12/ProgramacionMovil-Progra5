package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.model.Bitacora
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de alto nivel para la Bitácora de sesiones/eventos.
 */
interface InterfaceBitacora {

    fun observarEventos(): Flow<List<Bitacora>>

    suspend fun obtenerPorId(idBitacora: Int): Bitacora?
    suspend fun listarPorUsuario(username: String): List<Bitacora>
    suspend fun listarPorRangoFechas(desdeIso: String, hastaIso: String): List<Bitacora>

    suspend fun buscar(texto: String): List<Bitacora>

    // Crea un registro de login (fechaLogout suele quedar "" inicialmente)
    suspend fun insertar(bitacora: Bitacora): Int

    // Para correcciones manuales
    suspend fun actualizar(bitacora: Bitacora)
    suspend fun eliminar(bitacora: Bitacora)

    // Upsert genérico
    suspend fun upsert(bitacora: Bitacora)

    // Cierra la última sesión abierta del usuario (si existe)
    suspend fun cerrarSesionUsuario(username: String, logoutIso: String): Boolean

    suspend fun contar(): Int
}
