package com.luanlisboa.dosecerta.views.fragments

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanlisboa.dosecerta.databinding.FragmentAgendaBinding
import com.luanlisboa.dosecerta.repositories.AgendaRepository
import com.luanlisboa.dosecerta.adapters.AlertasAdapter
import com.luanlisboa.dosecerta.models.Agenda
import java.io.File
import java.io.FileOutputStream
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
            binding.btnRelatorio.setOnClickListener{
                val file = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    generateNamePdf()
                )
                onExportPdf(file, agendas)
            }
        }
    }

    private fun onExportPdf(file: File, alertas: List<Agenda>) {
        val pdfDocument = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas: Canvas = page.canvas
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 16f

        var yPosition = 100f

        for (item in alertas) {
            val medicamentoTomado = if (item.situacaoIngestao == 1) "Tomado" else "Não tomado"
            canvas.drawText("• ${item.nome} - ${item.hora} - ${medicamentoTomado}", 100f, yPosition, paint)
            yPosition += 30f
        }

        pdfDocument.finishPage(page)

        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
            fileOutputStream.close()
            openPdf(file)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao gerar o PDF.", Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()
    }

    private fun openPdf(file: File) {
        val uri = FileProvider.getUriForFile(requireContext(), "com.seu.pacote.app.fileprovider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(intent)
    }

    private fun generateNamePdf(): String {
        val date = getCurrentDate()
        return "Relatorio_$date.pdf"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
