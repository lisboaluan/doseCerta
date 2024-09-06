package com.luanlisboa.dosecerta.view

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.databinding.ActivityCadastroAlertaBinding
import java.util.Calendar

class CadastroAlertaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroAlertaBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroAlertaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)

        // Configura o comportamento do campo "Horário da 1º dose"
        val horarioPrimeiraDoseTextView: TextView = findViewById(R.id.horarioPrimeiraDoseValue)
        horarioPrimeiraDoseTextView.setOnClickListener {
            showTimePickerDialog(horarioPrimeiraDoseTextView)
        }

        // Configura o comportamento do campo "Periodicidade"
        val periodicidadeTextView: TextView = findViewById(R.id.periodicidadeValue)
        periodicidadeTextView.setOnClickListener {
            showNumberPickerDialog(periodicidadeTextView, "Escolha um intervalo", " horas")
        }

        // Configura o comportamento do campo "Duração do Tratamento"
        val duracaoTratamentoTextView: TextView = findViewById(R.id.duracaoTratamentoValue)
        duracaoTratamentoTextView.setOnClickListener {
            showNumberPickerDialog(duracaoTratamentoTextView, "Escolha a duração do tratamento", " dias")
        }
    }

    private fun showTimePickerDialog(textView: TextView) {
        // Obtém a hora atual para definir como padrão no TimePickerDialog
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Exibe o TimePickerDialog
        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            textView.text = "$selectedTime horas" // Define o horário selecionado no TextView
        }, hour, minute, true) // true para formato 24 horas

        timePickerDialog.show()
    }

    private fun showNumberPickerDialog(textView: TextView, title: String, suffix: String) {
        val numberPicker = NumberPicker(this).apply {
            minValue = 1
            maxValue = 30
        }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(numberPicker)
            .setPositiveButton("OK") { _, _ ->
                val selectedValue = numberPicker.value
                textView.text = "$selectedValue$suffix"
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}