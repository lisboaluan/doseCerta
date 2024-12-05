package com.luanlisboa.dosecerta.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityCadastroMedicamentoBinding
import com.luanlisboa.dosecerta.repositories.MedicamentoRepository
import com.luanlisboa.dosecerta.utils.RouterManager
import com.luanlisboa.dosecerta.adapters.CustomSpinnerAdapter
import com.luanlisboa.dosecerta.utils.SpinnerUtils

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
            val unidadeMedida = binding.editUnidadeMedida.text.toString()
            val spinnerUnidadeMedida = binding.spinnerUnidadeMedida.selectedItem.toString()
            val estoque = binding.editEstoque.text.toString()
            val spinnerEstoque = binding.spinnerEstoque.selectedItem.toString()

            when {
                nomeMedicamento.isEmpty() -> {
                    Toast.makeText(this, "Insira o nome do medicamento!", Toast.LENGTH_SHORT).show()
                }
                spinnerFormato == options[0] -> {
                    Toast.makeText(this, "Selecione um formato!", Toast.LENGTH_SHORT).show()
                }
                unidadeMedida.isEmpty() -> {
                    Toast.makeText(this, "Insira a unidade de medida!", Toast.LENGTH_SHORT).show()
                }
                estoque.isNotEmpty() && spinnerEstoque == options[0].toString() -> {
                    Toast.makeText(this, "Selecione um formato no campo estoque!", Toast.LENGTH_SHORT).show()
                }
                spinnerEstoque != options[0].toString() && estoque.isEmpty() -> {
                    Toast.makeText(this, "Informe um valor no campo estoque!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val sharedPreferences = getSharedPreferences("FormatoPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("selectedFormat", spinnerFormato)
                    editor.apply()

                    val resultado = medicamentoRepository.inserirMedicamento(
                        nomeMedicamento,
                        spinnerFormato,
                        unidadeMedida,
                        spinnerUnidadeMedida,
                        if (estoque.isEmpty()) -1 else estoque.toInt(),
                        if (spinnerEstoque != options[0]) null else spinnerEstoque
                    )

                    if (resultado > 0) {
                        Toast.makeText(this, "Medicamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        RouterManager.direcionarParaCadastroAlerta(this, resultado)
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar medicamento.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        SpinnerUtils.setupFormatoSpinnerListener(
            binding.spinnerFormato,
            binding.spinnerUnidadeMedida,
            resources
        )


    }
}
