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
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.FragmentAgendaBinding
import com.luanlisboa.dosecerta.repositories.AgendaRepository
import com.luanlisboa.dosecerta.adapters.AlertasAdapter
import com.luanlisboa.dosecerta.models.Agenda
import com.luanlisboa.dosecerta.models.Relatorio
import com.luanlisboa.dosecerta.repositories.RelatorioRepository
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
            binding.btnRelatorio.setOnClickListener {
                exibirPopupSelecaoDatas { dataInicio, dataFim ->
                    val relatorioRepository = RelatorioRepository(requireContext())
                    val relatorios = relatorioRepository.buscarRelatoriosPorPeriodo(dataInicio, dataFim)

                    if (relatorios.isNotEmpty()) {
                        val file = File(
                            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                            "Relatorio_${dataInicio}_a_${dataFim}.pdf"
                        )
                        gerarRelatorioPdf(file, relatorios)
                    } else {
                        Toast.makeText(requireContext(), "Nenhum dado encontrado no período selecionado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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

    private fun exibirPopupSelecaoDatas(callback: (String, String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_selecao_datas, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Selecione o Período")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val dataInicioPicker = dialogView.findViewById<DatePicker>(R.id.dataInicioPicker)
                val dataFimPicker = dialogView.findViewById<DatePicker>(R.id.dataFimPicker)

                val dataInicio = String.format(
                    "%04d-%02d-%02d",
                    dataInicioPicker.year,
                    dataInicioPicker.month + 1,
                    dataInicioPicker.dayOfMonth
                )
                val dataFim = String.format(
                    "%04d-%02d-%02d",
                    dataFimPicker.year,
                    dataFimPicker.month + 1,
                    dataFimPicker.dayOfMonth
                )

                callback(dataInicio, dataFim)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun gerarRelatorioPdf(file: File, relatorios: List<Relatorio>) {
        if (!isAdded || activity == null || context == null) {
            return
        }

        val pdfDocument = PdfDocument()
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 16f
        }

        var yPosition = 100f
        var currentPageNumber = 0
        var currentPage: PdfDocument.Page? = null
        var data: String
        var dataAnterior: String = ""

        try {

            // Caso não haja relatórios
            if (relatorios.isEmpty()) {
                currentPage = pdfDocument.startPage(
                    PdfDocument.PageInfo.Builder(595, 842, currentPageNumber).create()
                )
                currentPage?.canvas?.drawText("Nenhum dado disponível para exibir.", 100f, yPosition, paint)
                pdfDocument.finishPage(currentPage!!)
            } else {
                for ((index, relatorio) in relatorios.withIndex()) {
                    if (currentPage == null) {
                        currentPage = pdfDocument.startPage(
                            PdfDocument.PageInfo.Builder(595, 850, currentPageNumber).create()
                        )
                    }

                    var canvas = currentPage?.canvas
                    if (canvas == null) {
                        return
                    }

                    // Verifica se ultrapassou o limite da página
                    if (yPosition > 800f) {
                        try {
                            pdfDocument.finishPage(currentPage!!)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        // Cria uma nova página
                        currentPageNumber++
                        currentPage = pdfDocument.startPage(
                            PdfDocument.PageInfo.Builder(595, 850, currentPageNumber).create()
                        )
                        yPosition = 100f
                    }

                    // Adiciona o conteúdo na página
                    try {
                        val medicamentoNome = relatorio.nomeMedicamento ?: "Nome não disponível"
                        val nomeTruncado = if (medicamentoNome.length > 40) medicamentoNome.substring(0, 37) + "..." else medicamentoNome
                        data = relatorio.data
                        if (dataAnterior != data || dataAnterior == "") {
                            dataAnterior = data
                            canvas.drawText("Data: $data", 50f, yPosition, paint)
                            yPosition += 20f
                        }

                        if (currentPage != null) {
                            canvas = currentPage.canvas
                        }
                        if (canvas != null) {
                            canvas.drawText("Medicamento:" + nomeTruncado, 100f, yPosition, paint)
                        }
                        yPosition += 20f
                        val horaDosagem = relatorio.horaDosagem ?: "Hora não especificada"
                        val situacao = relatorio.situacaoIngestao ?: "Desconhecido"

                        if (canvas != null) {
                            canvas.drawText("Hora: $horaDosagem - Situação: $situacao", 100f, yPosition, paint)
                        }
                        yPosition += 30f
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // Finaliza a última página
                currentPage?.let {
                    try {
                        pdfDocument.finishPage(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            // Salva o documento no arquivo
            FileOutputStream(file).use { pdfDocument.writeTo(it) }
            openPdf(file)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Erro ao gerar o PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            try {
                pdfDocument.close()
            } catch (e: Exception) {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
