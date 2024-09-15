package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityHomeBinding
import com.luanlisboa.dosecerta.fragments.AnotacaoFragment
import com.luanlisboa.dosecerta.fragments.PerfilFragment
import com.luanlisboa.dosecerta.fragments.RelatorioFragment
import com.luanlisboa.dosecerta.fragments.TratamentoFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(TratamentoFragment())

        supportActionBar?.hide()

        binding.btnNavView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.menu_tratamento -> replaceFragment(TratamentoFragment())
                R.id.menu_relatorio -> replaceFragment(RelatorioFragment())
                R.id.menu_anotacao -> replaceFragment(AnotacaoFragment())
                R.id.menu_perfil -> replaceFragment(PerfilFragment())

                else ->{
                    replaceFragment(TratamentoFragment())
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment ){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentMenu, fragment)
        fragmentTransaction.commit()
    }
}