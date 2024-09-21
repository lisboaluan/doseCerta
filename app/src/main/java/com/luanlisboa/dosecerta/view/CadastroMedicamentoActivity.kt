package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityCadastroMedicamentoBinding
import com.luanlisboa.dosecerta.repository.MedicamentoRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.CustomSpinnerAdapter
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class CadastroMedicamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroMedicamentoBinding
    private lateinit var medicamentoRepository: MedicamentoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroMedicamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicamentoRepository = MedicamentoRepository(this)

        val options = resources.getStringArray(R.array.formato)
        val adapter = CustomSpinnerAdapter(this, options)
        binding.spinnerFormato.adapter = adapter

        binding.btnSeguinte.setOnClickListener {
            val nomeMedicamento = binding.editNomeMedicamento.text.toString()
            val spinnerFormato = binding.spinnerFormato.selectedItem.toString()

            val sharedPreferences = getSharedPreferences("FormatoPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("selectedFormat", spinnerFormato)
            editor.apply()

            val unidadeMedida = binding.editUnidadeMedida.text.toString()
            val spinnerUnidadeMedida = binding.spinnerUnidadeMedida.selectedItem.toString()
            val estoque = binding.editEstoque.text.toString()
            val spinnerEstoque = binding.spinnerEstoque.selectedItem.toString()

            when {
                nomeMedicamento.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira o nome do medicamento!")
                }
                spinnerFormato == options[0] -> {
                    SnackbarUtils.mensagem(it, "Selecione um formato!")
                }
                unidadeMedida.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira a unidade de medida!")
                }
                estoque.isNotEmpty() && spinnerEstoque == options[0] -> {
                    SnackbarUtils.mensagem(it, "Selecione um formato no campo estoque!")
                }
                spinnerEstoque != options[0] && estoque.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Informe um valor no campo estoque!")
                }
                else -> {
                    val estoqueInt = if (estoque.isEmpty()) 0 else estoque.toInt()
                    val estoqueFormato = if (spinnerEstoque == options[0]) null else spinnerEstoque

                    val resultado = medicamentoRepository.inserirMedicamento(
                        nomeMedicamento,
                        spinnerFormato,
                        unidadeMedida,
                        spinnerUnidadeMedida,
                        estoqueInt,
                        estoqueFormato
                    )

                    if (resultado > 0) {
                        SnackbarUtils.mensagem(it, "Medicamento cadastrado com sucesso!")
                        RouterManager.direcionarParaCadastroAlerta(this, resultado)
                    } else {
                        SnackbarUtils.mensagem(it, "Erro ao cadastrar medicamento.")
                    }
                }
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}
