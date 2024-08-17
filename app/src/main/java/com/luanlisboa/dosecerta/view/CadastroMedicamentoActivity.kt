package com.luanlisboa.dosecerta.view

import com.luanlisboa.dosecerta.utils.CustomSpinnerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityCadastroMedicamentoBinding
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class CadastroMedicamentoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroMedicamentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroMedicamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val options = resources.getStringArray(R.array.formato)

        val adapter = CustomSpinnerAdapter(this, options)
        binding.spinnerFormato.adapter = adapter

        binding.btnSeguinte.setOnClickListener{

            val nomeMedicamento = binding.editNomeMedicamento.text.toString()
            val spinnerFormato = binding.spinnerFormato.selectedItem.toString()
            val unidadeMedida = binding.editUnidadeMedida.text.toString()
            val estoque = binding.editEstoque.text.toString()
            val spinnerEstoque = binding.spinnerEstoque.selectedItem.toString()

            when{
                nomeMedicamento.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira o nome do medicamento!")
                }spinnerFormato == options[0] -> {
                    SnackbarUtils.mensagem(it, "Selecione um formato!")
                }unidadeMedida.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira a unidade de medida!")
                }estoque.isNotEmpty() && spinnerEstoque == options[0] -> {
                    SnackbarUtils.mensagem(it, "Selecione um formato no campo estoque!")
                }spinnerEstoque != options[0] && estoque.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Informe um valor no campo estoque!")
                }else -> {
                    RouterManager.direcionarParaCadastroAlerta(this)
                }
            }
        }

            binding.btnVoltar.setOnClickListener{
                finish()
            }
        }
    }
