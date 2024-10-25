package com.luanlisboa.dosecerta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanlisboa.dosecerta.databinding.FragmentAgendaBinding
import com.luanlisboa.dosecerta.repository.AgendaRepository
import com.luanlisboa.dosecerta.utils.AlertasAdapter

class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AlertasAdapter
    private lateinit var repository: AgendaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o RecyclerView
        binding.alertRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AlertasAdapter(emptyList())
        binding.alertRecyclerView.adapter = adapter

        // Instanciar o repositório (que busca os alertas/doses)
        repository = AgendaRepository(requireContext())

        // Listener para mudanças de data no CalendarView
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            carregarAlertasDoDia(selectedDate)
        }
    }

    private fun carregarAlertasDoDia(data: String) {
        val agendas = repository.obterAgendasPorData(data)

        if (agendas.isEmpty()) {
            // Não há alertas para a data selecionada
            binding.alertRecyclerView.visibility = View.GONE
            binding.emptyMessage.visibility = View.VISIBLE
        } else {
            // Exibe os alertas no RecyclerView
            binding.alertRecyclerView.visibility = View.VISIBLE
            binding.emptyMessage.visibility = View.GONE
            adapter.updateAlertas(agendas)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
