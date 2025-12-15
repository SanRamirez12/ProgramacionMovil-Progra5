package com.example.appaeropostv2.domain.logic


object CoordinatesLogic {

    data class Punto(val latitud: Double, val longitud: Double)

    // Downtown aproximado
    val MIAMI = Punto(25.7617, -80.1918)
    val LOS_ANGELES = Punto(34.0522, -118.2437)
    val NEW_YORK = Punto(40.7128, -74.0060)

    // SJO (Juan Santamaría) aproximado
    val JUAN_SANTAMARIA = Punto(9.9939, -84.2088)

    // Universidad Latina (aprox. San Pedro / SJ) — ajustable si querés exacto
    val UNIVERSIDAD_LATINA = Punto(9.9350, -84.0517)
}
