package com.example.appaeropost.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appaeropost.data.clientes.db.ClienteEntity
import com.example.appaeropost.data.clientes.db.ClientesDao

//Singleton de Room que registra las entidades y da los DAO.
@Database(
    entities = [ClienteEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientesDao(): ClientesDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aeropost.db"
                )
                    // Para desarrollo: destruye si cambia schema. En prod crea Migrations.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
