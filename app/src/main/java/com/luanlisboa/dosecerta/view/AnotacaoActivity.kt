package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityAnotacaoBinding
import com.luanlisboa.dosecerta.repository.AnotacaoRepository
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class AnotacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnotacaoBinding
    private lateinit var anotacaoRepository: AnotacaoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        anotacaoRepository = AnotacaoRepository(this)

        binding.saveButton.setOnClickListener{
            val titulo = binding.editTituloAnotacao.text.toString()
            val mensagem = binding.editAnotacao.text.toString()
            when {
                titulo.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira um título!")
                }
                mensagem.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Iscreva uma anotação!")
                }
                else -> {

                    val resultado = anotacaoRepository.inserirAnotacao(
                        titulo,
                        mensagem
                    )

                    if (resultado > 0) {
                        SnackbarUtils.mensagem(it, "Anotação salva com sucesso!")
                        finish()
                    } else {
                        SnackbarUtils.mensagem(it, "Erro ao salvar anotação.")
                    }
                }
            }
        }
    }
}
