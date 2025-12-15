package com.example.appaeropostv2.data.local.mappers

import com.example.appaeropostv2.data.local.dto.ClientesPorTipoDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorClienteDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorMesDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorTrackingDto
import com.example.appaeropostv2.data.local.dto.PaquetesEspecialesDto
import com.example.appaeropostv2.data.local.dto.PaquetesPorTiendaDto
import com.example.appaeropostv2.domain.reportes.ClientesPorTipoRow
import com.example.appaeropostv2.domain.reportes.FacturacionPorClienteRow
import com.example.appaeropostv2.domain.reportes.FacturacionPorMesRow
import com.example.appaeropostv2.domain.reportes.FacturacionPorTrackingRow
import com.example.appaeropostv2.domain.reportes.PaquetesEspecialesRow
import com.example.appaeropostv2.domain.reportes.PaquetesPorTiendaRow

fun ClientesPorTipoDto.toDomain(): ClientesPorTipoRow =
    ClientesPorTipoRow(tipoCliente = tipoCliente, cantidad = cantidad)

fun PaquetesPorTiendaDto.toDomain(): PaquetesPorTiendaRow =
    PaquetesPorTiendaRow(
        tiendaOrigen = tiendaOrigen,
        cantidad = cantidad,
        pesoTotal = pesoTotal,
        pesoPromedio = pesoPromedio
    )

fun PaquetesEspecialesDto.toDomain(): PaquetesEspecialesRow =
    PaquetesEspecialesRow(
        esEspecial = esEspecial == 1,
        cantidad = cantidad,
        porcentaje = porcentaje
    )

fun FacturacionPorMesDto.toDomain(): FacturacionPorMesRow =
    FacturacionPorMesRow(
        anioMes = anioMes,
        totalMonto = totalMonto,
        cantidadFacturas = cantidadFacturas,
        ticketPromedio = ticketPromedio
    )

fun FacturacionPorClienteDto.toDomain(): FacturacionPorClienteRow =
    FacturacionPorClienteRow(
        cedulaCliente = cedulaCliente,
        totalMonto = totalMonto,
        cantidadFacturas = cantidadFacturas,
        ticketPromedio = ticketPromedio
    )

fun FacturacionPorTrackingDto.toDomain(): FacturacionPorTrackingRow =
    FacturacionPorTrackingRow(
        numeroTracking = numeroTracking,
        cedulaCliente = cedulaCliente,
        fechaFacturacion = fechaFacturacion,
        montoTotal = montoTotal
    )
