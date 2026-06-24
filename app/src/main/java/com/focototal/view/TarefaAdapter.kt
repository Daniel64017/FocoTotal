package com.focototal.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focototal.R
import com.focototal.databinding.ItemTarefaBinding
import com.focototal.model.TarefaEstudo

/**
 * REQUISITO ACADÊMICO: Implementação de RecyclerView.
 * Este Adapter estende ListAdapter para gerenciar a lista de tarefas de estudo de forma otimizada.
 * Utiliza View Binding para conectar os elementos visuais definidos em item_tarefa.xml.
 */
class TarefaAdapter(
    private val onCheckChanged: (TarefaEstudo, Boolean) -> Unit,
    private val onDeleteClicked: (TarefaEstudo) -> Unit
) : ListAdapter<TarefaEstudo, TarefaAdapter.TarefaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val binding = ItemTarefaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TarefaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = getItem(position)
        holder.bind(tarefa)
    }

    inner class TarefaViewHolder(private val binding: ItemTarefaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tarefa: TarefaEstudo) {
            binding.txtMateria.text = tarefa.materia
            binding.txtDescricao.text = tarefa.descricao
            binding.txtHoras.text = "${tarefa.horasEstimadas} h planejadas"

            // Evitar disparos indesejados ao reciclar a view
            binding.cbConcluida.setOnCheckedChangeListener(null)
            binding.cbConcluida.isChecked = tarefa.concluida

            // Estilizar textos com base no status (concluído ou pendente)
            if (tarefa.concluida) {
                binding.txtDescricao.paintFlags = binding.txtDescricao.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                binding.txtDescricao.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_secondary))
                binding.txtHoras.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.accent_light)
                binding.txtHoras.setTextColor(ContextCompat.getColor(binding.root.context, R.color.accent))
                binding.txtHoras.text = "Concluído ✅"
            } else {
                binding.txtDescricao.paintFlags = binding.txtDescricao.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.txtDescricao.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_primary))
                binding.txtHoras.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.primary_light)
                binding.txtHoras.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primary_dark))
            }

            // Ouvinte de clique no Checkbox
            binding.cbConcluida.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(tarefa, isChecked)
            }

            // Ouvinte de clique no botão de Excluir
            binding.btnDeletar.setOnClickListener {
                onDeleteClicked(tarefa)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TarefaEstudo>() {
        override fun areItemsTheSame(oldItem: TarefaEstudo, newItem: TarefaEstudo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TarefaEstudo, newItem: TarefaEstudo): Boolean {
            return oldItem == newItem
        }
    }
}
