package com.example.appaeropostv2.presentation.pdf

import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.model.FacturaConDetalle

/**
 * Se encarga solo de construir el HTML de la factura.
 * Esto es "presentación pura" (cómo se ve el comprobante).
 */
object FacturaHtmlBuilder {

    fun build(detalle: FacturaConDetalle, selloDigital: String): String {
        val factura = detalle.factura
        val cliente = detalle.cliente
        val paquete = detalle.paquete

        val symbol = when (paquete.monedasPaquete) {
            Monedas.USD -> "$"
            Monedas.CRC -> "₡"
            Monedas.EUR -> "€"
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8" />
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f5f5f5;
                    }

                    .contenedor {
                        border: 2px solid #000;
                        padding: 20px;
                        margin: 16px;
                        background-color: #ffffff;
                    }

                    h1 {
                        text-align: center;
                        margin: 0;
                        font-size: 26px;
                        padding-bottom: 10px;
                    }

                    h2 {
                        border-bottom: 1px solid #333;
                        padding-bottom: 4px;
                        margin-top: 25px;
                        font-size: 18px;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin-top: 10px;
                    }

                    td, th {
                        border: 1px solid #444;
                        padding: 6px;
                        font-size: 14px;
                    }

                    th {
                        background-color: #e0e0e0;
                    }

                    .total {
                        font-size: 20px;
                        font-weight: bold;
                        text-align: right;
                        margin-top: 15px;
                    }

                    .sello {
                        margin-top: 25px;
                        font-size: 12px;
                        text-align: center;
                        color: #555;
                    }
                </style>
            </head>
            <body>
                <div class="contenedor">
                    <h1>FACTURA OFICIAL</h1>

                    <h2>Datos del Cliente</h2>
                    <table>
                        <tr>
                            <td><b>Nombre:</b></td>
                            <td>${cliente.nombreCliente}</td>
                        </tr>
                        <tr>
                            <td><b>Cédula:</b></td>
                            <td>${cliente.cedulaCliente}</td>
                        </tr>
                        <tr>
                            <td><b>Teléfono:</b></td>
                            <td>${cliente.telefonoCliente}</td>
                        </tr>
                        <tr>
                            <td><b>Correo:</b></td>
                            <td>${cliente.correoCliente}</td>
                        </tr>
                        <tr>
                            <td><b>Dirección entrega:</b></td>
                            <td>${factura.direccionEntrega}</td>
                        </tr>
                    </table>

                    <h2>Datos del Paquete</h2>
                    <table>
                        <tr>
                            <td><b>Tracking:</b></td>
                            <td>${factura.numeroTracking}</td>
                        </tr>
                        <tr>
                            <td><b>Peso:</b></td>
                            <td>${factura.pesoPaquete} kg</td>
                        </tr>
                        <tr>
                            <td><b>Valor declarado:</b></td>
                            <td>${symbol}${String.format("%.2f", factura.valorBrutoPaquete)}</td>
                        </tr>
                        <tr>
                            <td><b>Fecha registro:</b></td>
                            <td>${paquete.fechaRegistro}</td>
                        </tr>
                        <tr>
                            <td><b>Origen:</b></td>
                            <td>${paquete.tiendaOrigen} / ${paquete.casillero}</td>
                        </tr>
                        <tr>
                            <td><b>Condición especial:</b></td>
                            <td>${if (factura.productoEspecial) "Sí" else "No"}</td>
                        </tr>
                    </table>

                    <h2>Detalle de Cargos</h2>
                    <table>
                        <tr>
                            <th>Concepto</th>
                            <th>Monto</th>
                        </tr>
                        <tr>
                            <td>Peso × 12</td>
                            <td>${symbol}${String.format("%.2f", factura.pesoPaquete * 12)}</td>
                        </tr>
                        <tr>
                            <td>Impuesto (13%)</td>
                            <td>${symbol}${String.format("%.2f", factura.valorBrutoPaquete * 0.13)}</td>
                        </tr>
                        <tr>
                            <td>Recargo producto especial (10% si aplica, 5% si no)</td>
                            <td>${
            symbol + String.format(
                "%.2f",
                if (factura.productoEspecial)
                    factura.valorBrutoPaquete * 0.10
                else
                    factura.valorBrutoPaquete * 0.05
            )
        }</td>
                        </tr>
                    </table>

                    <div class="total">
                        TOTAL A PAGAR: ${symbol}${String.format("%.2f", factura.montoTotal)}
                    </div>

                    <div class="sello">
                        Sello digital: $selloDigital<br/>
                        Documento generado electrónicamente por AeropostApp.
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}
