package com.luanlisboa.dosecerta.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityEditarMedicamentoBinding
import com.luanlisboa.dosecerta.repositories.MedicamentoRepository
import com.luanlisboa.dosecerta.utils.RouterManager

class EditarMedicamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarMedicamentoBinding
    private lateinit var medicamentoRepository: MedicamentoRepository
    private var medicamentoId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializando o View Binding
        binding = ActivityEditarMedicamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicamentoRepository = MedicamentoRepository(this)

        // Recupera o ID do medicamento enviado pela Intent
        medicamentoId = intent.getLongExtra("medicamentoId", -1).takeIf { it != -1L }
        medicamentoId?.let { carregarDadosMedicamento(it) }

        // Configura o botão de salvar
        binding.btnSalvar.setOnClickListener {
            salvarMedicamento()
        }

        // Configura o botão voltar com confirmação
        binding.btnVoltarEditar.setOnClickListener {
            confirmarVoltar()
        }
    }

    private fun carregarDadosMedicamento(id: Long) {
        medicamentoRepository.getMedicamentoById(id)?.let { medicamento ->
            val formatoArray = resources.getStringArray(R.array.formato)
            val unidMedidaArray = resources.getStringArray(R.array.unidadeMedida)
            val estoqueFormatoArray = resources.getStringArray(R.array.formato)

            // Preenche os campos com os dados do medicamento
            binding.editNomeMedicamentoEditar.setText(medicamento.nome)
            binding.spinnerFormatoEditar.setSelection(formatoArray.indexOf(medicamento.formato))
            binding.editUnidadeMedidaEditar.setText(medicamento.medida)
            binding.spinnerUnidadeMedidaEditar.setSelection(unidMedidaArray.indexOf(medicamento.unidMedida))
            binding.editEstoqueEditar.setText(medicamento.quantEstoque.toString())
            binding.spinnerEstoqueEditar.setSelection(estoqueFormatoArray.indexOf(medicamento.formatoEstoque))
        }
    }

    private fun salvarMedicamento() {
        val nome = binding.editNomeMedicamentoEditar.text.toString()
        val formato = binding.spinnerFormatoEditar.selectedItem.toString()
        val medida = binding.editUnidadeMedidaEditar.text.toString()
        val unidMedida = binding.spinnerUnidadeMedidaEditar.selectedItem.toString()
        val quantEstoque = binding.editEstoqueEditar.text.toString().toIntOrNull() ?: 0
        val formatoEstoque = binding.spinnerEstoqueEditar.selectedItem.toString()

        // Validação básica dos campos
        if (nome.isEmpty() || medida.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado: Int = if (medicamentoId != null) {
            // Atualiza o medicamento existente
            medicamentoRepository.atualizarMedicamento(
                id = medicamentoId!!,
                nome = nome,
                formato = formato,
                medida = medida,
                unidMedida = unidMedida,
                quantEstoque = quantEstoque,
                formatoEstoque = formatoEstoque
            )
        } else {
            // Insere um novo medicamento
            medicamentoRepository.inserirMedicamento(
                nome = nome,
                formato = formato,
                medida = medida,
                unidMedida = unidMedida,
                quantEstoque = quantEstoque,
                formatoEstoque = formatoEstoque
            ).toInt() // Certifique-se de que o retorno é do tipo Int
        }

        if (resultado > 0) {
            Toast.makeText(this, "Operação realizada com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
        } else {
            Toast.makeText(this, "Erro ao realizar operação!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_CANCELED)
        }
        finish()
        RouterManager.direcionarParaHome(this)
    }

    private fun confirmarVoltar() {
        AlertDialog.Builder(this)
            .setTitle("Descartar alterações?")
            .setMessage("Você tem certeza que deseja sair sem salvar as alterações?")
            .setPositiveButton("Sim") { _, _ -> finish() }
            .setNegativeButton("Não", null)
            .show()
    }
}
