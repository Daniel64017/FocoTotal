package com.focototal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.focototal.model.TarefaEstudo

/**
 * Classe principal do Banco de Dados Room. Configura as entidades do projeto FocoTotal
 * e implementa o padrão Singleton para acesso seguro à instância do banco de dados.
 */
@Database(entities = [TarefaEstudo::class], version = 1, exportSchema = false)
abstract class TarefaDatabase : RoomDatabase() {

    abstract fun tarefaDao(): TarefaDao

    companion object {
        @Volatile
        private var INSTANCE: TarefaDatabase? = null

        fun obterInstancia(context: Context): TarefaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    TarefaDatabase::class.java,
                    "foco_total_database"
                )
                .fallbackToDestructiveMigration() // Facilita atualizações na fase acadêmica/desenvolvimento
                .build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}
