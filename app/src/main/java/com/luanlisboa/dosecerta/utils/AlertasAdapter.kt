package com.luanlisboa.dosecerta.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Agenda

class AlertasAdapter(private var alertas: List<Agenda>) :
    RecyclerView.Adapter<AlertasAdapter.AlertaViewHolder>() {

    class AlertaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconAlert: ImageView = view.findViewById(R.id.iconAlert) // Ícone do alerta
        val medicationName: TextView = view.findViewById(R.id.medicationName) // Nome do medicamento
        val timeAlert: TextView = view.findViewById(R.id.timeAlert)  // Horário do medicamento
        val dosageAlert: TextView = view.findViewById(R.id.dosageAlert)  // Dosagem do medicamento
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta_timeline, parent, false)
        return AlertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertaViewHolder, position: Int) {
        val alerta = alertas[position]
        holder.medicationName.text = alerta.nome // Exibe o nome do medicamento
        holder.timeAlert.text = alerta.hora // Exibe o horário da dose
        holder.dosageAlert.text = alerta.dosagem // Exibe a dosagem

        // Define o ícone para o alerta, caso tenha diferentes tipos de medicamentos
        holder.iconAlert.setImageResource(R.drawable.ic_medicine)
    }

    override fun getItemCount(): Int = alertas.size

    // Atualiza a lista de alertas no RecyclerView
    fun updateAlertas(newAlertas: List<Agenda>) {
        this.alertas = newAlertas
        notifyDataSetChanged()
    }
}
