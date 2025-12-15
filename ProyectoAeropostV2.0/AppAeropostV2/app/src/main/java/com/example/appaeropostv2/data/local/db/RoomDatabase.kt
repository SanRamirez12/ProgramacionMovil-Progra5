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
import com.example.appaeropostv2.data.local.dao.DaoReporte
import com.example.appaeropostv2.data.local.dao.DaoTracking
import com.example.appaeropostv2.data.local.dao.DaoUsuario
import com.example.appaeropostv2.data.local.entity.EntityBitacora
import com.example.appaeropostv2.data.local.entity.EntityCliente
import com.example.appaeropostv2.data.local.entity.EntityFacturacion
import com.example.appaeropostv2.data.local.entity.EntityPaquete
import com.example.appaeropostv2.data.local.entity.EntityReporte
import com.example.appaeropostv2.data.local.entity.EntityTracking
import com.example.appaeropostv2.data.local.entity.EntityUsuario

@Database(
    entities = [
        EntityUsuario::class,
        EntityCliente::class,
        EntityPaquete::class,
        EntityFacturacion::class,
        EntityBitacora::class,
        EntityTracking::class,
        EntityReporte::class,
    ],
    version = 5, // subir versión (tenías 4) :contentReference[oaicite:2]{index=2}
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

    abstract fun reporteDao(): DaoReporte

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
