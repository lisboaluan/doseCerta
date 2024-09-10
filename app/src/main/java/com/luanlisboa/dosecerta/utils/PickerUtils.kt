package com.luanlisboa.dosecerta.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import java.util.Calendar

object PickerUtils {

        fun showDatePickerDialog(context: Context, editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(date)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    fun showDosePickerDialog(context: Context, textView: TextView, format: String) {
        val options = Array(10) { i -> "${i + 1} $format" }

        val numberPicker = NumberPicker(context).apply {
            minValue = 1
            maxValue = options.size
            displayedValues = options
        }

        AlertDialog.Builder(context)
            .setTitle("Escolha a dose")
            .setView(numberPicker)
            .setPositiveButton("OK") { _, _ ->
                val selectedValue = numberPicker.value
                textView.text = options[selectedValue - 1]
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    fun showTimePickerDialog(context: Context, textView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            textView.text = "$selectedTime horas"
        }, hour, minute, true).show()
    }

    fun showNumberPickerDialog(context: Context, textView: TextView, title: String, suffix: String) {
        val numberPicker = NumberPicker(context).apply {
            minValue = 1
            maxValue = 30
        }

        AlertDialog.Builder(context)
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