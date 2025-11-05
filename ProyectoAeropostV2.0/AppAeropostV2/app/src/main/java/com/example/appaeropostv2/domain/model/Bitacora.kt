package com.example.appaeropostv2.domain.model

data class Bitacora(
    val idBitacora: Int,
    val username: String,
    val fechaLogin: String,
    val fechaLogout: String
)