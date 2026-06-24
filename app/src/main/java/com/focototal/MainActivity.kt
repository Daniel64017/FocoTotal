package com.focototal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.focototal.databinding.ActivityMainBinding
import com.focototal.util.GerenciadorLog
import com.focototal.view.FragmentDashboard
import com.focototal.view.FragmentTarefas

/**
 * REQUISITO ACADÊMICO: Activity principal do aplicativo.
 * Gerencia a navegação inferior (Bottom Navigation) e demonstra callbacks do ciclo de vida.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "FocoTotalCicloVida"
    }

    // ==========================================
    // REQUISITO ACADÊMICO: Ciclo de Vida da Activity
    // Cada callback registra o evento no console e no monitor visual.
    // ==========================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Log.d(TAG, "MainActivity: onCreate")
        GerenciadorLog.registrarEvento("MainActivity", "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarNavegacao()

        // Exibe o primeiro fragment por padrão se for a primeira inicialização
        if (savedInstanceState == null) {
            substituirFragmento(FragmentTarefas())
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MainActivity: onStart")
        GerenciadorLog.registrarEvento("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity: onResume")
        GerenciadorLog.registrarEvento("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity: onPause")
        GerenciadorLog.registrarEvento("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity: onStop")
        GerenciadorLog.registrarEvento("MainActivity", "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "MainActivity: onRestart")
        GerenciadorLog.registrarEvento("MainActivity", "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity: onDestroy")
        GerenciadorLog.registrarEvento("MainActivity", "onDestroy")
    }

    // ==========================================
    // Configuração de Navegação (Bottom Navigation)
    // ==========================================

    private fun configurarNavegacao() {
        // REQUISITO ACADÊMICO: Bottom Navigation para alternar entre as duas telas (Fragments)
        binding.navegacaoInferior.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_tarefas -> {
                    substituirFragmento(FragmentTarefas())
                    true
                }
                R.id.menu_dashboard -> {
                    substituirFragmento(FragmentDashboard())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * REQUISITO ACADÊMICO: Uso de Fragment e navegação dinâmica.
     * Substitui o fragment atual no container.
     */
    private fun substituirFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.conteudo_fragmento, fragmento)
            .commit()
    }
}
