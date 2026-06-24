package com.focototal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * REQUISITO ACADÊMICO: Entidade representando um objeto do sistema (Tarefa de Estudo).
 * Esta classe é anotada com @Entity do Room Database para mapeá-la para uma tabela
 * local SQLite. Representa o objeto de negócio "Tarefa" ou "Atividade de Estudo".
 */
@Entity(tableName = "tarefas_estudo")
data class TarefaEstudo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val materia: String,          // Ex: "Desenvolvimento Mobile", "Álgebra Linear"
    val descricao: String,        // Ex: "Fazer o projeto acadêmico de Bottom Navigation"
    val horasEstimadas: Int,      // Ex: 3
    val concluida: Boolean = false // Indica se o aluno já concluiu essa tarefa de estudos
)
