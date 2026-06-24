package com.focototal.database

import androidx.room.*
import com.focototal.model.TarefaEstudo
import kotlinx.coroutines.flow.Flow

/**
 * Interface DAO (Data Access Object) do Room para definir as operações do banco de dados 
 * sobre a entidade TarefaEstudo.
 */
@Dao
interface TarefaDao {
    
    @Query("SELECT * FROM tarefas_estudo ORDER BY id DESC")
    fun obterTodasAsTarefas(): Flow<List<TarefaEstudo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTarefa(tarefa: TarefaEstudo): Long

    @Update
    suspend fun atualizarTarefa(tarefa: TarefaEstudo)

    @Delete
    suspend fun deletarTarefa(tarefa: TarefaEstudo)
}
