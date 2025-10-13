package com.example.appaeropost.domain.clientes

data class Cliente(
    val id: Int,
    val nombre: String,
    val identificacion: String,
    val telefono: String,
    val tipo: String
)
