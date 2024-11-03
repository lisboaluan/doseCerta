package com.luanlisboa.dosecerta.views.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanlisboa.dosecerta.databinding.FragmentAgendaBinding
import com.luanlisboa.dosecerta.repositories.AgendaRepository
import com.luanlisboa.dosecerta.adapters.AlertasAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AlertasAdapter
    private lateinit var repository: AgendaRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o RecyclerView
        binding.alertRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AlertasAdapter(emptyList())
        binding.alertRecyclerView.adapter = adapter

        // Instanciar o repositório
        repository = AgendaRepository(requireContext())

        // Obter a data atual e carregar alertas
        val currentDate = getCurrentDate()
        carregarAlertasDoDia(currentDate)

        // Listener para mudanças de data no CalendarView
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

            carregarAlertasDoDia(selectedDate)
        }
    }

    // Função para obter a data atual no formato desejado
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun carregarAlertasDoDia(data: String) {
        // Obtém as agendas e agenda notificações
        val agendas = repository.obterAgendasPorData(data, requireContext())

        if (agendas.isEmpty()) {
            binding.alertRecyclerView.visibility = View.GONE
            binding.emptyMessage.visibility = View.VISIBLE
        } else {
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
