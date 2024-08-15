package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityHomeBinding

class CadastroMedicamentoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}