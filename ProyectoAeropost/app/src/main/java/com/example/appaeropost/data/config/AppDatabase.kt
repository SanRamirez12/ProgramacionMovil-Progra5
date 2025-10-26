package com.example.appaeropost.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appaeropost.data.clientes.db.ClienteEntity
import com.example.appaeropost.data.clientes.db.ClientesDao
import com.example.appaeropost.data.usuarios.db.UsuariosDao
import com.example.appaeropost.data.usuarios.db.UsuariosEntity

@Database(
    entities = [ClienteEntity::class, UsuariosEntity::class],
    version = 3,                // ⬅️ súbela
    exportSchema = true
)
@TypeConverters(RoomConverters::class)   // ⬅️ IMPORTA el de com.example.appaeropost.data.config
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientesDao(): ClientesDao
    abstract fun usuariosDao(): UsuariosDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aeropost.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}




