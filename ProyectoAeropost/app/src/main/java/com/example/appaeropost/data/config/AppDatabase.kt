package com.example.appaeropost.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appaeropost.data.clientes.db.ClienteEntity
import com.example.appaeropost.data.clientes.db.ClientesDao
import com.example.appaeropost.data.paquetes.db.PaquetesCanceladoEntity
import com.example.appaeropost.data.paquetes.db.PaquetesDao
import com.example.appaeropost.data.paquetes.db.PaquetesEntity
import com.example.appaeropost.data.usuarios.db.UsuariosDao
import com.example.appaeropost.data.usuarios.db.UsuariosEntity

@Database(
    entities = [
        ClienteEntity::class,
        UsuariosEntity::class,
        PaquetesEntity::class,
        PaquetesCanceladoEntity::class
    ],
    version = 4,              // ⬅️ subimos versión
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun clientesDao(): ClientesDao
    abstract fun usuariosDao(): UsuariosDao
    abstract fun paquetesDao(): PaquetesDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aeropost.db"
                )
                    // En dev puedes mantener esto. Para prod crea Migrations.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}





