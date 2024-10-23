package com.luanlisboa.dosecerta.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Alerta

class AlertasAdapter(private var alertas: List<Alerta>) :
    RecyclerView.Adapter<AlertasAdapter.AlertaViewHolder>() {

    class AlertaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val horarioTextView: TextView = view.findViewById(R.id.horarioTextView)
        val dosagemTextView: TextView = view.findViewById(R.id.dosagemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta, parent, false)
        return AlertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertaViewHolder, position: Int) {
        val alerta = alertas[position]
        holder.horarioTextView.text = alerta.horarioPrimeiraDose
        holder.dosagemTextView.text = alerta.dosagem
    }

    override fun getItemCount(): Int = alertas.size

    fun updateAlertas(newAlertas: List<Alerta>) {
        this.alertas = newAlertas
        notifyDataSetChanged()
    }
}
