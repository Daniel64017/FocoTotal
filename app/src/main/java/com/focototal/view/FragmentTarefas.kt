package com.focototal.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.focototal.R
import com.focototal.database.TarefaDatabase
import com.focototal.databinding.DialogNovaTarefaBinding
import com.focototal.databinding.FragmentTarefasBinding
import com.focototal.model.TarefaEstudo
import com.focototal.util.GerenciadorLog
import kotlinx.coroutines.launch

/**
 * REQUISITO ACADÊMICO: Primeiro Fragment do aplicativo.
 * Esta classe gerencia a interface do usuário para visualização e adição de tarefas de estudo.
 */
class FragmentTarefas : Fragment() {

    private var _binding: FragmentTarefasBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var db: TarefaDatabase
    private lateinit var adapter: TarefaAdapter

    companion object {
        private const val TAG = "FocoTotalCicloVida"
    }

    // ==========================================
    // REQUISITO ACADÊMICO: Demonstração do Ciclo de Vida do Fragment
    // Cada callback registra o evento no console (Log.d) e no visualizador de tela.
    // ==========================================

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "FragmentTarefas: onAttach")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FragmentTarefas: onCreate")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onCreate")
        
        // Inicializa o banco de dados Room
        db = TarefaDatabase.obterInstancia(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "FragmentTarefas: onCreateView")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onCreateView")
        _binding = FragmentTarefasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "FragmentTarefas: onViewCreated")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onViewCreated")

        configurarRecyclerView()
        observarDadosBanco()

        // Clique no FAB para adicionar nova tarefa
        binding.fabAdicionar.setOnClickListener {
            exibirDialogNovaTarefa()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "FragmentTarefas: onStart")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "FragmentTarefas: onResume")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "FragmentTarefas: onPause")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "FragmentTarefas: onStop")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "FragmentTarefas: onDestroyView")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "FragmentTarefas: onDestroy")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "FragmentTarefas: onDetach")
        GerenciadorLog.registrarEvento("FragmentTarefas", "onDetach")
    }

    // ==========================================
    // Funções de Inicialização e Lógica de Negócio
    // ==========================================

    private fun configurarRecyclerView() {
        // REQUISITO ACADÊMICO: Configurando o RecyclerView de tarefas
        adapter = TarefaAdapter(
            onCheckChanged = { tarefa, isChecked ->
                val tarefaAtualizada = tarefa.copy(concluida = isChecked)
                lifecycleScope.launch {
                    db.tarefaDao().atualizarTarefa(tarefaAtualizada)
                    Toast.makeText(
                        requireContext(), 
                        if (isChecked) "Tarefa concluída! Parabéns! 🎉" else "Tarefa marcada como pendente.", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onDeleteClicked = { tarefa ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Excluir Tarefa")
                    .setMessage("Deseja realmente remover esta atividade de estudos?")
                    .setPositiveButton("Sim") { _, _ ->
                        lifecycleScope.launch {
                            db.tarefaDao().deletarTarefa(tarefa)
                            Toast.makeText(requireContext(), "Tarefa excluída.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }
        )
        binding.rvTarefas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTarefas.adapter = adapter
    }

    private fun observarDadosBanco() {
        // Observa em tempo real as tarefas salvas no Room usando Kotlin Coroutines Flow
        lifecycleScope.launch {
            db.tarefaDao().obterTodasAsTarefas().collect { lista ->
                adapter.submitList(lista)
                if (lista.isEmpty()) {
                    binding.layoutEstadoVazio.visibility = View.VISIBLE
                    binding.rvTarefas.visibility = View.GONE
                } else {
                    binding.layoutEstadoVazio.visibility = View.GONE
                    binding.rvTarefas.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun exibirDialogNovaTarefa() {
        val dialogBinding = DialogNovaTarefaBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)
        
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Lista de matérias conforme requisitado pelo usuário
        val materias = arrayOf(
            "Banco de Dados",
            "Desenvolvimento Mobile",
            "Engenharia de Software",
            "Sistemas Computacionais",
            "Álgebra Linear",
            "Arquitetura de Computadores"
        )
        
        val spinnerAdapter = ArrayAdapter(
            requireContext(), 
            android.R.layout.simple_spinner_item, 
            materias
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerMaterias.adapter = spinnerAdapter

        dialogBinding.btnCancelar.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnSalvar.setOnClickListener {
            val materia = dialogBinding.spinnerMaterias.selectedItem.toString()
            val descricao = dialogBinding.etDescricao.text.toString().trim()
            val horasStr = dialogBinding.etHoras.text.toString().trim()

            if (descricao.isEmpty() || horasStr.isEmpty()) {
                Toast.makeText(
                    requireContext(), 
                    getString(R.string.erro_campos_obrigatorios), 
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val horas = horasStr.toIntOrNull() ?: 1

            // Cria e insere no banco local a tarefa mapeada pela entidade
            val novaTarefa = TarefaEstudo(
                materia = materia,
                descricao = descricao,
                horasEstimadas = horas
            )

            lifecycleScope.launch {
                db.tarefaDao().inserirTarefa(novaTarefa)
                Toast.makeText(requireContext(), "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
}
