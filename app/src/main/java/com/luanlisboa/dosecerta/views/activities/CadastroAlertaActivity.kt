package com.luanlisboa.dosecerta.views.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityCadastroAlertaBinding
import com.luanlisboa.dosecerta.repositories.AgendaRepository
import com.luanlisboa.dosecerta.repositories.AlertaRepository
import com.luanlisboa.dosecerta.repositories.MedicamentoRepository
import com.luanlisboa.dosecerta.repositories.UsuarioRepository
import com.luanlisboa.dosecerta.utils.RouterManager
import com.luanlisboa.dosecerta.utils.PickerUtils
import com.luanlisboa.dosecerta.utils.SnackbarUtils
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class CadastroAlertaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroAlertaBinding
    private lateinit var alertaRepository: AlertaRepository
    private lateinit var medicamentoRepository: MedicamentoRepository
    private lateinit var usuarioRepository: UsuarioRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroAlertaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alertaRepository = AlertaRepository(this)
        medicamentoRepository = MedicamentoRepository(this)
        usuarioRepository = UsuarioRepository(this)

        binding.btnConfirmar.setOnClickListener {
            val horarioDose = binding.horarioPrimeiraDoseValue.text.toString()
            val periodicidade = binding.periodicidadeValue.text.toString()
            val duracaoTratamento = binding.duracaoTratamentoValue.text.toString()
            val dose = binding.doseValue.text.toString()
            val notificar = if (binding.switchNotificar.isChecked) 1 else 0
            val idMedicamento = intent.getLongExtra("idMedicamento", -1).toLong()

            val resultado = alertaRepository.inserirAlerta(
                periodicidade,
                horarioDose,
                duracaoTratamento,
                dose,
                notificar,
                idMedicamento
            )

            if (resultado > 0) {
                createNotificationAlerts(
                    resultado,
                    horarioDose.replace(" horas", "").trim(),
                    periodicidade.replace(" horas", "").trim(),
                    duracaoTratamento.replace(" dias", "").trim(),
                    idMedicamento.toInt()
                )
                SnackbarUtils.mensagem(it, "Alerta cadastrado com sucesso!")
                RouterManager.direcionarParaHome(this)
            } else {
                SnackbarUtils.mensagem(it, "Erro ao cadastrar alerta.")
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationAlerts(
        idAlerta: Long,
        horarioPrimeiraDose: String,
        periodicidade: String,
        duracaoTratamento: String,
        idMedicamento: Int
    ) {
        val quantidadeDeDoses = ceil(duracaoTratamento.toDouble() * 24 / periodicidade.toDouble()).toInt()

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val localTime = LocalTime.parse(horarioPrimeiraDose, formatter)
        val currentDate = LocalDateTime.now().toLocalDate()
        var localDateTime = LocalDateTime.of(currentDate, localTime)
        val agendaRepository = AgendaRepository(this)

        repeat(quantidadeDeDoses) {
            val resultado = agendaRepository.inserirAgenda(
                localDateTime.toString(),
                0,
                idAlerta,
                0,
                idMedicamento
            )
            localDateTime = localDateTime.plusHours(periodicidade.toLong())
        }
    }
}