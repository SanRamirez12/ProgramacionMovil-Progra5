package com.example.appaeropost.data.facturacion


import com.example.appaeropost.domain.Factura
import kotlinx.coroutines.delay


class FakeFacturacionRepository : FacturacionRepository {
    private val store = mutableListOf(
        Factura(
            tracking = "SH0123CR-AIR-0001",
            cedulaCliente = "118250091",
            pesoKg = 2.6,
            valorTotalPaquete = 78000.0,
            productoEspecial = false,
            fechaEntrega = "2025-08-21",
            montoTotal = 134000.0,
            direccionEntrega = "Calle 1"
        )
    )


    override suspend fun crearFactura(f: Factura): Result<Factura> {
        delay(200)
        if (store.any { it.tracking.equals(f.tracking, ignoreCase = true) }) {
            return Result.failure(IllegalStateException("Ya existe una factura para ese número de tracking."))
        }
        store += f
        return Result.success(f)
    }


    override suspend fun actualizarFactura(f: Factura): Result<Factura> {
        delay(200)
        val idx = store.indexOfFirst { it.id == f.id }
        if (idx == -1) return Result.failure(NoSuchElementException("Factura no encontrada"))
        store[idx] = f
        return Result.success(f)
    }


    override suspend fun obtenerFacturaPorTracking(tracking: String): Factura? {
        delay(120)
        return store.firstOrNull { it.tracking.equals(tracking, ignoreCase = true) }
    }


    override suspend fun listarFacturas(): List<Factura> {
        delay(150)
        return store.toList()
    }
}