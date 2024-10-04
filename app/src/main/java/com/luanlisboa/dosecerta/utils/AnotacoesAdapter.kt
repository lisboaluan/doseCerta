import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Anotacoes
import java.text.SimpleDateFormat
import java.util.Locale

class AnotacoesAdapter(private val anotacoes: List<Anotacoes>) :
    RecyclerView.Adapter<AnotacoesAdapter.AnotacaoViewHolder>() {

    class AnotacaoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloAnotacao: TextView = view.findViewById(R.id.tvTituloAnotacao)
        val mensagemAnotacao: TextView = view.findViewById(R.id.tvMensagemAnotacao)
        val dataCriacao: TextView = view.findViewById(R.id.tvDataCriacaoAnotacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resumo_cadastro_anotacoes, parent, false)
        return AnotacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {
        val anotacao = anotacoes[position]
        holder.tituloAnotacao.text = anotacao.titulo
        holder.mensagemAnotacao.text = anotacao.mensagem

        // Formatar a data de criação
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            val date = inputFormat.parse(anotacao.dataCriacao)
            val formattedDate = outputFormat.format(date)
            holder.dataCriacao.text = formattedDate
        } catch (e: Exception) {
            e.printStackTrace()
            holder.dataCriacao.text = anotacao.dataCriacao // Caso ocorra um erro, vai exibir a data original
        }
    }

    override fun getItemCount(): Int = anotacoes.size
}

