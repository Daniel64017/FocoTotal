package com.focototal.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focototal.R
import com.focototal.databinding.ItemLogCicloVidaBinding
import com.focototal.model.LogCicloVida

/**
 * REQUISITO ACADÊMICO: Segundo RecyclerView do projeto.
 * Este Adapter exibe a lista dos eventos capturados do ciclo de vida em tempo real.
 * Inclui colorização dinâmica para cada tipo de callback de ciclo de vida.
 */
class LogCicloVidaAdapter : ListAdapter<LogCicloVida, LogCicloVidaAdapter.LogViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemLogCicloVidaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = getItem(position)
        holder.bind(log)
    }

    inner class LogViewHolder(private val binding: ItemLogCicloVidaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(log: LogCicloVida) {
            binding.txtLogHora.text = log.timestamp
            binding.txtLogComponente.text = log.componente
            binding.txtLogEvento.text = "${log.evento}()"

            val context = binding.root.context
            val evento = log.evento.lowercase()

            // REQUISITO ACADÊMICO: Diferenciação visual dos estados do ciclo de vida
            when {
                // Estados de Inicialização (Criado) -> Lilás/Violeta
                evento.contains("create") -> {
                    binding.txtLogEvento.backgroundTintList = ContextCompat.getColorStateList(context, R.color.primary_light)
                    binding.txtLogEvento.setTextColor(ContextCompat.getColor(context, R.color.primary))
                }
                // Estados Ativos (Visível/Interativo) -> Verde
                evento.contains("start") || evento.contains("resume") -> {
                    binding.txtLogEvento.backgroundTintList = ContextCompat.getColorStateList(context, R.color.accent_light)
                    binding.txtLogEvento.setTextColor(ContextCompat.getColor(context, R.color.accent))
                }
                // Estados de Pausa/Parada (Inativo temporariamente) -> Laranja
                evento.contains("pause") || evento.contains("stop") -> {
                    binding.txtLogEvento.backgroundTintList = ContextCompat.getColorStateList(context, R.color.pendente_light)
                    binding.txtLogEvento.setTextColor(ContextCompat.getColor(context, R.color.pendente))
                }
                // Estados de Destruição -> Vermelho
                evento.contains("destroy") || evento.contains("detach") -> {
                    binding.txtLogEvento.backgroundTintList = ContextCompat.getColorStateList(context, R.color.white)
                    binding.txtLogEvento.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                else -> {
                    binding.txtLogEvento.backgroundTintList = ContextCompat.getColorStateList(context, R.color.border)
                    binding.txtLogEvento.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LogCicloVida>() {
        override fun areItemsTheSame(oldItem: LogCicloVida, newItem: LogCicloVida): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LogCicloVida, newItem: LogCicloVida): Boolean {
            return oldItem == newItem
        }
    }
}
