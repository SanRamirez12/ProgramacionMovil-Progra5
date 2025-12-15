package com.example.appaeropostv2.domain.enums

enum class Estados {
    HABILITADO, DESHABILITADO
}

enum class RolesClientes {
    VIP, FRECUENTE, NORMAL
}

enum class RolesUsuarios {
    ADMINISTRADOR, SUPERVISOR, OPERADOR, COURIER
}

enum class Tiendas {
    AMAZON, EBAY, ALIEXPRESS, TEMU, SHEIN, OTRO
}

enum class Monedas {
    USD, CRC, EUR
}

enum class Genero {
   MASCULINO, FEMENINO, OTRO, NO_DECLARA
}

enum class Casilleros {
    MIA, NYK, LAS
}

enum class TrackingStatus(
    val etiqueta: String,
    val descripcion: String,
    val paso: Int
) {
    ORDERED(
        etiqueta = "Ordenado",
        descripcion = "El paquete ha sido registrado.",
        paso = 0
    ),
    PACKED(
        etiqueta = "Empacado",
        descripcion = "El paquete está siendo empacado en el casillero.",
        paso = 1
    ),
    IN_TRANSIT(
        etiqueta = "En tránsito",
        descripcion = "El paquete va en camino a Costa Rica.",
        paso = 2
    ),
    DELIVERED(
        etiqueta = "Entregado",
        descripcion = "El paquete llegó a oficinas centrales.",
        paso = 3
    )
}
