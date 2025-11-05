// Ruta: app/src/main/java/com/example/appaeropostv2/data/local/db/RoomDatabase.kt
package com.example.appaeropostv2.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appaeropostv2.data.local.dao.DaoBitacora
import com.example.appaeropostv2.data.local.dao.DaoCliente
import com.example.appaeropostv2.data.local.dao.DaoFacturacion
import com.example.appaeropostv2.data.local.dao.DaoPaquete
import com.example.appaeropostv2.data.local.dao.DaoUsuario
import com.example.appaeropostv2.data.local.entity.EntityBitacora
import com.example.appaeropostv2.data.local.entity.EntityCliente
import com.example.appaeropostv2.data.local.entity.EntityFacturacion
import com.example.appaeropostv2.data.local.entity.EntityPaquete
import com.example.appaeropostv2.data.local.entity.EntityUsuario

/**
 * AppDatabase es el punto de entrada a la persistencia local con Room.
 * - @Database: lista de entidades (tablas) y versión del esquema.
 * - @TypeConverters: convertidores para tipos no soportados nativamente (LocalDate, enums, etc.)
 *
 * Nota: empezamos SOLO con EntityUsuario y UsuarioDao para que puedas correr ya.
 * Luego agregas tus otras entidades/DAOs (Cliente, Paquete, Facturación, etc.)
 */
@Database(
    entities = [
        EntityUsuario::class,
        EntityCliente::class,
        EntityPaquete::class,
        EntityFacturacion::class,
        EntityBitacora::class,
    ],
    version = 1, // Cambia cuando alteres el esquema
    exportSchema = true // genera JSON de esquema (útil para migraciones)
)

@TypeConverters(TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    // DAOs expuestos por la DB
    abstract fun usuarioDao(): DaoUsuario
    abstract fun clienteDao(): DaoCliente
    abstract fun paqueteDao(): DaoPaquete
    abstract fun facturacionDao(): DaoFacturacion
    abstract fun bitacoraDao(): DaoBitacora

    // Singleton


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene una instancia Singleton de la base de datos.
         * - fallbackToDestructiveMigration(): útil en desarrollo; borra y recrea si cambia version.
         *   En producción, reemplázalo por MIGRATIONS reales.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aeropost_v2.db" // nombre del archivo .db
                )
                    // Para DEV: destruye y recrea si cambia la version
                    .fallbackToDestructiveMigration()
                    // Si quieres ver/guardar los esquemas: configura room.schemaLocation en Gradle
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}

