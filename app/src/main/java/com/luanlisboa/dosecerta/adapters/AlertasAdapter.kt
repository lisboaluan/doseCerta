package com.luanlisboa.dosecerta.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Agenda
import java.text.SimpleDateFormat
import java.util.*

class AlertasAdapter(private var alertas: List<Agenda>) :
    RecyclerView.Adapter<AlertasAdapter.AlertaViewHolder>() {

    class AlertaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val medicationName: TextView = view.findViewById(R.id.medicationName) // Nome do medicamento
        val timeAlert: TextView = view.findViewById(R.id.timeAlert)  // Horário do medicamento
        val dosageAlert: TextView = view.findViewById(R.id.dosageAlert)  // Dosagem do medicamento
        val statusButton: ImageView = view.findViewById(R.id.statusButton) // Botão de status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta_timeline, parent, false)
        return AlertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertaViewHolder, position: Int) {
        val alerta = alertas[position]
        holder.medicationName.text = alerta.nome
        holder.timeAlert.text = alerta.hora
        holder.dosageAlert.text = alerta.dosagem

        // Formatação de data e hora
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDateTime = Date()
        val alertaDateTime = dateTimeFormat.parse("${alerta.dataDeIngestao} ${alerta.hora}") // Considerando que `alerta.data` esteja no formato "yyyy-MM-dd"

        // Verificação do status baseado em data e horário
        if (alertaDateTime != null) {
            when {
                // Caso o medicamento tenha sido tomado
                (alerta.situacaoIngestao == 1 && alertaDateTime.before(currentDateTime)) -> {
                    holder.statusButton.setImageResource(R.drawable.ic_status_green)
                }
                // Se a data e hora do alerta já passaram e o medicamento não foi tomado
                alertaDateTime.before(currentDateTime) -> {
                    holder.statusButton.setImageResource(R.drawable.ic_status_yellow)
                }
                // Caso a data e hora ainda não tenham chegado
                else -> {
                    holder.statusButton.setImageResource(R.drawable.ic_status_silver)
                }
            }
        } else {
            // Em caso de erro na conversão de data, deixe como cinza por padrão
            holder.statusButton.setImageResource(R.drawable.ic_status_silver)
        }
    }




    override fun getItemCount(): Int = alertas.size

    // Atualiza a lista de alertas no RecyclerView
    fun updateAlertas(newAlertas: List<Agenda>) {
        this.alertas = newAlertas
        notifyDataSetChanged()
    }
}
