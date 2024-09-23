import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Anotacoes

class AnotacoesAdapter(private val anotacoes: List<Anotacoes>) :
    RecyclerView.Adapter<AnotacoesAdapter.AnotacaoViewHolder>() {

    class AnotacaoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloAnotacao: TextView = view.findViewById(R.id.tvTituloAnotacao)
        val mensagemAnotacao: TextView = view.findViewById(R.id.tvMensagemAnotacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resumo_cadastro_anotacoes, parent, false)
        return AnotacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {
        val anotacao = anotacoes[position]
        holder.tituloAnotacao.text = anotacao.titulo
        holder.mensagemAnotacao.text = anotacao.mensagem
    }

    override fun getItemCount(): Int = anotacoes.size
}
