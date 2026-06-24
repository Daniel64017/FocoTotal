package com.focototal.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.focototal.database.TarefaDatabase
import com.focototal.databinding.FragmentDashboardBinding
import com.focototal.util.GerenciadorLog
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * REQUISITO ACADÊMICO: Segundo Fragment do aplicativo.
 * Esta classe é responsável por computar e exibir as métricas acadêmicas (horas e tarefas)
 * e listar de forma reativa os logs do ciclo de vida coletados pelo app.
 */
class FragmentDashboard : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: TarefaDatabase
    private lateinit var logAdapter: LogCicloVidaAdapter

    companion object {
        private const val TAG = "FocoTotalCicloVida"
    }

    // Frases acadêmicas motivacionais para humanização do app
    private val frasesMotivacionais = listOf(
        "\"O sucesso acadêmico é a soma de pequenos esforços repetidos dia após dia.\"",
        "\"Estudar não é acumular informação, é desenvolver a mente para criar o futuro.\"",
        "\"A persistência supera o talento quando o talento não se esforça.\"",
        "\"A arquitetura de computadores é lógica, mas a sua dedicação é pura inspiração!\"",
        "\"Banco de dados estruturado, mobile rodando. Você está no caminho certo!\"",
        "\"Foco total nos estudos! Cada linha de código é um passo rumo ao diploma.\""
    )

    // ==========================================
    // REQUISITO ACADÊMICO: Demonstração do Ciclo de Vida do Fragment
    // Registra cada callback na console (Log.d) e no visualizador do dashboard.
    // ==========================================

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "FragmentDashboard: onAttach")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FragmentDashboard: onCreate")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onCreate")
        db = TarefaDatabase.obterInstancia(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "FragmentDashboard: onCreateView")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onCreateView")
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FragmentDashboard: onViewCreated")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onViewCreated")

        configurarRecyclerViewLogs()
        observarLogs()
        observarDadosAcademicos()
        sortearFraseMotivacional()

        // Botão para limpar a lista visual do monitor do ciclo de vida
        binding.btnLimpar.setOnClickListener {
            GerenciadorLog.limparLogs()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "FragmentDashboard: onStart")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "FragmentDashboard: onResume")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "FragmentDashboard: onPause")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "FragmentDashboard: onStop")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "FragmentDashboard: onDestroyView")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "FragmentDashboard: onDestroy")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "FragmentDashboard: onDetach")
        GerenciadorLog.registrarEvento("FragmentDashboard", "onDetach")
    }

    // ==========================================
    // Funções de Inicialização e Lógica de Negócio
    // ==========================================

    private fun configurarRecyclerViewLogs() {
        // REQUISITO ACADÊMICO: Segundo RecyclerView para exibir logs do ciclo de vida
        logAdapter = LogCicloVidaAdapter()
        binding.rvLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLogs.adapter = logAdapter
    }

    private fun observarLogs() {
        // Observa reativamente os eventos do Ciclo de Vida armazenados no Singleton GerenciadorLog
        GerenciadorLog.listaLogs.observe(viewLifecycleOwner) { logs ->
            logAdapter.submitList(logs)
            // Mantém a visualização focada no log mais recente (posição 0)
            if (logs.isNotEmpty()) {
                binding.rvLogs.scrollToPosition(0)
            }
        }
    }

    private fun observarDadosAcademicos() {
        // Coleta e calcula estatísticas do banco de dados Room para atualizar o Dashboard em tempo real
        lifecycleScope.launch {
            db.tarefaDao().obterTodasAsTarefas().collect { lista ->
                val totalTarefas = lista.size
                val concluidas = lista.count { it.concluida }
                
                // Soma apenas as horas das tarefas marcadas como concluídas
                val horasEstudadas = lista.filter { it.concluida }.sumOf { it.horasEstimadas }

                binding.txtValorHoras.text = "${horasEstudadas}h"
                binding.txtValorConcluidas.text = "$concluidas / $totalTarefas"
            }
        }
    }

    private fun sortearFraseMotivacional() {
        val indiceSorteado = Random.nextInt(frasesMotivacionais.size)
        binding.txtFraseMotivacional.text = frasesMotivacionais[indiceSorteado]
    }
}
