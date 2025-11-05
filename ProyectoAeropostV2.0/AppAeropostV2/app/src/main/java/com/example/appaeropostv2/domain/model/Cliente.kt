package com.example.appaeropostv2.domain.model

import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.enums.RolesClientes

data class Cliente(
    val idCliente: Int,
    val nombreCliente: String,
    val cedulaCliente: String,
    val telefonoCliente: String,
    val correoCliente: String,
    val tipoCliente: RolesClientes,
    val estadoCliente: Estados,
    val direccionEntrega: String
)