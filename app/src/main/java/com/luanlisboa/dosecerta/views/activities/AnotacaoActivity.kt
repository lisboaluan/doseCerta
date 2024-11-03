package com.luanlisboa.dosecerta.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityAnotacaoBinding
import com.luanlisboa.dosecerta.models.Anotacoes
import com.luanlisboa.dosecerta.repositories.AnotacaoRepository
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class AnotacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnotacaoBinding
    private lateinit var anotacaoRepository: AnotacaoRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        anotacaoRepository = AnotacaoRepository(this)

        binding.saveButton.setOnClickListener {
            val titulo = binding.editTituloAnotacao.text.toString()
            val mensagem = binding.editAnotacao.text.toString()
            when {
                titulo.isEmpty() -> {
                    Toast.makeText(this, "Insira um título!", Toast.LENGTH_SHORT).show()
                }

                mensagem.isEmpty() -> {
                    Toast.makeText(this, "Escreva uma anotação!", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val resultado = anotacaoRepository.inserirAnotacao(
                        titulo,
                        mensagem
                    )
                    if (resultado > 0) {
                        Toast.makeText(this, "Anotação salva com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Erro ao salvar anotação.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}
