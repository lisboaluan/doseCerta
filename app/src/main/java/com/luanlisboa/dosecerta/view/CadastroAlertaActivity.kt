package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.databinding.ActivityCadastroAlertaBinding
import com.luanlisboa.dosecerta.repository.AlertaRepository
import com.luanlisboa.dosecerta.repository.MedicamentoRepository
import com.luanlisboa.dosecerta.repository.UsuarioRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.PickerUtils
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class CadastroAlertaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroAlertaBinding
    private lateinit var alertaRepository: AlertaRepository
    private lateinit var medicamentoRepository: MedicamentoRepository
    private lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroAlertaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alertaRepository = AlertaRepository(this)
        medicamentoRepository = MedicamentoRepository(this)
        usuarioRepository = UsuarioRepository(this)

       /* binding.btnConfirmar.setOnClickListener {
            val horarioDose = binding.horarioPrimeiraDoseValue.text.toString()
            val periodicidade = binding.periodicidadeValue.text.toString()
            val duracaoTratamento = binding.duracaoTratamentoValue.text.toString()
            val dose = binding.doseValue.text.toString()
            val notificar = if (binding.switchNotificar.isChecked) 1 else 0

            val resultado = alertaRepository.inserirAlerta(
                periodicidade,
                horarioDose,
                duracaoTratamento,
                dose,
                notificar,
                idMedicamento,
                idUsuario
            )

            if (resultado > 0) {
                SnackbarUtils.mensagem(it, "Alerta cadastrado com sucesso!")
                RouterManager.direcionarParaHome(this)
            } else {
                SnackbarUtils.mensagem(it, "Erro ao cadastrar alerta.")
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        } */

        // Recuperar o formato selecionado no "Cadastro de Medicamentos"
        val sharedPreferences = getSharedPreferences("FormatoPrefs", MODE_PRIVATE)
        val selectedFormat = sharedPreferences.getString("selectedFormat", "unidade")

        // Configurar o campo "Dose" com base no formato recuperado
        binding.doseValue.setOnClickListener {
            PickerUtils.showDosePickerDialog(this, binding.doseValue, selectedFormat ?: "unidade")
        }

        setupTimePicker()
        setupPeriodicidadePicker()
        setupDuracaoTratamentoPicker()
    }

    private fun setupTimePicker() {
        binding.horarioPrimeiraDoseValue.setOnClickListener {
            PickerUtils.showTimePickerDialog(this, binding.horarioPrimeiraDoseValue)
        }
    }

    private fun setupPeriodicidadePicker() {
        binding.periodicidadeValue.setOnClickListener {
            PickerUtils.showNumberPickerDialog(this, binding.periodicidadeValue, "Escolha um intervalo", " horas")
        }
    }

    private fun setupDuracaoTratamentoPicker() {
        binding.duracaoTratamentoValue.setOnClickListener {
            PickerUtils.showNumberPickerDialog(this, binding.duracaoTratamentoValue, "Escolha a duração do tratamento", " dias")
        }
    }
}



