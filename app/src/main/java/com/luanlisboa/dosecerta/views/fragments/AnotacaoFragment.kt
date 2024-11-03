package com.luanlisboa.dosecerta.views.fragments

import com.luanlisboa.dosecerta.adapters.AnotacoesAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.models.Anotacoes
import com.luanlisboa.dosecerta.repositories.AnotacaoRepository
import com.luanlisboa.dosecerta.views.activities.AnotacaoActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AnotacaoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnotacoesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anotacao, container, false)

        // Configura o botão para criar nova anotação
        val btnCriarAnotacao: MaterialButton = view.findViewById(R.id.btnCriarAnotacao)
        btnCriarAnotacao.setOnClickListener {
            val intent = Intent(activity, AnotacaoActivity::class.java)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.recyclerViewAnotacoes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = AnotacoesAdapter(
            emptyList(),
            onDelete = { anotacao -> deletarAnotacao(anotacao) }
        )
        recyclerView.adapter = adapter
        atualizarAnotacoes()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnotacaoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun buscarAnotacoesDoBanco(): List<Anotacoes> {
        val anotacoesRepository = AnotacaoRepository(requireContext())
        val anotacoes = anotacoesRepository.getAllAnotacoes()

        return anotacoes
    }

    // Função chamada para deletar uma anotação
    private fun deletarAnotacao(anotacao: Anotacoes) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Anotação")
            .setMessage("Você tem certeza que deseja deletar esta anotação?")
            .setPositiveButton("Sim") { _, _ ->
                AnotacaoRepository(requireContext()).deletarAnotacao(anotacao)
                atualizarAnotacoes()
            }
            .setNegativeButton("Não", null)
            .show()
    }


    // Função para buscar as anotações do banco e atualizar o adapter
    private fun atualizarAnotacoes() {
        val anotacoes = buscarAnotacoesDoBanco()
        adapter.updateAnotacoes(anotacoes)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged() // Notifica o adapter sobre as mudanças
    }

    // Atualiza a lista de anotações ao retornar para a tela
    override fun onResume() {
        super.onResume()
        atualizarAnotacoes()
    }
}