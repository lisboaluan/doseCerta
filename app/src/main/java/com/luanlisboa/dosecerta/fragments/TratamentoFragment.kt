package com.luanlisboa.dosecerta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.FragmentTratamentoBinding
import com.luanlisboa.dosecerta.models.Medicamento
import com.luanlisboa.dosecerta.repository.AlertaRepository
import com.luanlisboa.dosecerta.repository.MedicamentoRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.TratamentoAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TratamentoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tratamentoAdapter: TratamentoAdapter
    private lateinit var tratamentoList: MutableList<Medicamento>

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
    ): View {
        val binding = FragmentTratamentoBinding.inflate(inflater, container, false)

        // Configura o RecyclerView
        setupRecyclerView(binding)

        // Carregar dados do banco de dados
        tratamentoList = buscarTratamentosDoBanco().toMutableList()

        // Configurar o adapter com os dados e o callback de exclusão
        tratamentoAdapter = TratamentoAdapter(tratamentoList) { medicamento ->
            excluirMedicamento(medicamento)
        }
        binding.resumoTratamentos.adapter = tratamentoAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCadastroMedicamento: Button = view.findViewById(R.id.btnCadastrarTratamento)

        btnCadastroMedicamento.setOnClickListener {
            RouterManager.direcionarParaCadastroMedicamento(this)
        }
    }

    private fun excluirMedicamento(medicamento: Medicamento) {
        val medicamentoRepository = MedicamentoRepository(requireContext())
        val resultado = medicamentoRepository.deletarMedicamento(medicamento.id!!)

        if (resultado > 0) {
            // Remove o medicamento da lista e notifica o adapter
            tratamentoList.remove(medicamento)
            tratamentoAdapter.notifyDataSetChanged()

            Toast.makeText(requireContext(), "Medicamento excluído com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Erro ao excluir medicamento!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TratamentoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setupRecyclerView(binding: FragmentTratamentoBinding) {
        binding.resumoTratamentos.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun buscarTratamentosDoBanco(): List<Medicamento> {
        val medicamentoRepository = MedicamentoRepository(requireContext())
        val alertaRepository = AlertaRepository(requireContext())

        // Recuperar dados dos medicamentos e alertas
        val medicamentos = medicamentoRepository.getAllMedicamentos()

        // Mapear os alertas para cada medicamento com base no id do medicamento
        return medicamentos.map { medicamento ->
            // Buscar alertas específicos para cada medicamento
            val alertas = alertaRepository.getAllAlertas(medicamento.id!!)

            // Verificar se há alerta relacionado, caso contrário, usar valores padrão
            val alerta = alertas.firstOrNull()

            Medicamento(
                id = medicamento.id,
                nome = medicamento.nome,
                formato = alerta?.dosagem ?: "Sem dosagem", // Valor padrão caso não haja alerta
                medida = medicamento.medida,
                unidMedida = medicamento.unidMedida,
                quantEstoque = medicamento.quantEstoque,
                formatoEstoque = medicamento.formatoEstoque,
                horarioPrimeiraDose = alerta?.horarioPrimeiraDose ?: "Sem horário" // Valor padrão caso não haja alerta
            )
        }
    }
}
