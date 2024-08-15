package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityHomeBinding
import com.luanlisboa.dosecerta.router.RouterManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrarTratamento.setOnClickListener{
            RouterManager.direcionarParaCadastroMedicamento(this)
        }

        supportActionBar?.hide()




    }
}