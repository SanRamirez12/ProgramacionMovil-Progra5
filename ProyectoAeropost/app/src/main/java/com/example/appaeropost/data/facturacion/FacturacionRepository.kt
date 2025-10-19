package com.example.appaeropost.data.facturacion


import com.example.appaeropost.domain.Factura


interface FacturacionRepository {
    suspend fun crearFactura(f: Factura): Result<Factura>
    suspend fun actualizarFactura(f: Factura): Result<Factura>
    suspend fun obtenerFacturaPorTracking(tracking: String): Factura?
    suspend fun listarFacturas(): List<Factura>
}