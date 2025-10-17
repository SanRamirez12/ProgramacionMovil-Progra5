package com.example.appaeropost.domain.clientes

enum class EstadoCliente { HABILITADO, DESHABILITADO }
enum class TipoCliente { REGULAR, VIP, SUCURSAL }

data class Cliente(
    val id: Int,
    val nombre: String,
    val identificacion: String,
    val telefono: String,
    val correo: String,
    val tipo: TipoCliente,
    val estado: EstadoCliente,
    val direccionEntrega: String
)
