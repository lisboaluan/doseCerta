package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityHomeBinding
import com.luanlisboa.dosecerta.fragments.AnotacaoFragment
import com.luanlisboa.dosecerta.fragments.PerfilFragment
import com.luanlisboa.dosecerta.fragments.AgendaFragment
import com.luanlisboa.dosecerta.fragments.TratamentoFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(TratamentoFragment())  // Fragmento inicial

        supportActionBar?.hide()

        binding.btnNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tratamento -> replaceFragment(TratamentoFragment())
                R.id.menu_relatorio -> replaceFragment(AgendaFragment())
                R.id.menu_anotacao -> replaceFragment(AnotacaoFragment())
                R.id.menu_perfil -> replaceFragment(PerfilFragment())
                else -> replaceFragment(TratamentoFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentMenu, fragment)

        // Adiciona o fragmento à pilha de retorno
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    // Sobrescreve o comportamento do botão "voltar" para evitar fechar a HomeActivity
    override fun onBackPressed() {
        // Se não houver mais fragmentos na pilha, minimiza o app ao invés de fechar
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()  // Volta para o fragmento anterior
        } else {
            moveTaskToBack(true)  // Minimiza o app
        }
    }
}
