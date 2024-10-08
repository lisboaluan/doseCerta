package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityAnotacaoBinding
import com.luanlisboa.dosecerta.repository.AnotacaoRepository
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class AnotacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnotacaoBinding
    private lateinit var anotacaoRepository: AnotacaoRepository
    private var anotacaoId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        anotacaoRepository = AnotacaoRepository(this)

        anotacaoId = intent.getStringExtra("anotacaoId")
        if (anotacaoId != null) {
            carregarAnotacao(anotacaoId!!)
        }

        binding.saveButton.setOnClickListener {
            val titulo = binding.editTituloAnotacao.text.toString()
            val mensagem = binding.editAnotacao.text.toString()
            when {
                titulo.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira um título!")
                }

                mensagem.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Escreva uma anotação!")
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
        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
    private fun carregarAnotacao(anotacaoId: String) {
        val anotacao = anotacaoRepository.getAnotacaoById(anotacaoId)
        if (anotacao != null) {
            binding.editTituloAnotacao.setText(anotacao.titulo)
            binding.editAnotacao.setText(anotacao.mensagem)
        }
    }


}
