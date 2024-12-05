package com.luanlisboa.dosecerta.adapters

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Medicamento
import com.luanlisboa.dosecerta.repositories.AlertaRepository
import com.luanlisboa.dosecerta.views.activities.EditarTratamentoActivity


class TratamentoAdapter(
    private val tratamentos: List<Medicamento>,
    private val onMedicamentoExcluido: (Medicamento) -> Unit  // Callback para excluir medicamento

) : RecyclerView.Adapter<TratamentoAdapter.TratamentoViewHolder>() {

    class TratamentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeMedicamento: TextView = view.findViewById(R.id.tvTituloAnotacao)
        val dosagem: TextView = view.findViewById(R.id.tvDosagem)
        val horario: TextView = view.findViewById(R.id.tvMensagemAnotacao)
        val avisoEstoqueBaixo: LinearLayout = view.findViewById(R.id.llAvisoEstoqueBaixo)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)  // Botão de edição
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TratamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resumo_cadastro_tratamento, parent, false)
        return TratamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TratamentoViewHolder, position: Int) {
        val tratamento = tratamentos[position]
        holder.nomeMedicamento.text = tratamento.nome
        holder.dosagem.text = "Dosagem: ${tratamento.formato}"
        holder.horario.text = "Horário: ${tratamento.horarioPrimeiraDose}"

        if (tratamento.quantEstoque != null) {
            if (tratamento.quantEstoque < 3) {
                holder.avisoEstoqueBaixo.visibility = View.VISIBLE
            }
        } else {
            holder.avisoEstoqueBaixo.visibility = View.GONE
        }

        // Configura o clique do botão de excluir
        holder.btnExcluir.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Excluir Medicamento")
                .setMessage("Você tem certeza que deseja excluir este medicamento?")
                .setPositiveButton("Sim") { _, _ ->
                    onMedicamentoExcluido(tratamento)  // Chama o callback ao excluir
                }
                .setNegativeButton("Não", null)
                .show()
        }

        // Configura o clique do botão de editar
        holder.btnEditar.setOnClickListener {
            var alertaRepository = AlertaRepository(holder.itemView.context)
            // Abrir a EditarMedicamentoActivity passando o ID do medicamento
            val intent = Intent(holder.itemView.context, EditarTratamentoActivity::class.java)
            intent.putExtra("medicamentoId", tratamento.id)  // Passa o ID do medicamento
            intent.putExtra("alertaId",
                tratamento.id?.let { it1 -> alertaRepository.getAlertaIdByMedicamentoId(it1.toLong()) })  // Passa o ID do medicamento
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = tratamentos.size
}
