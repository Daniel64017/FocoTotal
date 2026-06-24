package com.focototal.model

import java.util.UUID

/**
 * REQUISITO ACADÊMICO: Classe de dados (data class).
 * Esta classe é utilizada para estruturar os dados de logs dos eventos do Ciclo de Vida 
 * da Activity e dos Fragments que ocorrem durante o uso do aplicativo FocoTotal.
 */
data class LogCicloVida(
    val id: String = UUID.randomUUID().toString(),
    val componente: String, // Nome do componente (Ex: MainActivity, FragmentTarefas)
    val evento: String,     // Estado do ciclo de vida (Ex: onCreate, onResume)
    val timestamp: String   // Horário da ocorrência (Ex: 20:32:01)
)
