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
import com.example.appaeropostv2.data.local.dao.DaoTracking
import com.example.appaeropostv2.data.local.dao.DaoUsuario
import com.example.appaeropostv2.data.local.entity.EntityBitacora
import com.example.appaeropostv2.data.local.entity.EntityCliente
import com.example.appaeropostv2.data.local.entity.EntityFacturacion
import com.example.appaeropostv2.data.local.entity.EntityPaquete
import com.example.appaeropostv2.data.local.entity.EntityUsuario
import com.example.appaeropostv2.data.local.entity.EntityTracking

@Database(
    entities = [
        EntityUsuario::class,
        EntityCliente::class,
        EntityPaquete::class,
        EntityFacturacion::class,
        EntityBitacora::class,
        EntityTracking::class,
    ],
    version = 4, // ⬅⬅ Subido de 2 a 3 para aplicar destructive migration
    exportSchema = true
)

@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): DaoUsuario
    abstract fun clienteDao(): DaoCliente
    abstract fun paqueteDao(): DaoPaquete
    abstract fun facturacionDao(): DaoFacturacion
    abstract fun bitacoraDao(): DaoBitacora

    abstract fun trackingDao(): DaoTracking


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aeropost_v2.db"
                )
                    .fallbackToDestructiveMigration() // borra y recrea si cambia version
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
