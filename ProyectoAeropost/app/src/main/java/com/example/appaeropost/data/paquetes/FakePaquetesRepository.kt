package com.example.appaeropost.data.paquetes

import com.example.appaeropost.domain.paquetes.Moneda
import com.example.appaeropost.domain.paquetes.Paquete
import com.example.appaeropost.domain.paquetes.TiendaOrigen
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

class FakePaquetesRepository : PaquetesRepository {

    private val data = mutableListOf<Paquete>().apply {
        // datos fake similares a tus ClientsRepository
        val clientes = listOf(
            Triple("1", "María López", "1-2345-0678"),
            Triple("2", "Juan Pérez", "1-1111-1111"),
            Triple("3", "Ana Gómez", "2-2222-2222")
        )
        val tiendas = TiendaOrigen.values()
        val monedas = Moneda.values()

        repeat(18) {
            val c = clientes.random()
            add(
                Paquete(
                    id = UUID.randomUUID().toString(),
                    fechaRegistro = LocalDate.now().minusDays(Random.nextLong(0, 30)),
                    clienteId = c.first,
                    clienteNombre = c.second,
                    clienteCedula = c.third,
                    tracking = buildString {
                        append("TRK")
                        append(Random.nextInt(100000, 999999))
                    },
                    pesoLb = (1..50).random() + Random.nextDouble(),
                    valorBruto = BigDecimal((20..500).random()),
                    moneda = monedas.random(),
                    tiendaOrigen = tiendas.random(),
                    condicionEspecial = Random.nextBoolean()
                )
            )
        }
    }

    override suspend fun getAll(): List<Paquete> = data.sortedByDescending { it.fechaRegistro }

    override suspend fun searchByCedula(cedula: String): List<Paquete> {
        if (cedula.isBlank()) return getAll()
        return data.filter { it.clienteCedula.contains(cedula.trim(), ignoreCase = true) }
            .sortedByDescending { it.fechaRegistro }
    }

    override suspend fun upsert(paquete: Paquete) {
        val idx = data.indexOfFirst { it.id == paquete.id }
        if (idx >= 0) data[idx] = paquete else data.add(0, paquete)
    }

    override suspend fun getById(id: String): Paquete? = data.find { it.id == id }
}
