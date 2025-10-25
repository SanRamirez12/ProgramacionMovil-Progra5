package com.example.appaeropost.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appaeropost.data.clientes.db.ClienteEntity
import com.example.appaeropost.data.clientes.db.ClientesDao
import com.example.appaeropost.data.usuarios.db.UsuariosEntity
import com.example.appaeropost.data.usuarios.db.UsuariosDao

//Singleton de Room que registra las entidades y da los DAO.
@Database(
    entities = [ClienteEntity::class, UsuariosEntity::class],
    version = 2,                 // súbela si agregaste la tabla
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientesDao(): com.example.appaeropost.data.clientes.db.ClientesDao
    abstract fun usuariosDao(): com.example.appaeropost.data.usuarios.db.UsuariosDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aeropost.db"
                )
                    .fallbackToDestructiveMigration()  // en dev, recrea al cambiar versión
                    .build()
                    .also { INSTANCE = it }
            }
    }
}



