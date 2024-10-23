package com.luanlisboa.dosecerta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.repository.AgendaRepository
import com.luanlisboa.dosecerta.utils.AlertasAdapter

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AgendaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var calendarView: CalendarView
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var adapter: AlertasAdapter
    private lateinit var repository: AgendaRepository

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
        val view = inflater.inflate(R.layout.fragment_agenda, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        alertRecyclerView = view.findViewById(R.id.alertRecyclerView)
        emptyMessage = view.findViewById(R.id.emptyMessage)

        // Configurar o RecyclerView
        alertRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AlertasAdapter(emptyList())
        alertRecyclerView.adapter = adapter

        // Instanciar o repositório
        repository = AgendaRepository(requireContext())

        // Configurar listener para mudanças de data
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            carregarAlertasDoDia(selectedDate)
        }

        return view
    }

private fun carregarAlertasDoDia(data: String) {
    val alertas = repository.obterAlertasPorData(data)

    if (alertas.isEmpty()) {
        alertRecyclerView.visibility = View.GONE
        emptyMessage.visibility = View.VISIBLE
    } else {
        alertRecyclerView.visibility = View.VISIBLE
        emptyMessage.visibility = View.GONE
        adapter.updateAlertas(alertas)
    }
}

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgendaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}