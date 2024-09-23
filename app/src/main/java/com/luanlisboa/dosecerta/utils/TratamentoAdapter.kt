package com.luanlisboa.dosecerta.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Medicamento

class TratamentoAdapter(private val tratamentos: List<Medicamento>) :
    RecyclerView.Adapter<TratamentoAdapter.TratamentoViewHolder>() {

    class TratamentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeMedicamento: TextView = view.findViewById(R.id.tvTituloAnotacao)
        val dosagem: TextView = view.findViewById(R.id.tvDosagem)
        val horario: TextView = view.findViewById(R.id.tvMensagemAnotacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TratamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resumo_cadastro_tratamento, parent, false)
        return TratamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TratamentoViewHolder, position: Int) {
        val tratamento = tratamentos[position]
        holder.nomeMedicamento.text = tratamento.nome
        holder.dosagem.text = "Dosagem: ${tratamento.dosagem}"
        holder.horario.text = "Horário: ${tratamento.horario}"
    }

    override fun getItemCount(): Int = tratamentos.size
}
