package com.focototal.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focototal.model.LogCicloVida
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilitário singleton responsável por acumular e gerenciar os logs de eventos de ciclo de vida
 * das Activities e Fragments no aplicativo. Permite expor esses eventos de forma reativa
 * com LiveData para visualização instantânea na tela de Dashboard.
 */
object GerenciadorLog {
    
    private val _listaLogs = MutableLiveData<List<LogCicloVida>>(emptyList())
    val listaLogs: LiveData<List<LogCicloVida>> = _listaLogs

    /**
     * Registra um novo log do ciclo de vida.
     * @param componente O nome da classe que disparou o evento (Ex: MainActivity, FragmentDashboard).
     * @param evento O método do ciclo de vida executado (Ex: onCreate, onDestroy).
     */
    fun registrarEvento(componente: String, evento: String) {
        val formatoHora = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        val horaAtual = formatoHora.format(Date())
        
        val novoLog = LogCicloVida(
            componente = componente,
            evento = evento,
            timestamp = horaAtual
        )
        
        // Obter lista atual, adicionar novo item no início (posição 0) e atualizar LiveData
        val listaAtual = _listaLogs.value.orEmpty().toMutableList()
        listaAtual.add(0, novoLog)
        _listaLogs.postValue(listaAtual)
    }

    /**
     * Limpa todo o histórico de logs da tela do Dashboard.
     */
    fun limparLogs() {
        _listaLogs.postValue(emptyList())
    }
}
