package com.luanlisboa.dosecerta.views.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.adapters.CustomSpinnerAdapter
import com.luanlisboa.dosecerta.databinding.ActivityEditarTratamentoBinding
import com.luanlisboa.dosecerta.repositories.AlertaRepository
import com.luanlisboa.dosecerta.repositories.MedicamentoRepository
import com.luanlisboa.dosecerta.utils.PickerUtils
import com.luanlisboa.dosecerta.utils.RouterManager
import com.luanlisboa.dosecerta.utils.SpinnerUtils

class EditarTratamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarTratamentoBinding
    private lateinit var medicamentoRepository: MedicamentoRepository
    private lateinit var alertaRepository: AlertaRepository
    private var medicamentoId: Long? = null
    private var alertaId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializando o View Binding
        binding = ActivityEditarTratamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicamentoRepository = MedicamentoRepository(this)
        alertaRepository = AlertaRepository(this)

        // Configurações dos PickerDialogs
        configurarPickerDialogs()

        val options = resources.getStringArray(R.array.formato)
        val adapter = CustomSpinnerAdapter(this, options)
        binding.spinnerFormatoEditar.adapter = adapter

        // Recupera o ID do medicamento enviado pela Intent
        medicamentoId = intent.getLongExtra("medicamentoId", -1).takeIf { it != -1L }
        medicamentoId?.let { carregarDadosMedicamento(it) }

        // Recupera o ID do alerta enviado pela Intent
        alertaId = intent.getLongExtra("alertaId", -1).takeIf { it != -1L }
        alertaId?.let { carregarDadosAlerta(it) }

        // Configura o botão de salvar
        binding.btnSalvar.setOnClickListener {
            salvarMedicamento()
        }

        // Configura o botão voltar com confirmação
        binding.btnVoltarEditar.setOnClickListener {
            confirmarVoltar()
        }

        SpinnerUtils.setupFormatoSpinnerListener(
            binding.spinnerFormatoEditar,
            binding.spinnerUnidadeMedidaEditar,
            resources
        )
    }

    private fun configurarPickerDialogs() {
        // Horário da Primeira Dose
        binding.horarioPrimeiraDoseEditar.setOnClickListener {
            PickerUtils.showTimePickerDialog(this, binding.horarioPrimeiraDoseEditar)
        }

        // Periodicidade
        binding.periodicidadeEditar.setOnClickListener {
            PickerUtils.showNumberPickerDialog(
                context = this,
                textView = binding.periodicidadeEditar,
                title = "Escolha a periodicidade",
                suffix = " hora(s)"
            )
        }

        // Duração do Tratamento
        binding.duracaoTratamentoEditar.setOnClickListener {
            PickerUtils.showNumberPickerDialog(
                context = this,
                textView = binding.duracaoTratamentoEditar,
                title = "Escolha a duração do tratamento",
                suffix = " dia(s)"
            )
        }

        // Dose
        binding.doseEditar.setOnClickListener {
            PickerUtils.showDosePickerDialog(
                context = this,
                textView = binding.doseEditar,
                format = "dose(s)"
            )
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

    private fun carregarDadosAlerta(id: Long) {
        alertaRepository.getAlertaById(id)?.let { alerta ->
            // Preenche os campos com os dados do alerta
            binding.horarioPrimeiraDoseEditar.text = alerta.horarioPrimeiraDose
            binding.periodicidadeEditar.text = alerta.periodicidade
            binding.duracaoTratamentoEditar.text = alerta.diasTratamento
            binding.doseEditar.text = alerta.dosagem
            binding.switchNotificarEditar.isChecked = alerta.notificar == 1
        } ?: run {
            // Exibe uma mensagem de erro se o alerta não for encontrado
            Toast.makeText(this, "Erro ao carregar os dados do alerta!", Toast.LENGTH_SHORT).show()
            finish()
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

    private fun salvarAlerta(alertaId: Long) {
        val horarioPrimeiraDose = binding.horarioPrimeiraDoseEditar.text.toString()
        val periodicidade = binding.periodicidadeEditar.text.toString()
        val duracaoTratamento = binding.duracaoTratamentoEditar.text.toString()
        val dose = binding.doseEditar.text.toString()
        val notificar = if (binding.switchNotificarEditar.isChecked) 1 else 0

        // Validação dos campos
        if (horarioPrimeiraDose.isEmpty() || periodicidade.isEmpty() || duracaoTratamento.isEmpty() || dose.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = alertaRepository.atualizarAlerta(
            id = alertaId,
            periodicidade = periodicidade,
            horarioPrimeiraDose = horarioPrimeiraDose,
            diasTratamento = duracaoTratamento,
            dosagem = dose,
            notificar = notificar
        )

        if (resultado > 0) {
            Toast.makeText(this, "Alerta atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Erro ao atualizar o alerta!", Toast.LENGTH_SHORT).show()
        }
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
